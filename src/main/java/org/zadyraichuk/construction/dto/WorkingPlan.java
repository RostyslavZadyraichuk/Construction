package org.zadyraichuk.construction.dto;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class WorkingPlan implements Progressable {

    private final List<Task> mainTasks;
    private final List<DayOfWeek> holidays;

    public WorkingPlan() {
        this.mainTasks = new ArrayList<>();
        this.holidays = new ArrayList<>();
    }

    public void addMainTask(Task task) {
        this.mainTasks.add(task);
    }

    public void removeMainTask(Task task) {
        this.mainTasks.remove(task);
    }

    public void addHoliday(DayOfWeek dayOfWeek) {
        this.holidays.add(dayOfWeek);
    }

    public void removeHoliday(DayOfWeek dayOfWeek) {
        this.holidays.remove(dayOfWeek);
    }

    @Override
    public int calculateProgress() {
        int progressSum = 0;
        for (Task subTask : this.mainTasks) {
            progressSum += subTask.calculateProgress();
        }
        return progressSum / this.mainTasks.size();
    }
}
