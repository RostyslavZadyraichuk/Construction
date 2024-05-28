package org.zadyraichuk.construction.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class SubTaskDTO extends TaskDTO {

    private final List<SubTaskDTO> requiredTasks;
    private final List<SubTaskDTO> requiredFor;
    private final List<String> dependentIcogramObjects;

    public SubTaskDTO(int id,
                      LocalDate planningStart,
                      int planningDurationInDays) {
        super(id, planningStart, planningDurationInDays);
        this.requiredTasks = new ArrayList<>();
        this.requiredFor = new ArrayList<>();
        this.dependentIcogramObjects = new ArrayList<>();
    }

    public SubTaskDTO(int id,
                      LocalDate planningStart,
                      int planningDurationInDays,
                      LocalDate realStart,
                      int realDurationInDays) {
        super(id, planningStart, planningDurationInDays, realStart, realDurationInDays);
        this.requiredTasks = new ArrayList<>();
        this.requiredFor = new ArrayList<>();
        this.dependentIcogramObjects = new ArrayList<>();
    }

    public void changeStartDate(LocalDate newStart) {
//        this.realStart = newStart;
    }

    public void changeDuration(int newDurationInDays) {
//
    }

    public void addRequiredTask(SubTaskDTO requiredTask) {
        this.requiredTasks.add(requiredTask);
    }

    public void addRequiredFor(SubTaskDTO requiredFor) {
        this.requiredFor.add(requiredFor);
    }

    public void addIcogramObject(String icogramObject) {
        this.dependentIcogramObjects.add(icogramObject);
    }

    public void removeRequiredTask(SubTaskDTO requiredTask) {
        this.requiredTasks.remove(requiredTask);
    }

    public void removeRequiredFor(SubTaskDTO requiredFor) {
        this.requiredFor.remove(requiredFor);
    }

    public void removeIcogramObject(String icogramObject) {
        this.dependentIcogramObjects.remove(icogramObject);
    }

    @Override
    public int calculateProgress() {
        LocalDate today = LocalDate.now();
        if (today.isBefore(realStart))
            return 0;
        if (today.isAfter(today.plusDays(realDurationInDays)))
            return 100;
        int daysLeft = Period.between(today, realStart).getDays();
        return daysLeft * 100 / realDurationInDays;
    }

    @Override
    public String getCurrentStage() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTaskDTO subTaskDTO = (SubTaskDTO) o;
        return Objects.equals(requiredTasks, subTaskDTO.requiredTasks) &&
                Objects.equals(requiredFor, subTaskDTO.requiredFor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requiredTasks, requiredFor);
    }

    @Override
    public SubTaskDTO clone() throws CloneNotSupportedException {
        SubTaskDTO clone = (SubTaskDTO) super.clone();
        clone.dependentIcogramObjects.clear();
        dependentIcogramObjects.forEach(clone::addIcogramObject);
        return clone;
    }
}
