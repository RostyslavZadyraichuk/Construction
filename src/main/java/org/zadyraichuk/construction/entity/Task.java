package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public abstract class Task {

    @Field
    @NotNull
    private Integer id;

    @Field
    @NotNull
    private String description;

    @Field(name = "planning_start")
    @NotNull
    private LocalDate planingStart;

    @Field(name = "planning_duration")
    @NotNull
    private Integer planingDuration;

    @Field(name = "real_start")
    @NotNull
    private LocalDate realStart;

    @Field(name = "real_duration")
    @NotNull
    private Integer realDuration;

    @Field
    private Task parent;

}
