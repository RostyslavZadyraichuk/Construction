package org.zadyraichuk.construction.dto.creation;

import lombok.Getter;
import org.zadyraichuk.construction.enumeration.Role;

@Getter
public class RegisterUserDTO {

    private final String userName;
    private final String password;
    private final String passwordConfirm;
    private final String email;
    private final String fullName;
    private final Role initRole;

    public RegisterUserDTO(String userName,
                           String password,
                           String passwordConfirm,
                           String email,
                           Role initRole) {
        this.userName = userName;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.email = email;
        this.fullName = null;
        this.initRole = initRole;
    }

    public RegisterUserDTO(String userName,
                           String password,
                           String passwordConfirm,
                           String email,
                           String fullName,
                           Role initRole) {
        this.userName = userName;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.email = email;
        this.fullName = fullName;
        this.initRole = initRole;
    }
}
