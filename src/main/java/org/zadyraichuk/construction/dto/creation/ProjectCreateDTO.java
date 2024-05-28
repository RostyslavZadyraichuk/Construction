package org.zadyraichuk.construction.dto.creation;

import lombok.Getter;

import java.io.File;
import java.util.List;

@Getter
public class ProjectCreateDTO {

    private final String projectName;
    private final String description;
    private final String buildingLocation;
    private final List<File> pictures;
    private final List<File> schemes;

    public ProjectCreateDTO(String projectName,
                            String description,
                            String buildingLocation,
                            List<File> pictures,
                            List<File> schemes) {
        this.projectName = projectName;
        this.description = description;
        this.buildingLocation = buildingLocation;
        this.pictures = pictures;
        this.schemes = schemes;
    }
}
