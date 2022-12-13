package tests;

import network.HttpTaskManager;
import network.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class HttpTaskManagerTest {
    HttpTaskManager manager;
    KVServer server;
    URI uriKVServer;

    @BeforeEach
    void start() throws IOException, InterruptedException {
        server = new KVServer();
        server.start();
        uriKVServer = KVServer.getServerURL();
        manager = new HttpTaskManager(uriKVServer);
    }

    @AfterEach
    public void stopKVServer() {
        server.stop();
    }

    @Test
    void addTaskTest() {
        Task task1 = new Task(Type.TASK, "Тестовый таск 1", Status.NEW,
                LocalDateTime.of(2001, 1, 1, 1, 1, 1),
                Duration.ofMinutes(20), "Описание тестового таска");
        task1.setId(1);
        manager.addTask(task1);
        manager.loadManagerFromKVServer();
        Assertions.assertEquals(task1, manager.tasks.get(task1.getId()));
    }

    @Test
    void addEpicTest() {
        Epic epic1 = new Epic(Type.EPIC, "Тестовый эпик 1", "Описание тестового эпика 1",
                Status.NEW);
        epic1.setId(1);
        manager.addEpic(epic1);
        manager.loadManagerFromKVServer();
        Assertions.assertEquals(epic1, manager.epics.get(epic1.getId()));
    }

    @Test
    void addSubTaskTest() {
        Epic epic1 = new Epic(Type.EPIC, "Тестовый эпик 1", "Описание тестового эпика",
                Status.NEW);
        manager.addEpic(epic1);
        SubTask subtask1 = new SubTask(Type.SUBTASK, "Тестовый сабтаск 1", Status.DONE,
                LocalDateTime.of(2009, 1, 1, 1, 1, 1),
                Duration.ofMinutes(20), "Описание тестового сабтаска", 1);
        subtask1.setId(2);
        manager.addSubTask(subtask1);
        manager.loadManagerFromKVServer();
        Assertions.assertEquals(subtask1, manager.subTasks.get(subtask1.getId()));
    }

    @Test
    void updateTaskTest() {
        Task task1 = new Task(Type.TASK, "Тестовый таск 1", Status.NEW,
                LocalDateTime.of(2001, 1, 1, 1, 1, 1),
                Duration.ofMinutes(20), "Описание тестового таска");
        Task task1Upd = new Task(Type.TASK, "Тестовый таск 2", Status.IN_PROGRESS,
                LocalDateTime.of(2006, 1, 1, 1, 1, 1),
                Duration.ofMinutes(20), "Описание тестового таска");
        task1Upd.setId(1);
        manager.addTask(task1);
        manager.updateTask(1, task1Upd);
        manager.loadManagerFromKVServer();
        Assertions.assertEquals(task1Upd, manager.tasks.get(task1.getId()));
    }

    @Test
    void updateEpicTest() {
        Epic epic3 = new Epic(Type.EPIC, "Тестовый эпик 1", "Описание тестового эпика",
                Status.NEW);
        Epic epic3Upd = new Epic(Type.EPIC, "Тестовый эпик 2", "Описание тестового эпика",
                Status.NEW);
        epic3Upd.setId(1);
        manager.addEpic(epic3);
        manager.updateEpic(1, epic3Upd);
        manager.loadManagerFromKVServer();
        Assertions.assertEquals(epic3Upd, manager.epics.get(epic3.getId()));
    }

    @Test
    void updateSubTaskTest() {
        Epic epic3 = new Epic(Type.EPIC, "Тестовый эпик 1", "Описание тестового эпика",
                Status.NEW);
        manager.addEpic(epic3);
        SubTask subtask5 = new SubTask(Type.SUBTASK, "Тестовый сабтаск 1", Status.NEW,
                LocalDateTime.of(2009, 1, 1, 1, 1, 1),
                Duration.ofMinutes(20), "Описание тестового сабтаска", 1);
        SubTask subtask5Upd = new SubTask(Type.SUBTASK, "Тестовый сабтаск 2", Status.DONE,
                LocalDateTime.of(2010, 1, 1, 1, 1, 1),
                Duration.ofMinutes(20), "Описание тестового сабтаска 2", 1);
        subtask5Upd.setId(2);
        manager.addSubTask(subtask5);
        manager.updateSubTask(2, subtask5Upd);
        manager.loadManagerFromKVServer();
        Assertions.assertEquals(subtask5Upd, manager.subTasks.get(subtask5.getId()));
    }

    @Test
    void saveHistoryTest() {
        Task task1 = new Task(Type.TASK, "Тестовый таск 1", Status.NEW,
                LocalDateTime.of(2001, 1, 1, 1, 1, 1),
                Duration.ofMinutes(20), "Описание тестового таска");
        Task task2 = new Task(Type.TASK, "Тестовый таск 2", Status.NEW,
                LocalDateTime.of(2002, 1, 1, 1, 1, 1),
                Duration.ofMinutes(20), "Описание тестового таска");
        manager.addTask(task1);
        manager.addTask(task2);
        List<Task> history = manager.getTaskHistory();
        manager.loadManagerFromKVServer();
        List<Task> historyForEquals = manager.getTaskHistory();
        Assertions.assertEquals(history, historyForEquals);
    }
}