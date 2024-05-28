package org.zadyraichuk.construction.service.mapper;

import org.mapstruct.Mapper;
import org.zadyraichuk.construction.dto.FloorTaskDTO;
import org.zadyraichuk.construction.dto.SubTaskDTO;
import org.zadyraichuk.construction.dto.SupTaskDTO;
import org.zadyraichuk.construction.dto.TaskDTO;
import org.zadyraichuk.construction.entity.FloorTask;
import org.zadyraichuk.construction.entity.SubTask;
import org.zadyraichuk.construction.entity.SupTask;
import org.zadyraichuk.construction.entity.Task;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {

    public Task toEntity(TaskDTO task) {
        if (task.getClass().equals(FloorTaskDTO.class))
            return toEntity((FloorTaskDTO) task);
        else if (task.getClass().equals(SubTaskDTO.class))
            return toEntity((SubTaskDTO) task);
        else if (task.getClass().equals(SupTaskDTO.class))
            return toEntity((SupTaskDTO) task);
        return null;
    }

    public TaskDTO toDTO(Task task) {
        if (task.getClass().equals(FloorTask.class))
            return toDTO((FloorTask) task);
        else if (task.getClass().equals(SubTask.class))
            return toDTO((SubTask) task);
        else if (task.getClass().equals(SupTask.class))
            return toDTO((SupTask) task);
        return null;
    }
    
    private FloorTask toEntity(FloorTaskDTO task) {
        FloorTask entity = new FloorTask();
        entity.setId(task.getId());
        entity.setFloor(task.getFloor());
        entity.setDescription(task.getDescription());
        entity.setPlaningStart(task.getPlanningStart());
        entity.setPlaningDuration(task.getPlanningDurationInDays());
        entity.setRealStart(task.getRealStart());
        entity.setRealDuration(task.getRealDurationInDays());
        return entity;
    }

    private SubTask toEntity(SubTaskDTO task) {
        SubTask entity = new SubTask();
        entity.setId(task.getId());
        entity.setDescription(task.getDescription());
        entity.setPlaningStart(task.getPlanningStart());
        entity.setPlaningDuration(task.getPlanningDurationInDays());
        entity.setRealStart(task.getRealStart());
        entity.setRealDuration(task.getRealDurationInDays());
        return entity;
    }

    private SupTask toEntity(SupTaskDTO task) {
        SupTask entity = new SupTask();
        entity.setId(task.getId());
        entity.setDescription(task.getDescription());
        entity.setPlaningStart(task.getPlanningStart());
        entity.setPlaningDuration(task.getPlanningDurationInDays());
        entity.setRealStart(task.getRealStart());
        entity.setRealDuration(task.getRealDurationInDays());
        return entity;
    }

    private FloorTaskDTO toDTO(FloorTask task) {
        return new FloorTaskDTO(task.getId(), task.getPlaningStart(), task.getPlaningDuration(),
                task.getRealStart(), task.getRealDuration(), task.getFloor());
    }

    private SubTaskDTO toDTO(SubTask task) {
        return new SubTaskDTO(task.getId(), task.getPlaningStart(), task.getPlaningDuration(),
                task.getRealStart(), task.getRealDuration());
    }

    private SupTaskDTO toDTO(SupTask task) {
        return new SupTaskDTO(task.getId(), task.getPlaningStart(), task.getPlaningDuration(),
                task.getRealStart(), task.getRealDuration());
    }
}
