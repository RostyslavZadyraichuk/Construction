package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "review")
public class Review {

    @Field
    private final String description;

    @Field
    @NotNull
    private final LocalDate date;

    @Field
    @NotNull
    @Range(min = 1, max = 5)
    private final Double rate;

    @Id
    @Indexed(unique = true)
    @NotNull
    private final ObjectId projectId;

    @Field(name = "project_name")
    @NotNull
    private String projectName;

    @Field(name = "is_project_showed")
    @NotNull
    private Boolean isProjectShowed = false;

    @Field(name = "contractor_id")
    @NotNull
    private final ObjectId generalContractorId;

    @Field(name = "contractor_full_name")
    @NotNull
    private final String generalContractorFullName;


    @PersistenceConstructor
    public Review(String description,
                  LocalDate date,
                  Double rate,
                  ObjectId projectId,
                  ObjectId generalContractorId,
                  String generalContractorFullName) {
        this.description = description;
        this.date = date;
        this.rate = rate;
        this.projectId = projectId;
        this.generalContractorId = generalContractorId;
        this.generalContractorFullName = generalContractorFullName;
    }

}
