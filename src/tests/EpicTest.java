package tests;

import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Type;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        taskManager.deleteAllTask();
        taskManager.deleteAllSubTask();
        taskManager.deleteAllEpic();
    }

    @Test
    void epicStatusWithoutSubTasks() {
        epic = new Epic(Type.EPIC, "Тестовый эпик 1", "Описание тестового эпика 1", Status.NEW);
        taskManager.addEpic(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void epicStatusWithSubTasksStatusNew() {
        epic = new Epic(Type.EPIC, "Тестовый эпик 1", "Описание тестового эпика 1", Status.NEW);
        taskManager.addEpic(epic);
        subTask1 = new SubTask(Type.SUBTASK, "Тестовый сабтаск 1", Status.NEW, LocalDateTime.of(2009, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "Описание тестового сабтаска 1", 1);
        taskManager.addSubTask(subTask1);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void epicStatusWithSubTasksStatusInProgress() {
        epic = new Epic(Type.EPIC, "Тестовый эпик 1", "Описание тестового эпика 1", Status.NEW);
        epic.setId(1);
        subTask1 = new SubTask(Type.SUBTASK, "Тестовый сабтаск 1", Status.IN_PROGRESS, LocalDateTime.of(2009, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "Описание тестового сабтаска 1", 1);
        subTask1.setId(3);
        epic.addNewSubtaskInEpic(subTask1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void epicStatusWithSubTasksStatusDone() {
        epic = new Epic(Type.EPIC, "Тестовый эпик 1", "Описание тестового эпика 1", Status.NEW);
        epic.setId(1);
        subTask1 = new SubTask(Type.SUBTASK, "Тестовый сабтаск 1", Status.DONE, LocalDateTime.of(2009, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "Описание тестового сабтаска 1", 1);
        subTask1.setId(3);
        epic.addNewSubtaskInEpic(subTask1);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void epicStatusWithSubTasksStatusNewOrDone() {
        epic = new Epic(Type.EPIC, "Тестовый эпик 1", "Описание тестового эпика 1", Status.NEW);
        epic.setId(1);
        subTask1 = new SubTask(Type.SUBTASK, "Тестовый сабтаск 1", Status.NEW, LocalDateTime.of(2009, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "Описание тестового сабтаска 1", 1);
        subTask1.setId(2);
        subTask2 = new SubTask(Type.SUBTASK, "Тестовый сабтаск 1", Status.DONE, LocalDateTime.of(2009, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "Описание тестового сабтаска 1", 1);
        subTask1.setId(3);
        epic.addNewSubtaskInEpic(subTask1);
        epic.addNewSubtaskInEpic(subTask2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}