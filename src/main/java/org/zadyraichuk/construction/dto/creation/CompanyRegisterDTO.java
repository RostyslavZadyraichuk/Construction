package org.zadyraichuk.construction.dto.creation;

import lombok.Getter;

@Getter
public class CompanyRegisterDTO {

    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String webSite;
    private final String address;

    public CompanyRegisterDTO(String name, String email, String phoneNumber, String webSite, String address) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.webSite = webSite;
        this.address = address;
    }
}
