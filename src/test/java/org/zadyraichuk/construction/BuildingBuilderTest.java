package org.zadyraichuk.construction;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.zadyraichuk.construction.dto.SubTaskDTO;
import org.zadyraichuk.construction.dto.SupTaskDTO;
import org.zadyraichuk.construction.dto.TaskDTO;
import org.zadyraichuk.construction.dto.WorkingPlanDTO;
import org.zadyraichuk.construction.dto.builder.BuildingBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BuildingBuilderTest {

    static BuildingBuilder bb;

    static WorkingPlanDTO customWPManyMainTasks;
    static WorkingPlanDTO customWPSingleMainTask;
    static WorkingPlanDTO residentialTemplate;

    @BeforeAll
    static void setUpBeforeAll() {
        bb = new BuildingBuilder();

        setUpCustomWPManyMainTasks();
        setUpCustomWPSingleMainTask();
        setUpResidentialTemplate();
    }

    @Test
    void customWPManyMainTasksTest() {
        assertNotNull(customWPManyMainTasks);
        List<TaskDTO> mainTasks = customWPManyMainTasks.getMainTasks();

        int mainTasksCountExpected = 3;
        int mainTasksCountActual = mainTasks.size();
        assertNotNull(mainTasks);
        assertFalse(mainTasks.isEmpty());
        assertEquals(mainTasksCountExpected, mainTasksCountActual);

        TaskDTO startTask = mainTasks.get(0);
        LocalDate startExpected = LocalDate.of(2024, 6, 3);
        LocalDate startPlanningActual = startTask.getPlanningStart();
        LocalDate startRealActual = startTask.getRealStart();
        assertNotSame(startPlanningActual, startRealActual);
        assertEquals(startExpected, startPlanningActual);
        assertEquals(startExpected, startRealActual);

        TaskDTO endTask = mainTasks.get(mainTasks.size() - 1);
        LocalDate endExpected = LocalDate.of(2024, 6, 16);
        LocalDate endPlanningActual = endTask.getPlanningStart().plusDays(endTask.getPlanningDurationInDays());
        LocalDate endRealActual = endTask.getRealStart().plusDays(endTask.getRealDurationInDays());
        assertNotSame(endPlanningActual, endRealActual);
        assertEquals(endExpected, endPlanningActual);
        assertEquals(endExpected, endRealActual);

        SubTaskDTO firstSubTask = (SubTaskDTO) mainTasks.get(0);
        List<SubTaskDTO> requiredTasks = firstSubTask.getRequiredTasks();
        List<SubTaskDTO> requiredFor = firstSubTask.getRequiredFor();
        SupTaskDTO parent = firstSubTask.getParent();
        assertNotNull(requiredTasks);
        assertNotNull(requiredFor);
        assertNull(parent);

        int expectedRequiredForCount = 2;
        assertTrue(requiredTasks.isEmpty());
        assertFalse(requiredFor.isEmpty());
        assertEquals(expectedRequiredForCount, requiredFor.size());

        System.out.println(customWPManyMainTasks);
    }

    @Test
    void customWPSingleMainTaskTest() {
        assertNotNull(customWPSingleMainTask);
        List<TaskDTO> mainTasks = customWPSingleMainTask.getMainTasks();

        int mainTasksCountExpected = 1;
        int mainTasksCountActual = mainTasks.size();
        assertNotNull(mainTasks);
        assertFalse(mainTasks.isEmpty());
        assertEquals(mainTasksCountExpected, mainTasksCountActual);

        TaskDTO startTask = mainTasks.get(0);
        LocalDate startExpected = LocalDate.of(2024, 6, 3);
        LocalDate startPlanningActual = startTask.getPlanningStart();
        LocalDate startRealActual = startTask.getRealStart();
        assertNotSame(startPlanningActual, startRealActual);
        assertEquals(startExpected, startPlanningActual);
        assertEquals(startExpected, startRealActual);

        LocalDate endExpected = LocalDate.of(2024, 6, 16);
        LocalDate endPlanningActual = startTask.getPlanningStart().plusDays(startTask.getPlanningDurationInDays());
        LocalDate endRealActual = startTask.getRealStart().plusDays(startTask.getRealDurationInDays());
        assertNotSame(endPlanningActual, endRealActual);
        assertEquals(endExpected, endPlanningActual);
        assertEquals(endExpected, endRealActual);

        SubTaskDTO firstSubTask = (SubTaskDTO) ((SupTaskDTO) mainTasks.get(0)).getSubTasks().get(0);
        List<SubTaskDTO> requiredTasks = firstSubTask.getRequiredTasks();
        List<SubTaskDTO> requiredFor = firstSubTask.getRequiredFor();
        SupTaskDTO parent = firstSubTask.getParent();
        assertNotNull(requiredTasks);
        assertNotNull(requiredFor);
        assertNotNull(parent);

        int expectedRequiredForCount = 2;
        assertTrue(requiredTasks.isEmpty());
        assertFalse(requiredFor.isEmpty());
        assertEquals(expectedRequiredForCount, requiredFor.size());

        System.out.println(customWPSingleMainTask);
    }

    @Test
    void residentialTemplateTest() {
        System.out.println(residentialTemplate);
    }

    private static void setUpCustomWPManyMainTasks() {
        LocalDate date = LocalDate.now();
        SubTaskDTO main1 = new SubTaskDTO(1, date, 4);
        SubTaskDTO main2 = new SubTaskDTO(2, date.plusDays(1), 4);
        SupTaskDTO main3 = new SupTaskDTO(3, date, 4);
        SubTaskDTO sub1 = new SubTaskDTO(5, date, 4);
        sub1.setParent(main3);
        sub1.addRequiredTask(main1);
        sub1.addRequiredTask(main2);
        SubTaskDTO sub2 = new SubTaskDTO(6, date, 4);
        sub2.setParent(main3);
        sub2.addRequiredTask(main1);
        sub2.addRequiredTask(sub1);
        List<TaskDTO> mainTasks = new ArrayList<>();
        mainTasks.add(main1);
        mainTasks.add(main2);
        mainTasks.add(main3);
        customWPManyMainTasks = new WorkingPlanDTO(mainTasks);
    }

    private static void setUpCustomWPSingleMainTask() {
        LocalDate date = LocalDate.now();
        SupTaskDTO main1 = new SupTaskDTO(1, date, 4);
        SubTaskDTO sub1 = new SubTaskDTO(2, date, 4);
        main1.addSubTask(sub1);
        SubTaskDTO sub2 = new SubTaskDTO(3, date.plusDays(1), 4);
        main1.addSubTask(sub2);
        SupTaskDTO sub3 = new SupTaskDTO(4, date, 4);
        main1.addSubTask(sub3);
        SubTaskDTO sub4 = new SubTaskDTO(5, date, 4);
        sub3.addSubTask(sub4);
        sub4.addRequiredTask(sub1);
        sub4.addRequiredTask(sub2);
        SubTaskDTO sub5 = new SubTaskDTO(6, date, 4);
        sub3.addSubTask(sub5);
        sub5.addRequiredTask(sub1);
        sub5.addRequiredTask(sub4);
        List<TaskDTO> mainTasks = new ArrayList<>();
        mainTasks.add(main1);
        customWPSingleMainTask = new WorkingPlanDTO(mainTasks);
    }

    private static void setUpResidentialTemplate() {
        Optional<WorkingPlanDTO> template =
                bb.useResidentialComplexTemplate(LocalDate.now(), 5, new ArrayList<>());
        residentialTemplate = template.orElse(null);
    }

}
