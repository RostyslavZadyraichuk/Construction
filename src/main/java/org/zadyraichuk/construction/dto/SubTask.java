package org.zadyraichuk.construction.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SubTask extends Task {

//    private final SupTask supTask;
    private final List<SubTask> requiredTasks;
    private final List<SubTask> requiredFor;
    @Getter
    private final List<String> dependentIcogramObjects;

    public SubTask(String description,
                   LocalDate planningStart,
                   int planningDurationInDays) {
        super(description, planningStart, planningDurationInDays);
//        this.supTask = supTask;
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

    public void addRequiredTask(SubTask requiredTask) {
        this.requiredTasks.add(requiredTask);
    }

    public void addRequiredFor(SubTask requiredFor) {
        this.requiredFor.add(requiredFor);
    }

    public void addIcogramObject(String icogramObject) {
        this.dependentIcogramObjects.add(icogramObject);
    }

    public void removeRequiredTask(SubTask requiredTask) {
        this.requiredTasks.remove(requiredTask);
    }

    public void removeRequiredFor(SubTask requiredFor) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(requiredTasks, subTask.requiredTasks) &&
                Objects.equals(requiredFor, subTask.requiredFor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requiredTasks, requiredFor);
    }
}
