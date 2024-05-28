package org.zadyraichuk.construction.dto;

import lombok.Getter;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Getter
public class WorkingPlanDTO implements Progressable {

    private final List<TaskDTO> mainTasks;
    private final List<DayOfWeek> holidays;

    public WorkingPlanDTO() {
        this.mainTasks = new ArrayList<>();
        this.holidays = new ArrayList<>();
    }

    public WorkingPlanDTO(List<TaskDTO> mainTasks, List<DayOfWeek> holidays) {
        this.mainTasks = mainTasks;
        this.holidays = holidays;
    }

    public void addMainTask(TaskDTO taskDTO) {
        this.mainTasks.add(taskDTO);
    }

    public void removeMainTask(TaskDTO taskDTO) {
        this.mainTasks.remove(taskDTO);
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
        for (TaskDTO subTaskDTO : this.mainTasks) {
            progressSum += subTaskDTO.calculateProgress();
        }
        return progressSum / this.mainTasks.size();
    }

    @Override
    public String getCurrentStage() {
        for (TaskDTO task : mainTasks) {
            if (task.isInProgress()) {
                return task.getCurrentStage();
            }
        }
        return "Finished project";
    }
}
