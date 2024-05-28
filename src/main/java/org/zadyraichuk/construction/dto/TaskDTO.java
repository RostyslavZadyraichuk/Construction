package org.zadyraichuk.construction.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public abstract class TaskDTO implements Progressable, Cloneable {

    protected int id;
    protected String description;
    protected TaskDTO parent;
    protected LocalDate planningStart;
    protected int planningDurationInDays;
    protected LocalDate realStart;
    protected int realDurationInDays;

    protected TaskDTO(int id,
                      LocalDate planningStart,
                      int planningDurationInDays) {
        this.id = id;
        this.planningStart = planningStart;
        this.planningDurationInDays = planningDurationInDays;
        this.realStart = planningStart;
        this.realDurationInDays = planningDurationInDays;
    }

    public TaskDTO(int id,
                   LocalDate planningStart,
                   int planningDurationInDays,
                   LocalDate realStart,
                   int realDurationInDays) {
        this.id = id;
        this.planningStart = planningStart;
        this.planningDurationInDays = planningDurationInDays;
        this.realStart = realStart;
        this.realDurationInDays = realDurationInDays;
    }

    public boolean isInProgress() {
        return calculateProgress() > 0 && calculateProgress() < 100;
    }

    @Override
    public TaskDTO clone() throws CloneNotSupportedException {
        TaskDTO clone = (TaskDTO) super.clone();
        clone.setParent(null);
        return clone;
    }
}
