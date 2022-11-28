package tests;

import managers.FileBackedTasksManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest {

    TaskManager createTaskManager() {
        return new FileBackedTasksManager(new File("src/data", "data.csv"));
    }

    @AfterEach
    void clear() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllEpics();
    }

    @Test
    void SaveAndLoadFileWithEmptyHistory() {
        taskManager.createTask(task1);
        assertTrue(taskManager.getHistory().isEmpty(), "история не пуста");
        TaskManager taskManager2 = new FileBackedTasksManager(new File("src/data", "data.csv"));
        assertTrue(taskManager2.getHistory().isEmpty(), "история не пуста");
        assertFalse(taskManager2.printAllTasks().isEmpty(), "таск не восстановился");
    }

    @Test
    void SaveAndLoadFileWithEmptyEpic() {
        taskManager.createEpic(epic3);
        assertTrue(taskManager.getHistory().isEmpty(), "история не пуста");
        TaskManager taskManager2 = new FileBackedTasksManager(new File("src/data", "data.csv"));
        assertTrue(taskManager2.getHistory().isEmpty(), "история не пуста");
        assertFalse(taskManager2.printAllEpics().isEmpty(), "таск не восстановился");
    }

    @Test
    void SaveAndLoadFileWithEmptyTasks() {
        assertTrue(taskManager.printAllTasks().isEmpty(), "таски не пусты");
        assertTrue(taskManager.printAllEpics().isEmpty(), "таски не пусты");
        assertTrue(taskManager.printAllSubtasks().isEmpty(), "таски не пусты");
        TaskManager taskManager2 = new FileBackedTasksManager(new File("src/data", "data.csv"));
        assertTrue(taskManager2.getHistory().isEmpty(), "история не пуста");
        assertTrue(taskManager2.printAllEpics().isEmpty(), "список пуст");
    }

    @Test
    void addEpicTEST() {
        super.addEpicTEST();
    }

    @Test
    void addSubTaskTEST() {
        super.addSubTaskTEST();
    }

    @Test
    void deleteAllTaskTEST() {
        super.deleteAllTaskTEST();
    }

    @Test
    void deleteAllEpicTEST() {
        super.deleteAllEpicTEST();
    }

    @Test
    void deleteAllSubTaskTEST() {
        super.deleteAllSubTaskTEST();
    }

    @Test
    void deleteTaskByIdTEST() {
        super.deleteTaskByIdTEST();
    }

    @Test
    void deleteEpicByIdTEST() {
        super.deleteEpicByIdTEST();
    }

    @Test
    void deleteSubTaskByIdTEST() {
        super.deleteSubTaskByIdTEST();
    }

    @Test
    void getTaskByIdTEST() {
        super.getTaskByIdTEST();
    }

    @Test
    void getEpicByIdTEST() {
        super.getEpicByIdTEST();
    }

    @Test
    void getSubTaskByIdTEST() {
        super.getSubTaskByIdTEST();
    }

    @Test
    void updateTaskTEST() {
        super.updateTaskTEST();
    }

    @Test
    void updateEpicTEST() {
        super.updateEpicTEST();
    }

    @Test
    void updateSubTaskTEST() {
        super.updateSubTaskTEST();
    }

    @Test
    void viewAllTaskTEST() {
        super.viewAllTaskTEST();
    }

    @Test
    void viewSubTaskOfEpicTEST() {
        super.viewSubTaskOfEpicTEST();
    }

    @Test
    void getTaskHistoryTEST() {
        super.getTaskHistoryTEST();
    }

    @Test
    void getPrioritizedTasksTEST() {
        super.getPrioritizedTasksTEST();
    }
}