package org.zadyraichuk.construction.dto;

import lombok.Getter;

import java.time.DayOfWeek;
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

    public void setUpHolidays(List<DayOfWeek> holidays) {
        updateSelf(holidays);
    }

    public void moveStartDate(LocalDate newDate) {
        updateSelf(newDate);
    }

    public void moveStartDate(LocalDate newDate, List<DayOfWeek> holidays) {
        updateSelf(newDate, holidays);
    }

    //TODO test set up newDate after realDate
    public void changeStartDate(LocalDate newDate) {
        int increment = Period.between(realStart, newDate).getDays();
        updateSelf(newDate, realDurationInDays + increment);
    }

    public void changeStartDate(LocalDate newDate, List<DayOfWeek> holidays) {
        int increment = Period.between(realStart, newDate).getDays();
        updateSelf(newDate, realDurationInDays + increment, holidays);
    }

    public void changeDuration(int newDurationInDays) {
        updateSelf(newDurationInDays);
    }

    public void changeDuration(int newDurationInDays, List<DayOfWeek> holidays) {
        updateSelf(newDurationInDays, holidays);
    }

    //TODO think up and realise algorithm for clever chain updating
    // only linked tasks and only after that -> all parents
    //current realisation -> each task in parent call update next tasks and plans call update parent
    //after all tasks done, but when first task in reversed order call update parent it switch
    //trigger isChildrenChanged in parent to false and all next tasks just skip that method

    public void addRequiredTask(SubTaskDTO requiredTask) {
        this.requiredTasks.add(requiredTask);
        requiredTask.addRequiredForAvoidLoop(this);
        updateSelf();
    }

    public void addRequiredFor(SubTaskDTO requiredFor) {
        this.requiredFor.add(requiredFor);
        requiredFor.addRequiredTaskAvoidLoop(this);
        requiredFor.updateSelf();
    }

    public void addIcogramObject(String icogramObject) {
        this.dependentIcogramObjects.add(icogramObject);
    }

    public void removeRequiredTask(SubTaskDTO requiredTask) {
        this.requiredTasks.remove(requiredTask);
        requiredTask.removeRequiredFor(this);
        updateSelf();
    }

    public void removeRequiredFor(SubTaskDTO requiredFor) {
        this.requiredFor.remove(requiredFor);
        requiredFor.removeRequiredTask(this);
        requiredFor.updateSelf();
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

    protected void addRequiredTaskAvoidLoop(SubTaskDTO requiredTask) {
        this.requiredTasks.add(requiredTask);
    }

    protected void addRequiredForAvoidLoop(SubTaskDTO requiredFor) {
        this.requiredFor.add(requiredFor);
    }

    private LocalDate fitDateByHolidays(LocalDate date, List<DayOfWeek> holidays) {
        if (holidays == null)
            return date;

        while (holidays.contains(date.getDayOfWeek())) {
            date = date.plusDays(1);
        }
        return date;
    }

    private int fitDurationByHolidays(int duration, List<DayOfWeek> holidays) {
        if (holidays == null)
            return duration;

        while (holidays.contains(realStart.plusDays(duration).getDayOfWeek())) {
            duration++;
        }
        return duration;
    }

    private void updateSelf() {
        updateSelf(realStart, realDurationInDays, null);
    }

    private void updateSelf(List<DayOfWeek> holidays) {
        updateSelf(realStart, realDurationInDays, holidays);
    }

    private void updateSelf(LocalDate newDate) {
        updateSelf(newDate, realDurationInDays, null);
    }

    private void updateSelf(LocalDate newDate, List<DayOfWeek> holidays) {
        updateSelf(newDate, realDurationInDays, holidays);
    }

    private void updateSelf(int newDurationInDays) {
        updateSelf(realStart, newDurationInDays, null);
    }

    private void updateSelf(int newDurationInDays, List<DayOfWeek> holidays) {
        updateSelf(realStart, newDurationInDays, holidays);
    }

    private void updateSelf(LocalDate newDate, int newDurationInDays) {
        updateSelf(newDate, newDurationInDays, null);
    }

    private void updateSelf(LocalDate newDate, int newDurationInDays, List<DayOfWeek> holidays) {
        newDate = fitDateByHolidays(newDate, holidays);
        if (!newDate.isEqual(realStart) && canStartBeChanged(newDate)) {
            realStart = newDate;
        }

        newDurationInDays = fitDurationByHolidays(newDurationInDays, holidays);
        if (newDurationInDays != realDurationInDays) {
            realDurationInDays = newDurationInDays;
        }

        updateNextTasks(holidays);
        parent.setUpChildrenChanged();
        parent.updateDateAndDuration();

    }

    private boolean canStartBeChanged(LocalDate date) {
        LocalDate minimalStart = getMinimalStartDate();
        return minimalStart.isBefore(date);
    }

    private LocalDate getMinimalStartDate() {
        LocalDate minimal = null;
        for (SubTaskDTO task : requiredTasks) {
            if (minimal == null) {
                minimal = task.getFinishDate();
            }

            if (task.getFinishDate().isAfter(minimal)) {
                minimal = task.getFinishDate();
            }
        }
        return minimal;
    }

    private void updateNextTasks(List<DayOfWeek> holidays) {
        if (holidays == null) {
            for (SubTaskDTO task : requiredFor) {
                task.moveStartDate(getFinishDate());
            }
        } else {
            for (SubTaskDTO task : requiredFor) {
                task.moveStartDate(getFinishDate(), holidays);
            }
        }
    }

}
