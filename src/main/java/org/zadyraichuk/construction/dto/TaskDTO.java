package org.zadyraichuk.construction.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

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
    protected boolean isCreationProcess;

    protected TaskDTO(int id,
                      LocalDate planningStart,
                      int planningDurationInDays) {
        this.id = id;
        this.planningStart = planningStart;
        this.planningDurationInDays = planningDurationInDays;
        this.realStart = LocalDate.of(planningStart.getYear(), planningStart.getMonth(), planningStart.getDayOfMonth());
        this.realDurationInDays = planningDurationInDays;
        this.isCreationProcess = true;
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
        this.isCreationProcess = false;
    }

    public boolean isInProgress() {
        return calculateProgress() > 0 && calculateProgress() < 100;
    }

    public LocalDate getFinishDate() {
        return realStart.plusDays(realDurationInDays);
    }

    public void setParent(SupTaskDTO parent) {
        if (parent != this) {
            if (parent != null) {
                parent.addSubTaskAvoidLoop(this);
                parent.setUpChildrenChanged();
                parent.updateDateAndDuration();
            }

            this.parent = parent;
        }
    }

    protected void setParentAvoidLoop(SupTaskDTO parent) {
        this.parent = parent;
    }

    protected boolean isAcceptableForDependency(TaskDTO task) {
        return task != null && !task.equals(this);
    }

    protected void disableCreationMode() {
        this.isCreationProcess = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskDTO)) return false;
        TaskDTO taskDTO = (TaskDTO) o;
        return id == taskDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
