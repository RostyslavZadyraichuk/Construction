package org.zadyraichuk.construction.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Message {

    private String messageText;
    private boolean isNew = true;

    public Message(String messageText) {
        this.messageText = messageText;
    }

    public void read() {
        this.isNew = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(messageText, message.messageText);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(messageText);
    }
}
