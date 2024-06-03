package org.zadyraichuk.construction.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Getter
public class SupTaskDTO extends TaskDTO {

    //TODO change to Set
    private final List<TaskDTO> subTasks;
    private boolean isChildrenChanged;

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
        if (isAcceptableForDependency(subTaskDTO) && !subTasks.contains(subTaskDTO)) {
            this.subTasks.add(subTaskDTO);
            subTaskDTO.setParentAvoidLoop(this);
            this.setUpChildrenChanged();
            this.updateDateAndDuration();
        }
    }

    public void removeSubTask(TaskDTO subTaskDTO) {
        if (this.subTasks.remove(subTaskDTO)) {
            setUpChildrenChanged();
            updateDateAndDuration();
        }
    }

    public void updateDateAndDuration() {
        if (isChildrenChanged) {
            subTasks.sort(Comparator.comparing(t -> t.realStart));
            TaskDTO first = subTasks.get(0);
            TaskDTO last = subTasks.get(subTasks.size() - 1);

            realStart = first.getRealStart();
            realDurationInDays = Period.between(first.realStart, last.realStart).getDays();
            realDurationInDays += last.realDurationInDays;

            if (isCreationProcess) {
                planningStart = LocalDate.of(realStart.getYear(), realStart.getMonth(), realStart.getDayOfMonth());
                planningDurationInDays = realDurationInDays;
            }

            if (parent != null) {
                parent.setUpChildrenChanged();
                parent.updateDateAndDuration();
            }

            isChildrenChanged = false;
        }
    }

    public void setUpChildrenChanged() {
        isChildrenChanged = true;
    }

    public SubTaskDTO getMinimalTask() {
        Optional<TaskDTO> taskOpt = subTasks.stream()
                .min(Comparator.comparing(TaskDTO::getRealStart));
        if (taskOpt.isPresent()) {
            TaskDTO first = taskOpt.get();
            if (first instanceof SupTaskDTO)
                return ((SupTaskDTO) first).getMinimalTask();
            return (SubTaskDTO) first;
        }
        return null;
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
    protected void disableCreationMode() {
        super.disableCreationMode();
        subTasks.forEach(TaskDTO::disableCreationMode);
    }

    protected void addSubTaskAvoidLoop(TaskDTO subTaskDTO) {
        this.subTasks.add(subTaskDTO);
    }
}
