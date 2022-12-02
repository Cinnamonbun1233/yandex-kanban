package tests;

import managers.FileBackedTasksManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest {

    String HISTORY_IS_NOT_EMPTY = "история не пуста";
    String TASK_NOT_RESTORED = "таск не восстановился";

    TaskManager createTaskManager() {
        return new FileBackedTasksManager(new File("src/data", "data.csv"));
    }

    @AfterEach
    void clear() {
        taskManager.deleteAllTask();
        taskManager.deleteAllSubTask();
        taskManager.deleteAllEpic();
    }

    @Test
    void saveAndLoadFileWithEmptyHistory() {
        taskManager.addTask(task1);
        assertTrue(taskManager.getTaskHistory().isEmpty(), HISTORY_IS_NOT_EMPTY);
        TaskManager taskManager2 = new FileBackedTasksManager(new File("src/data", "data.csv"));
        assertTrue(taskManager2.getTaskHistory().isEmpty(), HISTORY_IS_NOT_EMPTY);
        assertFalse(taskManager2.viewAllTask().isEmpty(), TASK_NOT_RESTORED);
    }

    @Test
    void saveAndLoadFileWithEmptyEpic() {
        taskManager.addEpic(epic3);
        assertTrue(taskManager.getTaskHistory().isEmpty(), HISTORY_IS_NOT_EMPTY);
        TaskManager taskManager2 = new FileBackedTasksManager(new File("src/data", "data.csv"));
        assertTrue(taskManager2.getTaskHistory().isEmpty(), HISTORY_IS_NOT_EMPTY);
        assertFalse(taskManager2.viewAllEpic().isEmpty(), TASK_NOT_RESTORED);
    }

    @Test
    void saveAndLoadFileWithEmptyTasks() {
        assertAll(() -> assertTrue(taskManager.viewAllTask().isEmpty(), "таски не пусты"));
        TaskManager taskManager2 = new FileBackedTasksManager(new File("src/data", "data.csv"));
        assertTrue(taskManager2.getTaskHistory().isEmpty(), HISTORY_IS_NOT_EMPTY);
        assertTrue(taskManager2.viewAllEpic().isEmpty(), "список пуст");
    }

    @Test
    void addEpicTEST() {
        super.addEpicTest();
    }

    @Test
    void addSubTaskTEST() {
        super.addSubTaskTest();
    }

    @Test
    void deleteAllTaskTEST() {
        super.deleteAllTaskTest();
    }

    @Test
    void deleteAllEpicTEST() {
        super.deleteAllEpicTest();
    }

    @Test
    void deleteAllSubTaskTEST() {
        super.deleteAllSubTaskTest();
    }

    @Test
    void deleteTaskByIdTEST() {
        super.deleteTaskByIdTest();
    }

    @Test
    void deleteEpicByIdTEST() {
        super.deleteEpicByIdTest();
    }

    @Test
    void deleteSubTaskByIdTEST() {
        super.deleteSubTaskByIdTest();
    }

    @Test
    void getTaskByIdTEST() {
        super.getTaskByIdTest();
    }

    @Test
    void getEpicByIdTEST() {
        super.getEpicByIdTest();
    }

    @Test
    void getSubTaskByIdTEST() {
        super.getSubTaskByIdTest();
    }

    @Test
    void updateTaskTEST() {
        super.updateTaskTest();
    }

    @Test
    void updateEpicTEST() {
        super.updateEpicTest();
    }

    @Test
    void updateSubTaskTEST() {
        super.updateSubTaskTest();
    }

    @Test
    void viewAllTaskTEST() {
        super.viewAllTaskTest();
    }

    @Test
    void viewSubTaskOfEpicTEST() {
        super.viewSubTaskOfEpicTest();
    }

    @Test
    void getTaskHistoryTEST() {
        super.getTaskHistoryTest();
    }

    @Test
    void getPrioritizedTasksTEST() {
        super.getPrioritizedTasksTest();
    }
}