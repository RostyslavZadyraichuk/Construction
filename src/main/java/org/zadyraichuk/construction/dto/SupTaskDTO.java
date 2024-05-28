package org.zadyraichuk.construction.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class SupTaskDTO extends TaskDTO {

    private final List<TaskDTO> subTasks;

    public SupTaskDTO(int id,
                      LocalDate planningStart,
                      int planningDurationInDays) {
        super(id, planningStart, planningDurationInDays);
        this.subTasks = new ArrayList<>();
    }

    public SupTaskDTO(int id,
                      LocalDate planningStart,
                      int planningDurationInDays,
                      LocalDate realStart,
                      int realDurationInDays) {
        super(id, planningStart, planningDurationInDays, realStart, realDurationInDays);
        this.subTasks = new ArrayList<>();
    }

    public void addSubTask(TaskDTO subTaskDTO) {
        this.subTasks.add(subTaskDTO);
    }

    public void removeSubTask(TaskDTO subTaskDTO) {
        this.subTasks.remove(subTaskDTO);
    }

    @Override
    public int calculateProgress() {
        int progressSum = 0;
        for (TaskDTO subTaskDTO : this.subTasks) {
            progressSum += subTaskDTO.calculateProgress();
        }
        return progressSum / this.subTasks.size();
    }

    @Override
    public String getCurrentStage() {
        for (TaskDTO subTask : subTasks) {
            if (subTask.isInProgress()) {
                return subTask.getCurrentStage();
            }
        }
        return "Done";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupTaskDTO supTaskDTO = (SupTaskDTO) o;
        return Objects.equals(subTasks, supTaskDTO.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(subTasks);
    }
}
