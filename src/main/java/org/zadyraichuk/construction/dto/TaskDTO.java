package org.zadyraichuk.construction.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public abstract class TaskDTO implements Progressable {

    protected int id;
    @Setter
    protected String description;
    protected SupTaskDTO parent;
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

    public LocalDate getFinishDate() {
        return realStart.plusDays(realDurationInDays);
    }

    public void setParent(SupTaskDTO parent) {
        this.parent = parent;
        parent.addSubTaskAvoidLoop(this);
        parent.updateDateAndDuration();
    }

    protected void setParentAvoidLoop(SupTaskDTO parent) {
        this.parent = parent;
    }
}
