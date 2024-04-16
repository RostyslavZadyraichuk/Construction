package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Getter
@Setter
public class SubTask extends Task {

    @Field(name = "required_tasks")
    private Task[] requiredTasks;

    @Field(name = "required_for")
    private Task[] requiredFor;

    @Field(name = "icogram_parts")
    private String icogramPartName;

    public SubTask(String description, LocalDate planingStart, Integer planingDuration) {
        super(description, planingStart, planingDuration);
    }
}
