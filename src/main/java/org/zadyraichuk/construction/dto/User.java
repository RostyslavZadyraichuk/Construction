package org.zadyraichuk.construction.dto;

import lombok.Getter;
import lombok.Setter;
import org.zadyraichuk.construction.enumeration.Role;

import java.util.ArrayList;
import java.util.List;

@Getter
public class User extends UserConnector {

    @Setter
    private List<Role> roles;
    private String userName;
    private final List<Message> messages;

    public User(int userId, String userName) {
        super(userId);
        this.userName = userName;
        this.messages = new ArrayList<>();
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
    }

    public void readMessage(Message message) {
        message.read();
    }
}
