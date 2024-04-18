package org.zadyraichuk.construction.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BuildingCompany extends CompanyConnector {

    @Setter
    private String description;
    private String email;
    private String phoneNumber;
    private String officeLocation;
    @Setter
    private String website;
    private List<ProjectConnector> projects;

    public BuildingCompany(int companyId,
                           String companyName,
                           UserConnector owner,
                           String email,
                           String phoneNumber,
                           String officeLocation) {
        super(companyId, companyName, owner);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.officeLocation = officeLocation;
        this.projects = new ArrayList<>();
    }

    public void addProject(ProjectConnector project) {
        this.projects.add(project);
    }

    public void removeProject(ProjectConnector project) {
        this.projects.remove(project);
    }
}
