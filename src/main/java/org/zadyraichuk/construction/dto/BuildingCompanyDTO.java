package org.zadyraichuk.construction.dto;

import lombok.Getter;
import lombok.Setter;
import org.zadyraichuk.construction.dto.simple.CompanySimpleDTO;
import org.zadyraichuk.construction.dto.simple.UserSimpleDTO;

@Getter
public class BuildingCompanyDTO extends CompanySimpleDTO {

    @Setter
    private String description;
    private String email;
    private String phoneNumber;
    private String officeLocation;
    @Setter
    private String website;

    public BuildingCompanyDTO(String companyId,
                              String companyName,
                              UserSimpleDTO owner,
                              String email,
                              String phoneNumber,
                              String officeLocation) {
        super(companyId, companyName, owner);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.officeLocation = officeLocation;
    }

}
