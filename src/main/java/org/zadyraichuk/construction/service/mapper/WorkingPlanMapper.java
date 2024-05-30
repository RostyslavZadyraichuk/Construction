package org.zadyraichuk.construction.service.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.zadyraichuk.construction.dto.SubTaskDTO;
import org.zadyraichuk.construction.dto.SupTaskDTO;
import org.zadyraichuk.construction.dto.TaskDTO;
import org.zadyraichuk.construction.dto.WorkingPlanDTO;
import org.zadyraichuk.construction.entity.Project;
import org.zadyraichuk.construction.entity.SubTask;
import org.zadyraichuk.construction.entity.SupTask;
import org.zadyraichuk.construction.entity.Task;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public abstract class WorkingPlanMapper {

    protected TaskMapper tm;

    @Autowired
    public void setTaskMapper(TaskMapper taskMapper) {
        this.tm = taskMapper;
    }

    //TODO rename
    public WorkingPlanDTO toDTO(Project entity) {
        List<TaskDTO> tasks = toDTOList(entity.getWorkingPlan());
        List<DayOfWeek> holidays = toList(entity.getHolidays());
        return new WorkingPlanDTO(tasks, holidays);
    }

    //TODO rename
    public Task[] toEntityArray(List<TaskDTO> tasks) {
        Task[] entities = new Task[tasks.size()];
        Map<Integer, Task> allTasks = getAllTasks(tasks);
        setDependencies(allTasks, tasks, entities);
        return entities;
    }

    //TODO rename
    public DayOfWeek[] toArray(List<DayOfWeek> days) {
        return days.toArray(new DayOfWeek[0]);
    }

    //TODO rename
    public List<DayOfWeek> toList(DayOfWeek[] days) {
        return new ArrayList<>(List.of(days));
    }

    //TODO rename
    public List<TaskDTO> toDTOList(Task[] tasks) {
        List<TaskDTO> dtos = new ArrayList<>();
        Map<Integer, TaskDTO> allTasks = getAllTasks(tasks);
        setDependencies(allTasks, tasks, dtos);
        return dtos;
    }

    private Map<Integer, Task> getAllTasks(List<TaskDTO> tasks) {
        Map<Integer, Task> tasksInOrder = new HashMap<>();
        addTasks(tasksInOrder, tasks);
        return tasksInOrder;
    }

    private void addTasks(Map<Integer, Task> tasks, List<TaskDTO> list) {
        for (TaskDTO task : list) {
            tasks.put(task.getId(), tm.toEntity(task));

            if (task.getClass().equals(SupTaskDTO.class)) {
                addTasks(tasks, ((SupTaskDTO) task).getSubTasks());
            }
        }
    }

    private void setDependencies(Map<Integer, Task> tasks, List<TaskDTO> src, Task[] dest) {
        for (int i = 0; i < src.size(); i++) {
            TaskDTO task = src.get(i);
            Task entity = tm.toEntity(task);
            entity.setParent(tasks.get(task.getParent().getId()));

            if (entity.getClass().equals(SupTask.class)) {
                List<TaskDTO> childrenDTO = ((SupTaskDTO) task).getSubTasks();
                Task[] children = new Task[childrenDTO.size()];
                setDependencies(tasks, childrenDTO, children);
                ((SupTask) entity).setSubTasks(children);
            } else {
                List<SubTaskDTO> requiredTasksDTO = ((SubTaskDTO) task).getRequiredTasks();
                List<SubTaskDTO> requiredForDTO = ((SubTaskDTO) task).getRequiredFor();
                Task[] requiredTasks = new Task[requiredTasksDTO.size()];
                Task[] requiredFor = new Task[requiredForDTO.size()];

                for (int j = 0; j < requiredTasksDTO.size(); j++) {
                    requiredTasks[j] = tasks.get(requiredTasksDTO.get(j).getId());
                }
                ((SubTask) entity).setRequiredTasks(requiredTasks);
                for (int j = 0; j < requiredForDTO.size(); j++) {
                    requiredFor[j] = tasks.get(requiredForDTO.get(j).getId());
                }
                ((SubTask) entity).setRequiredFor(requiredFor);
            }

            dest[i] = entity;
        }
    }

    private Map<Integer, TaskDTO> getAllTasks(Task[] tasks) {
        Map<Integer, TaskDTO> tasksInOrder = new HashMap<>();
        addTasks(tasksInOrder, tasks);
        return tasksInOrder;
    }

    private void addTasks(Map<Integer, TaskDTO> tasks, Task[] array) {
        for (Task task : array) {
            tasks.put(task.getId(), tm.toDTO(task));

            if (task.getClass().equals(SupTask.class)) {
                addTasks(tasks, ((SupTask) task).getSubTasks());
            }
        }
    }

    private void setDependencies(Map<Integer, TaskDTO> tasks, Task[] src, List<TaskDTO> dest) {
        for (Task task : src) {
            TaskDTO dto = tm.toDTO(task);
            dto.setParent((SupTaskDTO) tasks.get(task.getParent().getId()));

            if (dto.getClass().equals(SupTaskDTO.class)) {
                Task[] childrenEntity = ((SupTask) task).getSubTasks();
                ((SupTaskDTO) dto).getSubTasks().clear();
                setDependencies(tasks, childrenEntity, ((SupTaskDTO) dto).getSubTasks());
            } else {
                Task[] requiredTasksEntities = ((SubTask) task).getRequiredTasks();
                Task[] requiredForEntities = ((SubTask) task).getRequiredFor();

                for (Task requiredTasksEntity : requiredTasksEntities) {
                    SubTaskDTO subTask = (SubTaskDTO) tasks.get(requiredTasksEntity.getId());
                    ((SubTaskDTO) dto).addRequiredTask(subTask);
                }
                for (Task requiredForEntity : requiredForEntities) {
                    SubTaskDTO subTask = (SubTaskDTO) tasks.get(requiredForEntity.getId());
                    ((SubTaskDTO) dto).addRequiredFor(subTask);
                }
            }

            dest.add(dto);
        }
    }

}
