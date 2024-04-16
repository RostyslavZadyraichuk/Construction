package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class FloorTask extends SubTask {

    @Field
    @NotNull
    @Range(min = 1, max = 30)
    private final Integer floor;

    public FloorTask(String description, LocalDate planingStart, Integer planingDuration, int floor) {
        super(description, planingStart, planingDuration);
        this.floor = floor;
    }
}
