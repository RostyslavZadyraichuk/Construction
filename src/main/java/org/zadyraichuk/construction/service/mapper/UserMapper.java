package org.zadyraichuk.construction.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.zadyraichuk.construction.dto.MessageDTO;
import org.zadyraichuk.construction.dto.UserDTO;
import org.zadyraichuk.construction.dto.creation.RegisterUserDTO;
import org.zadyraichuk.construction.dto.simple.UserSimpleDTO;
import org.zadyraichuk.construction.entity.Subscription;
import org.zadyraichuk.construction.entity.User;
import org.zadyraichuk.construction.enumeration.Periodicity;
import org.zadyraichuk.construction.enumeration.Role;
import org.zadyraichuk.construction.enumeration.UsageType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = SubscriptionMapper.class)
public abstract class UserMapper extends GeneralMapper {

    protected SubscriptionMapper sm;

    @Autowired
    public void setSubscriptionMapper(SubscriptionMapper subscriptionMapper) {
        this.sm = subscriptionMapper;
    }

    @Mapping(source = "userName", target = "username")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "initRole", target = "roles", qualifiedByName = "addRole")
    @Mapping(target = "subscription", expression = "java(generateFreeSubscription())")
    public abstract User toEntity(RegisterUserDTO userDTO);

    @Mapping(source = "userId", target = "id", qualifiedByName = "toObjectId")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "toRolesArray")
    @Mapping(source = "messages", target = "newMessages", qualifiedByName = "toNewMessages")
    @Mapping(source = "messages", target = "checkedMessages", qualifiedByName = "toOldMessages")
    @Mapping(target = "subscription", expression = "java(sm.toEntity(userDTO.getSubscription()))")
    @Mapping(source = "userName", target = "username")
    @Mapping(source = "fullName", target = "fullName")
    public abstract User toEntity(UserDTO userDTO);

    @Mapping(source = "id", target = "userId", qualifiedByName = "toStringId")
    @Mapping(source = "fullName", target = "fullName")
    public abstract UserSimpleDTO toConnectorDTO(User user);

    @Mapping(source = "id", target = "userId", qualifiedByName = "toStringId")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "toRolesList")
    @Mapping(target = "messages", expression = "java(toMessagesDTO(user))")
    @Mapping(target = "subscription", expression = "java(sm.toDTO(user.getSubscription()))")
    @Mapping(source = "username", target = "userName")
    @Mapping(source = "fullName", target = "fullName")
    public abstract UserDTO toDTO(User user);

    protected Subscription generateFreeSubscription() {
        Subscription subscription = new Subscription();
        subscription.setOperationId("0");
        subscription.setUsageType(UsageType.FREE);
        subscription.setPaymentDate(LocalDate.now());
        subscription.setExpirationDate(LocalDate.now().plusYears(1));
        subscription.setPeriodicity(Periodicity.ANNUAL);
        return subscription;
    }

    protected List<MessageDTO> toMessagesDTO(User user) {
        String[] oldMessages = user.getCheckedMessages();
        String[] newMessages = user.getNewMessages();
        List<MessageDTO> messageDTOS = new ArrayList<>();

        if (oldMessages != null) {
            for (String oldMessage : oldMessages) {
                messageDTOS.add(new MessageDTO(oldMessage, false));
            }
        }

        if (newMessages != null) {
            for (String newMessage : newMessages) {
                messageDTOS.add(new MessageDTO(newMessage, true));
            }
        }

        return messageDTOS;
    }

    @Named("toOldMessages")
    public String[] toOldMessages(List<MessageDTO> messageDTOS) {
        return messageDTOS.stream()
                .filter(m -> !m.isNew())
                .map(MessageDTO::getMessageText)
                .toArray(String[]::new);
    }

    @Named("toNewMessages")
    public String[] toNewMessages(List<MessageDTO> messageDTOS) {
        return messageDTOS.stream()
                .filter(MessageDTO::isNew)
                .map(MessageDTO::getMessageText)
                .toArray(String[]::new);
    }

    @Named("addRole")
    public Role[] addRole(Role role) {
        return new Role[]{role};
    }

    @Named("toRolesArray")
    public Role[] toRolesArray(List<Role> roles) {
        return roles.toArray(new Role[0]);
    }

    @Named("toRolesList")
    public List<Role> toRolesList(Role[] roles) {
        return new ArrayList<>(List.of(roles));
    }

}
