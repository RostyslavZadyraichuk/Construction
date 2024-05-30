package org.zadyraichuk.construction.service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.zadyraichuk.construction.dto.ReviewDTO;
import org.zadyraichuk.construction.dto.simple.ProjectSimpleDTO;
import org.zadyraichuk.construction.entity.Review;
import org.zadyraichuk.construction.service.ProjectService;

@Mapper(componentModel = "spring")
public abstract class ReviewMapper extends GeneralMapper {

    protected ProjectService ps;

    @Autowired
    public void setPs(ProjectService ps) {
        this.ps = ps;
    }

    @Mapping(source = "description", target = "description")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "starRate", target = "rate")
    @Mapping(source = "project.projectId", target = "projectId", qualifiedByName = "toObjectId")
    @Mapping(source = "project.projectName", target = "projectName")
    @Mapping(source = "project.isShowed", target = "isProjectShowed")
    @Mapping(source = "project.generalContractor.userId", target = "generalContractorId", qualifiedByName = "toObjectId")
    @Mapping(source = "project.generalContractor.fullName", target = "generalContractorFullName")
    public abstract Review toEntity(ReviewDTO review);

    @Mapping(source = "description", target = "description")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "rate", target = "starRate")
    @Mapping(source = "projectId", target = "project", qualifiedByName = "findProject")
    public abstract ReviewDTO toDto(Review review);

    @Named("findProject")
    public ProjectSimpleDTO findProject(ObjectId id) {
        String projectId = toStringId(id);
        return ps.findSimpleById(projectId).orElse(null);
    }

}
