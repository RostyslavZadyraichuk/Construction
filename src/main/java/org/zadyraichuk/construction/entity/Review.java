package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
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
    private String description;

    @Field
    @NotNull
    private LocalDate date;

    @Field
    @NotNull
    @Range(min = 1, max = 5)
    private Double rate;

    @Id
    @Indexed(unique = true)
    @NotNull
    private ObjectId projectId;

    @Field(name = "project_name")
    @NotNull
    private String projectName;

    @Field(name = "is_project_showed")
    @NotNull
    private Boolean isProjectShowed = false;

    @Field(name = "contractor_id")
    @NotNull
    private ObjectId generalContractorId;

    @Field(name = "contractor_full_name")
    @NotNull
    private String generalContractorFullName;

}
