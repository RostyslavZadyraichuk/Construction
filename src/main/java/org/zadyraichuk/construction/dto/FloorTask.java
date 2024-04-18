package org.zadyraichuk.construction.dto;

import lombok.Getter;

import java.time.LocalDate;

public class FloorTask extends SubTask {

    @Getter
    private final int floor;

    public FloorTask(int floor,
                     String description,
                     LocalDate planningStart,
                     int planningDurationInDays) {
        super(description, planningStart, planningDurationInDays);
        this.floor = floor;
    }

}
