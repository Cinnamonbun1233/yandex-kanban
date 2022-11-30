package tests;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @Test
    TaskManager createTaskManager() {
        return Managers.getInMemoryTaskManager();
    }

    @Test
    void addTaskTEST() {
        super.addTaskTest();
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