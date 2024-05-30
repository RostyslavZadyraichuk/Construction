package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SupTask extends Task {

    @Field(name = "sub_tasks")
    @NotNull
    @NotEmpty
    private Task[] subTasks;

}
