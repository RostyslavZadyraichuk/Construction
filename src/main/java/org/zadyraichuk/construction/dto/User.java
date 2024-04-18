package org.zadyraichuk.construction.dto;

import lombok.Getter;
import org.zadyraichuk.construction.enumeration.Role;

import java.util.ArrayList;
import java.util.List;

@Getter
public class User extends UserConnector {

    private Role role;
    private String username;
    private final List<Message> messages;

    public User(int userId, Role role, String username) {
        super(userId);
        this.role = role;
        this.username = username;
        this.messages = new ArrayList<>();
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
