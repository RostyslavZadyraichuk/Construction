package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FloorTask extends SubTask {

    @Field
    @NotNull
    @Range(min = 1, max = 30)
    private Integer floor;

}
