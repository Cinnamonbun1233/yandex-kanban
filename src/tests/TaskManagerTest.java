package tests;

import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    public T taskManager;
    Task task1 = new Task(Type.TASK, "Тестовый таск 1", Status.NEW, LocalDateTime.of(2001, 1,
            1, 1, 1, 1), Duration.ofMinutes(20), "Описание тестового таска 1");
    Task task2 = new Task(Type.TASK, "Тестовый таск 2", Status.NEW, LocalDateTime.of(2006, 1,
            1, 1, 1, 1), Duration.ofMinutes(20), "Описание тестового таска 2");
    Task task3 = new Task(Type.TASK, "Тестовый таск 3", Status.NEW, LocalDateTime.of(2003, 1,
            1, 1, 1, 1), Duration.ofMinutes(20), "Описание тестового таска 3");
    Task task4 = new Task(Type.TASK, "Тестовый таск 4", Status.NEW, LocalDateTime.of(2004, 1,
            1, 1, 1, 1), Duration.ofMinutes(20), "Описание тестового таска 4");
    Epic epic3 = new Epic(Type.EPIC, "Тестовый эпик 1", "Описание тестового эпика 1", Status.NEW);
    Epic epic4 = new Epic(Type.EPIC, "Тестовый эпик 2", "Описание тестового эпика 2", Status.NEW);
    SubTask subtask4 = new SubTask(Type.SUBTASK, "Тестовый сабтаск 1", Status.DONE, LocalDateTime.of(2009,
            1, 1, 1, 1, 1), Duration.ofMinutes(20),
            "Описание тестового сабтаска 1", 1);
    SubTask subtask5 = new SubTask(Type.SUBTASK, "Тестовый сабтаск 2", Status.DONE, LocalDateTime.of(2010,
            1, 1, 1, 1, 1), Duration.ofMinutes(20),
            "Описание тестового сабтаска 2", 1);

    abstract T createTaskManager();

    @BeforeEach
    public void updateTaskManager() {
        taskManager = createTaskManager();
    }

    @AfterEach
    void clear() {
        taskManager.deleteAllTask();
        taskManager.deleteAllSubTask();
        taskManager.deleteAllEpic();
    }

    @Test
    void addTaskTest() {
        taskManager.addTask(task1);
        assertNotNull(taskManager.getTaskById(task1.getId()), "задача null");
        Assertions.assertFalse(taskManager.viewAllTask().isEmpty());
    }

    @Test
    void addEpicTest() {
        taskManager.addEpic(epic3);
        assertNotNull(taskManager.getEpicById(epic3.getId()), "задача null");
    }

    @Test
    void addSubTaskTest() {
        taskManager.addEpic(epic3);
        assertNotNull(taskManager.getEpicById(epic3.getId()), "задача null");
        taskManager.addSubTask(subtask4);
        assertNotNull(taskManager.getSubTaskById(subtask4.getId()), "задача null");
    }

    @Test
    void deleteAllTaskTest() {
        taskManager.addTask(task1);
        taskManager.deleteAllTask();
        Assertions.assertTrue(taskManager.viewAllTask().isEmpty());
    }

    @Test
    void deleteAllEpicTest() {
        taskManager.addEpic(epic3);
        taskManager.deleteAllEpic();
        Assertions.assertTrue(taskManager.viewAllEpic().isEmpty());
    }

    @Test
    void deleteAllSubTaskTest() {
        taskManager.addEpic(epic3);
        taskManager.addSubTask(subtask4);
        taskManager.deleteAllSubTask();
        Assertions.assertTrue(taskManager.viewAllSubtask().isEmpty());
    }

    @Test
    void deleteTaskByIdTest() {
        taskManager.addTask(task1);
        Assertions.assertFalse(taskManager.viewAllTask().isEmpty());
        taskManager.deleteTaskById(1);
        Assertions.assertTrue(taskManager.viewAllTask().isEmpty());
    }

    @Test
    void deleteEpicByIdTest() {
        taskManager.addEpic(epic3);
        Assertions.assertFalse(taskManager.viewAllEpic().isEmpty());
        taskManager.deleteEpicById(1);
        Assertions.assertTrue(taskManager.viewAllEpic().isEmpty());
    }

    @Test
    void deleteSubTaskByIdTest() {
        taskManager.addEpic(epic3);
        taskManager.addSubTask(subtask4);
        Assertions.assertFalse(taskManager.viewAllSubtask().isEmpty(), "пусто");
        taskManager.deleteSubTaskById(subtask4.getId());
        Assertions.assertNotNull(taskManager.getSubTaskById(subtask4.getId()), "не пусто");
    }

    @Test
    void getTaskByIdTest() {
        taskManager.addTask(task1);
        assertEquals(task1, taskManager.getTaskById(1));
        Assertions.assertNull(taskManager.getTaskById(500), "неверный идентификатор");
    }

    @Test
    void getEpicByIdTest() {
        taskManager.addEpic(epic3);
        assertEquals(epic3, taskManager.getEpicById(1));
        Assertions.assertNull(taskManager.getEpicById(500), "неверный идентификатор");
    }

    @Test
    void getSubTaskByIdTest() {
        taskManager.addEpic(epic3);
        taskManager.addTask(task1);
        assertEquals(task1, taskManager.getTaskById(2));
        Assertions.assertNull(taskManager.getSubTaskById(500), "неверный идентификатор");
    }

    @Test
    void updateTaskTest() {
        taskManager.addTask(task1);
        taskManager.updateTask(1, task2);
        assertEquals(task2, taskManager.getTaskById(1));
        taskManager.updateTask(500, task2);
        assertFalse(taskManager.viewAllTask().containsKey(500), "добавилась левая задача");
    }

    @Test
    void updateEpicTest() {
        taskManager.addEpic(epic3);
        taskManager.updateEpic(1, epic4);
        taskManager.updateEpic(500, epic4);
        assertEquals(epic4, taskManager.getEpicById(1));
        assertFalse(taskManager.viewAllEpic().containsKey(500), "добавилась левая задача");
    }

    @Test
    void updateSubTaskTest() {
        taskManager.addEpic(epic3);
        taskManager.addSubTask(subtask4);
        taskManager.updateSubTask(2, subtask5);
        taskManager.updateSubTask(500, subtask5);
        assertEquals(subtask5, taskManager.getSubTaskById(2));
        assertFalse(taskManager.viewAllSubtask().containsKey(500), "добавилась левая задача");
    }

    @Test
    void viewAllTaskTest() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        int i = 2;
        assertEquals(taskManager.viewAllTask().size(), i);
    }

    @Test
    void viewSubTaskOfEpicTest() {
        taskManager.addEpic(epic3);
        taskManager.addSubTask(subtask4);
        taskManager.addSubTask(subtask5);
        int i = 2;
        assertEquals(taskManager.viewSubTaskOfEpic(1).size(), i);
        assertNull(taskManager.viewSubTaskOfEpic(500));
    }

    @Test
    void getTaskHistoryTest() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        int i = 2;
        assertEquals(taskManager.getTaskHistory().size(), i);
    }

    @Test
    void getPrioritizedTasksTest() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        int i = 4;
        assertEquals(taskManager.getPrioritizedTasks().size(), i);
        assertEquals(taskManager.getPrioritizedTasks().first(), task1);
        assertEquals(taskManager.getPrioritizedTasks().last(), task2);
    }
}