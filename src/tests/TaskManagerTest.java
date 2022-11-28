package tests;

import managers.TaskManager;
import tasks.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    public T taskManager;

    abstract T createTaskManager();

    @BeforeEach
    private void updateTaskManager() {
        taskManager = createTaskManager();
    }

    Task task1 = new Task(Type.TASK, "2001", Status.NEW, LocalDateTime.of(2001, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "11");
    Task task2 = new Task(Type.TASK, "2004", Status.NEW, LocalDateTime.of(2006, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "22");
    Task task3 = new Task(Type.TASK, "2005", Status.NEW, LocalDateTime.of(2003, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "22");
    Task task4 = new Task(Type.TASK, "2006", Status.NEW, LocalDateTime.of(2004, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "22");
    Epic epic3 = new Epic(Type.EPIC, "эпик включающий2009", "55", Status.NEW);
    Epic epic4 = new Epic(Type.EPIC, "эпик включающий2010", "55", Status.NEW);
    SubTask subtask4 = new SubTask(Type.SUBTASK, "2009", Status.DONE, LocalDateTime.of(2009, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "66", 1);
    SubTask subtask5 = new SubTask(Type.SUBTASK, "2010", Status.DONE, LocalDateTime.of(2010, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "66", 1);

    @AfterEach
    void clear() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllEpics();
    }

    @Test
    void addTaskTEST() {
        taskManager.createTask(task1);
        assertNotNull(taskManager.getTaskById(task1.getId()), "задача null");
        Assertions.assertFalse(taskManager.printAllTasks().isEmpty());
    }

    @Test
    void addEpicTEST() {
        taskManager.createEpic(epic3);
        assertNotNull(taskManager.getEpicById(epic3.getId()), "задача null");
    }

    @Test
    void addSubTaskTEST() {
        taskManager.createEpic(epic3);
        assertNotNull(taskManager.getEpicById(epic3.getId()), "задача null");
        taskManager.createSubTask(subtask4);
        assertNotNull(taskManager.getSubTaskById(subtask4.getId()), "задача null");
    }

    @Test
    void deleteAllTaskTEST() {
        taskManager.createTask(task1);
        taskManager.deleteAllTasks();
        Assertions.assertTrue(taskManager.printAllTasks().isEmpty());
    }

    @Test
    void deleteAllEpicTEST() {
        taskManager.createEpic(epic3);
        taskManager.deleteAllEpics();
        Assertions.assertTrue(taskManager.printAllEpics().isEmpty());
    }

    @Test
    void deleteAllSubTaskTEST() {
        taskManager.createEpic(epic3);
        taskManager.createSubTask(subtask4);
        taskManager.deleteAllSubTasks();
        Assertions.assertTrue(taskManager.printAllSubtasks().isEmpty());
    }

    @Test
    void deleteTaskByIdTEST() {
        taskManager.createTask(task1);
        Assertions.assertFalse(taskManager.printAllTasks().isEmpty());

        taskManager.deleteTaskById(1);
        Assertions.assertTrue(taskManager.printAllTasks().isEmpty());
    }

    @Test
    void deleteEpicByIdTEST() {
        taskManager.createEpic(epic3);
        Assertions.assertFalse(taskManager.printAllEpics().isEmpty());
        taskManager.deleteEpicById(1);
        Assertions.assertTrue(taskManager.printAllEpics().isEmpty());
    }

    @Test
    void deleteSubTaskByIdTEST() {
        taskManager.createEpic(epic3);
        taskManager.createSubTask(subtask4);
        Assertions.assertFalse(taskManager.printAllSubtasks().isEmpty(), "пусто");
        taskManager.deleteSubTaskById(subtask4.getId());
        Assertions.assertNotNull(taskManager.getSubTaskById(subtask4.getId()), "не пуста");
    }

    @Test
    void getTaskByIdTEST() {
        taskManager.createTask(task1);
        assertEquals(task1, taskManager.getTaskById(1));
        Assertions.assertNull(taskManager.getTaskById(500), "неверный идентификатор ломает");
    }

    @Test
    void getEpicByIdTEST() {
        taskManager.createEpic(epic3);
        assertEquals(epic3, taskManager.getEpicById(1));
        Assertions.assertNull(taskManager.getEpicById(500), "неверный идентификатор ломает");
    }

    @Test
    void getSubTaskByIdTEST() {
        taskManager.createEpic(epic3);
        taskManager.createTask(task1);
        assertEquals(task1, taskManager.getTaskById(2));
        Assertions.assertNull(taskManager.getSubTaskById(500), "неверный идентификатор ломает");
    }

    @Test
    void updateTaskTEST() {
        taskManager.createTask(task1);
        taskManager.updateTask(1, task2);
        assertEquals(task2, taskManager.getTaskById(1));
        taskManager.updateTask(500, task2);
        assertFalse(taskManager.printAllTasks().containsKey(500), "добавилась левая задача");
    }

    @Test
    void updateEpicTEST() {
        taskManager.createEpic(epic3);
        taskManager.updateEpic(1, epic4);
        taskManager.updateEpic(500, epic4);
        assertEquals(epic4, taskManager.getEpicById(1));
        assertFalse(taskManager.printAllEpics().containsKey(500), "добавилась левая задача");
    }

    @Test
    void updateSubTaskTEST() {
        taskManager.createEpic(epic3);
        taskManager.createSubTask(subtask4);
        taskManager.updateSubTask(2, subtask5);
        taskManager.updateSubTask(500, subtask5);
        assertEquals(subtask5, taskManager.getSubTaskById(2));
        assertFalse(taskManager.printAllSubtasks().containsKey(500), "добавилась левая задача");
    }

    @Test
    void viewAllTaskTEST() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        int i = 2;
        assertEquals(taskManager.printAllTasks().size(), i);
    }

    @Test
    void viewSubTaskOfEpicTEST() {
        taskManager.createEpic(epic3);
        taskManager.createSubTask(subtask4);
        taskManager.createSubTask(subtask5);
        int i = 2;
        assertEquals(taskManager.printSubTasksOfEpic(1).size(), i);
        assertNull(taskManager.printSubTasksOfEpic(500));
    }

    @Test
    void getTaskHistoryTEST() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        int i = 2;
        assertEquals(taskManager.getHistory().size(), i);
    }

    @Test
    void getPrioritizedTasksTEST() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        int i = 4;
        assertEquals(taskManager.getPrioritizedTasks().size(), i);
        assertEquals(taskManager.getPrioritizedTasks().first(), task1);
        assertEquals(taskManager.getPrioritizedTasks().last(), task2);
    }
}