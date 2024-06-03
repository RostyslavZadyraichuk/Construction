package org.zadyraichuk.construction.dto.builder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.zadyraichuk.construction.dto.*;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BuildingBuilder {

    private static final String RESIDENTIAL_PATH = "src/main/resources/json/residential_template.json";

    public Optional<WorkingPlanDTO> useResidentialComplexTemplate(LocalDate start, int floors,
                                                        List<DayOfWeek> holidays) {
        JSONObject template = readTemplateFromJSON(RESIDENTIAL_PATH);

        try {
            JSONArray data = template.getJSONArray("data");
            JSONArray links = template.getJSONArray("links");
            Map<Integer, TaskDTO> tasks = new HashMap<>();

            setUpAllTasks(data, start, floors, tasks);
            setUpParents(data, tasks);
            setUpDependencies(links, tasks);
            addFloorTasks(floors, tasks);

            List<TaskDTO> mainTasks = tasks.values().stream()
                    .filter(t -> t.getParent() == null)
                    .collect(Collectors.toList());
            return Optional.of(new WorkingPlanDTO(mainTasks, holidays));
        } catch (NullPointerException e) {
            System.out.println("Errors when reading residential complex template");
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    private void setUpAllTasks(JSONArray tasks,
                               LocalDate start,
                               int floors,
                               Map<Integer, TaskDTO> allTasks)
            throws ClassCastException {

        for (int i = 0; i < tasks.length(); i++) {
            JSONObject taskJSON = tasks.getJSONObject(i);
            int id = taskJSON.getInt("id");
            TaskType type = TaskType.valueOf(taskJSON.getString("type"));
            int duration = 0;
            TaskDTO task;

            switch (type) {
                case SUB:
                    duration = taskJSON.getInt("duration");
                    task = new SubTaskDTO(id, start, duration);
                    break;
                case SUP:
                    task = new SupTaskDTO(id, start, duration);
                    break;
                case FLOOR:
                    duration = taskJSON.getInt("duration");
                    String floorString = taskJSON.getString("floor");
                    int floor = floorString.equals("BASE") ? 0 : floors + 1;
                    task = new FloorTaskDTO(id, floor, start, duration);
                    break;
                default:
                    throw new ClassCastException("Unsupported task type: " + type);
            }

            String description = taskJSON.getString("text");
            task.setDescription(description);
            allTasks.put(id, task);
        }
    }

    private void setUpParents(JSONArray tasks, Map<Integer, TaskDTO> allTasks) {
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject taskJSON = tasks.getJSONObject(i);
            int id = taskJSON.getInt("id");
            int parentId = taskJSON.getInt("parent");

            SupTaskDTO parent = parentId == 0 ? null : (SupTaskDTO) allTasks.get(parentId);
            allTasks.get(id).setParent(parent);
        }
    }

    private void setUpDependencies(JSONArray links, Map<Integer, TaskDTO> allTasks) {
        for (int i = 0; i < links.length(); i++) {
            JSONObject linkJSON = links.getJSONObject(i);
            int srcId = linkJSON.getInt("source");
            int trgId = linkJSON.getInt("target");

            ((SubTaskDTO) allTasks.get(srcId)).addRequiredFor((SubTaskDTO) allTasks.get(trgId));
        }
    }

    private void addFloorTasks(int floors, Map<Integer, TaskDTO> allTasks) {
        int currentId = allTasks.size();
        List<FloorTaskDTO> basicFloorTasks = allTasks.values().stream()
                .filter(v -> v instanceof FloorTaskDTO)
                .map(v -> (FloorTaskDTO) v)
                .sorted(Comparator.comparingInt((FloorTaskDTO o) -> o.getParent().getId())
                                .thenComparingInt(FloorTaskDTO::getFloor))
                .collect(Collectors.toList());

        Map<Integer, List<FloorTaskDTO>> tasksByStages = new HashMap<>();
        for (int i = 0; i < basicFloorTasks.size(); i += 2) {
            FloorTaskDTO baseTask = basicFloorTasks.get(i);
            FloorTaskDTO roofTask = basicFloorTasks.get(i + 1);

            List<FloorTaskDTO> floorsInAscOrder = new ArrayList<>();
            floorsInAscOrder.add(baseTask);
            for (int j = 0; j < floors; j++) {

                while (allTasks.containsKey(currentId)) {
                    currentId++;
                }

                FloorTaskDTO floorTask = new FloorTaskDTO(currentId++, j, baseTask.getRealStart(),
                        baseTask.getRealDurationInDays());
                floorTask.setParent(baseTask.getParent());
                floorTask.addRequiredTask(floorsInAscOrder.get(floorsInAscOrder.size() - 1));
                floorsInAscOrder.add(floorTask);
            }
            roofTask.addRequiredTask(floorsInAscOrder.get(floorsInAscOrder.size() - 1));
            floorsInAscOrder.add(roofTask);

            tasksByStages.put(tasksByStages.size() + 1, floorsInAscOrder);
        }

        setUpFloorTasksDetails(floors, tasksByStages);
    }

    private void setUpFloorTasksDetails(int floors,
                                        Map<Integer, List<FloorTaskDTO>> tasksByStages) {
        int floorsMiddle = floors / 2;

        //Set up second stage tasks starts when half of first is done
        List<FloorTaskDTO> firstStageTasks = tasksByStages.get(1);
        List<FloorTaskDTO> secondStageTasks = tasksByStages.get(2);
        secondStageTasks.get(0).getRequiredTasks().clear();
        secondStageTasks.get(0).addRequiredTask(firstStageTasks.get(floorsMiddle));
        for (int i = floorsMiddle; i < secondStageTasks.size(); i++) {
            secondStageTasks.get(i).addRequiredTask(firstStageTasks.get(i));
        }

        //Set up third stage tasks starts when half of second is done
        List<FloorTaskDTO> thirdStageTasks = tasksByStages.get(3);
        thirdStageTasks.get(0).getRequiredTasks().clear();
        thirdStageTasks.get(0).addRequiredTask(secondStageTasks.get(floorsMiddle));
        for (int i = floorsMiddle; i < thirdStageTasks.size(); i++) {
            thirdStageTasks.get(i).addRequiredTask(secondStageTasks.get(i));
        }
    }

    //TODO create reverse writer
    private JSONObject readTemplateFromJSON(String fileName) {
        File file = new File(fileName);

        try (FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader)) {
            StringBuffer stringBuffer = new StringBuffer();

            bufferedReader.lines().forEach(line -> {
                if (!line.isEmpty())
                    stringBuffer.append(line.trim());
            });

            return new JSONObject(stringBuffer.toString());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
        }

        return null;
    }

    private enum TaskType {
        SUB, SUP, FLOOR
    }
    
}
