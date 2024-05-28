package org.zadyraichuk.construction.dto.creation;

import lombok.Getter;

@Getter
public class LoginUserDTO {

    private final String userNameOrEmail;
    private final String password;

    public LoginUserDTO(String userNameOrEmail, String password) {
        this.userNameOrEmail = userNameOrEmail;
        this.password = password;
    }

}
