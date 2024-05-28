package org.zadyraichuk.construction.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FloorTaskDTO extends SubTaskDTO {

    private final int floor;

    public FloorTaskDTO(int id,
                        int floor,
                        LocalDate planningStart,
                        int planningDurationInDays) {
        super(id, planningStart, planningDurationInDays);
        this.floor = floor;
    }

    public FloorTaskDTO(int id,
                        LocalDate planningStart,
                        int planningDurationInDays,
                        LocalDate realStart,
                        int realDurationInDays,
                        int floor) {
        super(id, planningStart, planningDurationInDays, realStart, realDurationInDays);
        this.floor = floor;
    }

    @Override
    public FloorTaskDTO clone() throws CloneNotSupportedException {
        return (FloorTaskDTO) super.clone();
    }
}
