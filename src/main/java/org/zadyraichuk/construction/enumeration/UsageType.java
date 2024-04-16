package org.zadyraichuk.construction.enumeration;

import lombok.Getter;

@Getter
public enum UsageType {
    FREE(1, false, false, false, 3),
    PERSONAL(3, false, false, false, 5),
    SMALL_COMPANY(15, true, true, false, 8),
    BIG_COMPANY(50, true, true, true, 15);

    private final Integer maxProjectsCount;

    private final Boolean accessById;

    private final Boolean progressAutoSwitch;

    private final Boolean diagramView;

    private final Integer maxImagesPerProject;

    private final Boolean checkBeforeRegisterProject = true;

    private final Boolean progressMessages = true;

    UsageType(Integer maxProjectsCount,
              Boolean accessById,
              Boolean progressAutoSwitch,
              Boolean diagramView,
              Integer maxImagesPerProject) {
        this.maxProjectsCount = maxProjectsCount;
        this.accessById = accessById;
        this.progressAutoSwitch = progressAutoSwitch;
        this.diagramView = diagramView;
        this.maxImagesPerProject = maxImagesPerProject;
    }
}
