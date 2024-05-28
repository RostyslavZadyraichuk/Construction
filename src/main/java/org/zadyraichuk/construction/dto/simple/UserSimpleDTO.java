package org.zadyraichuk.construction.dto.simple;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
public class UserSimpleDTO {

    protected String userId;
    @Setter
    protected String fullName;
    @Setter
    protected URL userPicture;

    public UserSimpleDTO(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserSimpleDTO{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
