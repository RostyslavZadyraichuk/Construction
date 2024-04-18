package org.zadyraichuk.construction.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class UserConnector {

    private int userId;
    @Setter
    private String fullName;

    public UserConnector(int userId) {
        this.userId = userId;
    }
}
