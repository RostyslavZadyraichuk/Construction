package org.zadyraichuk.construction.dto.simple;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.Objects;

@Getter
public class ProjectSimpleDTO {

    protected String projectId;
    protected String projectName;
    @Setter
    protected UserSimpleDTO generalContractor;
    protected CompanySimpleDTO company;
    @Setter
    protected Boolean isShowed = false;
    @Setter
    protected URL projectPicture;

    public ProjectSimpleDTO(String projectId,
                            String projectName,
                            CompanySimpleDTO company) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectSimpleDTO that = (ProjectSimpleDTO) o;
        return Objects.equals(projectId, that.projectId)
                && Objects.equals(projectName, that.projectName)
                && Objects.equals(generalContractor, that.generalContractor)
                && Objects.equals(company, that.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, generalContractor, company);
    }
}
