package org.zadyraichuk.construction.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class MessageDTO {

    private String messageText;
    private boolean isNew = true;

    public MessageDTO(String messageText) {
        this.messageText = messageText;
    }

    public MessageDTO(String messageText, boolean isNew) {
        this.messageText = messageText;
        this.isNew = isNew;
    }

    public void read() {
        this.isNew = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDTO messageDTO = (MessageDTO) o;
        return Objects.equals(messageText, messageDTO.messageText);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(messageText);
    }

}
