package org.zadyraichuk.construction.service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.zadyraichuk.construction.dto.ProjectDTO;
import org.zadyraichuk.construction.dto.WorkingPlanDTO;
import org.zadyraichuk.construction.dto.creation.ProjectCreateDTO;
import org.zadyraichuk.construction.dto.simple.CompanySimpleDTO;
import org.zadyraichuk.construction.dto.simple.ProjectSimpleDTO;
import org.zadyraichuk.construction.dto.simple.UserSimpleDTO;
import org.zadyraichuk.construction.entity.Project;
import org.zadyraichuk.construction.entity.Task;
import org.zadyraichuk.construction.service.BuildingCompanyService;
import org.zadyraichuk.construction.service.UserService;

import java.time.DayOfWeek;

@Mapper(componentModel = "spring")
public abstract class ProjectMapper extends GeneralMapper {

    protected UserService us;

    protected BuildingCompanyService bcs;

    protected WorkingPlanMapper wpm;

    @Autowired
    public void setUs(UserService us) {
        this.us = us;
    }

    @Autowired
    public void setBcs(BuildingCompanyService bcs) {
        this.bcs = bcs;
    }

    @Autowired
    public void setWpm(WorkingPlanMapper wpm) {
        this.wpm = wpm;
    }

    @Mapping(source = "projectName", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "buildingLocation", target = "buildingLocation")
    public abstract Project toEntity(ProjectCreateDTO project);

    @Mapping(source = "projectId", target = "id", qualifiedByName = "toObjectId")
    @Mapping(source = "projectName", target = "name")
    @Mapping(source = "generalContractor.userId", target = "contractorId", qualifiedByName = "toObjectId")
    @Mapping(source = "generalContractor.fullName", target = "contractorFullName")
    @Mapping(source = "company.companyId", target = "companyId", qualifiedByName = "toObjectId")
    @Mapping(source = "company.companyName", target = "companyName")
    @Mapping(source = "isShowed", target = "isShowed")
    public abstract Project toEntity(ProjectSimpleDTO project);

    @Mapping(source = "projectId", target = "id", qualifiedByName = "toObjectId")
    @Mapping(source = "projectName", target = "name")
    @Mapping(source = "generalContractor.userId", target = "contractorId", qualifiedByName = "toObjectId")
    @Mapping(source = "generalContractor.fullName", target = "contractorFullName")
    @Mapping(source = "company.companyId", target = "companyId", qualifiedByName = "toObjectId")
    @Mapping(source = "company.companyName", target = "companyName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "buildingLocation", target = "buildingLocation")
    @Mapping(source = "isVerified", target = "isVerified")
    @Mapping(source = "isShowed", target = "isShowed")
    @Mapping(source = "workingPlanDTO", target = "workingPlan", qualifiedByName = "toTasks")
    @Mapping(source = "workingPlanDTO", target = "holidays", qualifiedByName = "toHolidays")
    public abstract Project toEntity(ProjectDTO project);

    @Mapping(source = "id", target = "projectId", qualifiedByName = "toStringId")
    @Mapping(source = "name", target = "projectName")
    @Mapping(source = "contractorId", target = "generalContractor", qualifiedByName = "findContractor")
    @Mapping(source = "companyId", target = "company", qualifiedByName = "findCompany")
    @Mapping(source = "isShowed", target = "isShowed")
    public abstract ProjectSimpleDTO toConnectorDTO(Project project);

    @Mapping(source = "id", target = "projectId", qualifiedByName = "toStringId")
    @Mapping(source = "name", target = "projectName")
    @Mapping(source = "contractorId", target = "generalContractor", qualifiedByName = "findContractor")
    @Mapping(source = "companyId", target = "company", qualifiedByName = "findCompany")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "buildingLocation", target = "buildingLocation")
    @Mapping(source = "isVerified", target = "isVerified")
    @Mapping(source = "isShowed", target = "isShowed")
    @Mapping(target = "workingPlanDTO", expression = "java(wpm.toDTO(project))")
    public abstract ProjectDTO toDTO(Project project);

    @Named("toTasks")
    public Task[] toTasks(WorkingPlanDTO workingPlan) {
        return wpm.toEntityArray(workingPlan.getMainTasks());
    }

    @Named("toHolidays")
    public DayOfWeek[] toHolidays(WorkingPlanDTO workingPlan) {
        return wpm.toArray(workingPlan.getHolidays());
    }

    @Named("findContractor")
    public UserSimpleDTO findContractor(ObjectId id) {
        String userId = toStringId(id);
        return us.findById(userId).orElse(null);
    }

    @Named("findCompany")
    public CompanySimpleDTO findCompany(ObjectId id) {
        String companyId = toStringId(id);
        return bcs.findById(companyId).orElse(null);
    }

}
