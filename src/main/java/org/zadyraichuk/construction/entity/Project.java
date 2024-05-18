package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;

@Getter
@Setter
@Document(collection = "project")
public class Project {

    @Id
    @Indexed(unique = true)
    private final ObjectId id;

    @Field
    @NotNull
    private String name;

    @Field
    private String description;

    @Field(name = "location")
    @NotNull
    private final String buildingLocation;

    @Field(name = "working_plan")
    @NotNull
    @NotEmpty
    private Task[] workingPlan;

    @Field(name = "is_verified")
    @NotNull
    private Boolean isVerified = false;

    @Field(name = "company_id")
    @NotNull
    private ObjectId companyId;

    @Field(name = "company_name")
    @NotNull
    private String companyName;

    @Field(name = "contractor_id")
    private ObjectId contractorId;

    @Field(name = "contractor_full_name")
    private String contractorFullName;

    @Field(name = "is_showed")
    @NotNull
    private Boolean isShowed = false;

    @Field
    private DayOfWeek[] holidays;

    @Field(name = "changes_history")
    @NotNull
    @NotEmpty
    private String[] actionsHistory;


    public Project() {
        this.id = null;
        this.buildingLocation = null;
    }

    public Project(String buildingLocation) {
        this.id = null;
        this.buildingLocation = buildingLocation;
    }

    @PersistenceConstructor
    public Project(ObjectId id,
                   String name,
                   String description,
                   String buildingLocation,
                   @NotEmpty Task[] workingPlan,
                   Boolean isVerified,
                   ObjectId companyId,
                   String companyName,
                   ObjectId contractorId,
                   String contractorFullName,
                   Boolean isShowed,
                   DayOfWeek[] holidays,
                   @NotEmpty String[] actionsHistory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.buildingLocation = buildingLocation;
        this.workingPlan = workingPlan;
        this.isVerified = isVerified;
        this.companyId = companyId;
        this.companyName = companyName;
        this.contractorId = contractorId;
        this.contractorFullName = contractorFullName;
        this.isShowed = isShowed;
        this.holidays = holidays;
        this.actionsHistory = actionsHistory;
    }

}
