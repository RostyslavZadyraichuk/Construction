package org.zadyraichuk.construction.dto;

import lombok.Getter;
import lombok.Setter;
import org.zadyraichuk.construction.dto.simple.CompanySimpleDTO;
import org.zadyraichuk.construction.dto.simple.ProjectSimpleDTO;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ProjectDTO extends ProjectSimpleDTO implements Progressable {

    @Setter
    private String description;
    private String buildingLocation;
    @Setter
    private Boolean isVerified = false;

    private List<URL> projectPictures;
    private List<URL> schemes;
    private WorkingPlanDTO workingPlanDTO;
    //TODO add general contractor actions history that owner can show what he did
//    private final List<String> actionsHistory;
    //TODO include review
//    private ReviewDTO reviewDTO;

    public ProjectDTO(String projectId,
                      String projectName,
                      CompanySimpleDTO company,
                      String buildingLocation,
                      WorkingPlanDTO workingPlanDTO) {
        super(projectId, projectName, company);
        this.buildingLocation = buildingLocation;
        this.workingPlanDTO = workingPlanDTO;
//        actionsHistory = new ArrayList<>();
        this.projectPictures = new ArrayList<>();
        this.schemes = new ArrayList<>();
    }

//    public void addAction(String action) {
//        this.actionsHistory.add(action);
//    }

    public void addPicture(URL picture) {
        this.projectPictures.add(picture);
    }

    public void addScheme(URL picture) {
        this.schemes.add(picture);
    }

    @Override
    public int calculateProgress() {
        return workingPlanDTO.calculateProgress();
    }

    @Override
    public String getCurrentStage() {
        return workingPlanDTO.getCurrentStage();
    }
}
