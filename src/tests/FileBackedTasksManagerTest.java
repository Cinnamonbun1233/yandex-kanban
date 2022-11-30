package tests;

import managers.FileBackedTasksManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTasksManagerTest extends TaskManagerTest {


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
        assertTrue(taskManager.getTaskHistory().isEmpty(), "история не пуста");
        TaskManager taskManager2 = new FileBackedTasksManager(new File("src/data", "data.csv"));
        assertTrue(taskManager2.getTaskHistory().isEmpty(), "история не пуста");
        assertFalse(taskManager2.viewAllTask().isEmpty(), "таск не восстановился");
    }


    @Test
    void saveAndLoadFileWithEmptyEpic() {
        taskManager.addEpic(epic3);
        assertTrue(taskManager.getTaskHistory().isEmpty(), "история не пуста");
        TaskManager taskManager2 = new FileBackedTasksManager(new File("src/data", "data.csv"));
        assertTrue(taskManager2.getTaskHistory().isEmpty(), "история не пуста");
        assertFalse(taskManager2.viewAllEpic().isEmpty(), "таск не восстановился");
    }

    @Test
    void saveAndLoadFileWithEmptyTasks() {
        assertTrue(taskManager.viewAllTask().isEmpty(), "таски не пусты");
        assertTrue(taskManager.viewAllEpic().isEmpty(), "таски не пусты");
        assertTrue(taskManager.viewAllSubtask().isEmpty(), "таски не пусты");
        TaskManager taskManager2 = new FileBackedTasksManager(new File("src/data", "data.csv"));
        assertTrue(taskManager2.getTaskHistory().isEmpty(), "история не пуста");
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