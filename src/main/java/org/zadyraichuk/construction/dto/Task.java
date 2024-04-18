package org.zadyraichuk.construction.dto;

import lombok.Setter;

import java.time.LocalDate;

public abstract class Task implements Progressable {

    @Setter
    protected String description;
    protected final LocalDate planningStart;
    protected final int planningDurationInDays;
    protected LocalDate realStart;
    protected int realDurationInDays;

    protected Task(String description,
                   LocalDate planningStart,
                   int planningDurationInDays) {
        this.description = description;
        this.planningStart = planningStart;
        this.planningDurationInDays = planningDurationInDays;
        this.realStart = planningStart;
        this.realDurationInDays = planningDurationInDays;
    }

    public boolean isInProgress() {
        return calculateProgress() > 0 && calculateProgress() < 100;
    }

}
