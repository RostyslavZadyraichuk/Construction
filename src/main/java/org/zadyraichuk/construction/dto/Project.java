package org.zadyraichuk.construction.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Project extends ProjectConnector {

    @Setter
    private String description;
    private String buildingLocation;
    @Setter
    private boolean isVerified = false;
    private WorkingPlan workingPlan;
    private final List<String> actionsHistory;

    public Project(int projectId,
                   String projectName,
                   CompanyConnector company,
                   String buildingLocation,
                   WorkingPlan workingPlan) {
        super(projectId, projectName, company);
        this.buildingLocation = buildingLocation;
        this.workingPlan = workingPlan;
        actionsHistory = new ArrayList<>();
    }

    public void addAction(String action) {
        this.actionsHistory.add(action);
    }
}
