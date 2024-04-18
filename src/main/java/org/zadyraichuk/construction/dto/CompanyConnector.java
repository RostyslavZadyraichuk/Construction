package org.zadyraichuk.construction.dto;

import lombok.Getter;

@Getter
public class CompanyConnector {

    private int companyId;
    private String companyName;
    private UserConnector owner;

    public CompanyConnector(int companyId,
                            String companyName,
                            UserConnector owner) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.owner = owner;
    }
}
