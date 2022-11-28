package tests;

import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import tasks.Epic;
import tasks.SubTask;
import tasks.Status;
import tasks.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    TaskManager taskManager;
    Epic epic;
    SubTask subTask1;
    SubTask subTask2;

    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = new InMemoryTaskManager();
    }

    @AfterEach
    void clear() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllEpics();
    }

    @Test
    void epicStatusWithoutSubTasks() {
        epic = new Epic(Type.EPIC, "эпик 1", "описание эпика 1", Status.NEW);
        taskManager.createEpic(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void epicStatusWithSubTasksStatusNew() {
        epic = new Epic(Type.EPIC, "эпик 1", "описание эпика 1", Status.NEW);
        taskManager.createEpic(epic);
        subTask1 = new SubTask(Type.SUBTASK, "2009", Status.NEW, LocalDateTime.of(2009, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "66", 1);
        taskManager.createSubTask(subTask1);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void epicStatusWithSubTasksStatusInProgress() {
        epic = new Epic(Type.EPIC, "эпик 1", "описание эпика 1", Status.NEW);
        epic.setId(1);
        subTask1 = new SubTask(Type.SUBTASK, "2009", Status.IN_PROGRESS, LocalDateTime.of(2009, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "66", 1);
        subTask1.setId(3);
        epic.addSubtaskToEpic(subTask1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void epicStatusWithSubTasksStatusDone() {
        epic = new Epic(Type.EPIC, "эпик 1", "описание эпика 1", Status.NEW);
        epic.setId(1);
        subTask1 = new SubTask(Type.SUBTASK, "2009", Status.DONE, LocalDateTime.of(2009, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "66", 1);
        subTask1.setId(3);
        epic.addSubtaskToEpic(subTask1);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void epicStatusWithSubTasksStatusNewOrDone() {
        epic = new Epic(Type.EPIC, "эпик 1", "описание эпика 1", Status.NEW);
        epic.setId(1);
        subTask1 = new SubTask(Type.SUBTASK, "2009", Status.NEW, LocalDateTime.of(2009, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "66", 1);
        subTask1.setId(2);
        subTask2 = new SubTask(Type.SUBTASK, "2009", Status.DONE, LocalDateTime.of(2009, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "66", 1);
        subTask1.setId(3);
        epic.addSubtaskToEpic(subTask1);
        epic.addSubtaskToEpic(subTask2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}