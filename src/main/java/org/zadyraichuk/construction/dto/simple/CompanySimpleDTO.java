package org.zadyraichuk.construction.dto.simple;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
public class CompanySimpleDTO {

    protected String companyId;
    protected String companyName;
    protected UserSimpleDTO owner;
    @Setter
    protected URL companyLogo;

    public CompanySimpleDTO(String companyId,
                            String companyName,
                            UserSimpleDTO owner) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.owner = owner;
    }
}
