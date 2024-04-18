package org.zadyraichuk.construction.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class ProjectConnector {

    private int projectId;
    private String projectName;
    @Setter
    private boolean isShowed = false;
    @Setter
    private UserConnector generalContractor;
    private CompanyConnector company;

    public ProjectConnector(int projectId,
                            String projectName,
                            CompanyConnector company) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectConnector that = (ProjectConnector) o;
        return projectId == that.projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId);
    }
}
