package org.zadyraichuk.construction.dto;

import lombok.Getter;
import lombok.Setter;
import org.zadyraichuk.construction.dto.simple.UserSimpleDTO;
import org.zadyraichuk.construction.enumeration.Role;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserDTO extends UserSimpleDTO {

    @Setter
    private String userName;
    @Setter
    private SubscriptionDTO subscription;
    private List<Role> roles;
    private final List<MessageDTO> messages;

    public UserDTO(String userId, String userName) {
        super(userId);
        this.userName = userName;
        this.messages = new ArrayList<>();
        this.roles = new ArrayList<>();
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public void addMessage(MessageDTO messageDTO) {
        this.messages.add(messageDTO);
    }

    public void removeMessage(MessageDTO messageDTO) {
        this.messages.remove(messageDTO);
    }

    public void readMessage(MessageDTO messageDTO) {
        messageDTO.read();
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", userName='" + userName + '\'' +
                ", subscriptionId=" + subscription.getId() +
                ", roles=" + roles +
                ", messagesSize=" + messages.size() +
                '}';
    }
}
