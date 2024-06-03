package org.zadyraichuk.construction.dto;

import lombok.Getter;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Getter
public class WorkingPlanDTO implements Progressable {

    private final List<TaskDTO> mainTasks;
    private final List<DayOfWeek> holidays;

//    public WorkingPlanDTO() {
//        this.mainTasks = new ArrayList<>();
//        this.holidays = new ArrayList<>();
//    }

    public WorkingPlanDTO(List<TaskDTO> mainTasks) {
        this.mainTasks = mainTasks;
        this.mainTasks.forEach(TaskDTO::disableCreationMode);
        this.holidays = new ArrayList<>();
    }

    public WorkingPlanDTO(List<TaskDTO> mainTasks, List<DayOfWeek> holidays) {
        this.mainTasks = mainTasks;
        this.mainTasks.forEach(TaskDTO::disableCreationMode);
        this.holidays = holidays;
    }

    //TODO plug in these methods and default constructor
//    public void addMainTask(TaskDTO taskDTO) {
//        this.mainTasks.add(taskDTO);
//    }
//
//    public void removeMainTask(TaskDTO taskDTO) {
//        this.mainTasks.remove(taskDTO);
//    }

    public void addHoliday(DayOfWeek dayOfWeek) {
        this.holidays.add(dayOfWeek);
    }

    public void removeHoliday(DayOfWeek dayOfWeek) {
        this.holidays.remove(dayOfWeek);
    }

    public void setUpTasksWithHolidays() {
        Optional<TaskDTO> taskOpt = mainTasks.stream()
                .min(Comparator.comparing(TaskDTO::getRealStart));
        if (taskOpt.isPresent()) {
            TaskDTO first = taskOpt.get();
            if (first instanceof SupTaskDTO)
                ((SupTaskDTO) first).getMinimalTask().setUpHolidays(holidays);
            else
                ((SubTaskDTO) first).setUpHolidays(holidays);
        }
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
