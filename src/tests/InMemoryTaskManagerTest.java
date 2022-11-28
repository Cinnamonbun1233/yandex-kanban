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
        super.addTaskTEST();
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