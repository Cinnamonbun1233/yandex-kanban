package tests;

import managers.HistoryManager;
import managers.TaskManager;
import tasks.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static managers.Managers.getDefault;
import static managers.Managers.getDefaultHistory;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    TaskManager manager = getDefault();
    HistoryManager historyManager = getDefaultHistory();
    Task task1 = new Task(Type.TASK, "2001", Status.NEW, LocalDateTime.of(2001, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "11");
    Task task2 = new Task(Type.TASK, "2004", Status.NEW, LocalDateTime.of(2006, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "22");
    Task task3 = new Task(Type.TASK, "2005", Status.NEW, LocalDateTime.of(2003, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "22");
    Task task4 = new Task(Type.TASK, "2006", Status.NEW, LocalDateTime.of(2004, 1, 1, 1, 1, 1), Duration.ofMinutes(20), "22");

    @AfterEach
    void clear() {
        manager.deleteAllTasks();
        manager.deleteAllSubTasks();
        manager.deleteAllEpics();
    }

    @Test
    void doublingTask() {
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createTask(task4);
        historyManager.add(manager.getTaskById(1));
        historyManager.add(manager.getTaskById(2));
        historyManager.add(manager.getTaskById(1));
        historyManager.add(manager.getTaskById(2));
        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    void deleteFromBetween() {
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createTask(task4);
        historyManager.add(manager.getTaskById(1));
        historyManager.add(manager.getTaskById(2));
        historyManager.add(manager.getTaskById(3));
        historyManager.add(manager.getTaskById(4));
        historyManager.remove(3);
        assertEquals(task4, historyManager.getHistory().get(2));
    }

    @Test
    void deleteFromEnd() {
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createTask(task4);
        historyManager.add(manager.getTaskById(1));
        historyManager.add(manager.getTaskById(2));
        historyManager.add(manager.getTaskById(3));
        historyManager.add(manager.getTaskById(4));
        historyManager.remove(4);
        assertEquals(task3, historyManager.getHistory().get(2));
        assertEquals(task2, historyManager.getHistory().get(1));
        assertEquals(task1, historyManager.getHistory().get(0));
        assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    void deleteFromStart() {
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createTask(task4);
        historyManager.add(manager.getTaskById(1));
        historyManager.add(manager.getTaskById(2));
        historyManager.add(manager.getTaskById(3));
        historyManager.add(manager.getTaskById(4));
        historyManager.remove(1);
        assertEquals(task2, historyManager.getHistory().get(0));
    }

    @Test
    void addInHistory() {
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createTask(task4);
        historyManager.add(manager.getTaskById(1));
        historyManager.add(manager.getTaskById(2));
        historyManager.add(manager.getTaskById(3));
        historyManager.add(manager.getTaskById(4));
        assertEquals(4, historyManager.getHistory().size());
    }

    @Test
    void getEmptyHistory() {
        assertEquals(0, historyManager.getHistory().size());
    }
}