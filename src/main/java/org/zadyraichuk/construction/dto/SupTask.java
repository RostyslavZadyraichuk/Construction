package org.zadyraichuk.construction.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SupTask extends Task {

    private final List<Task> subTasks;

    public SupTask(String description,
                   LocalDate planningStart,
                   int planningDurationInDays) {
        super(description, planningStart, planningDurationInDays);
        this.subTasks = new ArrayList<>();
    }

    public void addSubTask(Task subTask) {
        this.subTasks.add(subTask);
    }

    public void removeSubTask(Task subTask) {
        this.subTasks.remove(subTask);
    }

    @Override
    public int calculateProgress() {
        int progressSum = 0;
        for (Task subTask : this.subTasks) {
            progressSum += subTask.calculateProgress();
        }
        return progressSum / this.subTasks.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupTask supTask = (SupTask) o;
        return Objects.equals(subTasks, supTask.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(subTasks);
    }
}
