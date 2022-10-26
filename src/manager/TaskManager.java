package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    void printAllMaps();

    void createNewTask(Task task);

    void createNewEpic(Epic epic);

    void createNewSubtask(Subtask subtask, Integer epicId);

    void updateTask(Integer taskId, Task task);

    void updateEpic(Integer taskId, Epic epic);

    void updateSubtask(Integer taskId, Subtask subTask);

    void updateEpicStatus(Integer epicId);

    Task getTaskById(Integer taskId);

    Epic getEpicById(Integer taskId);

    Subtask getSubtaskById(Integer taskId);

    HashMap<Integer, Subtask> getSubtaskByEpic(Integer epicId);

    void deleteTaskById(Integer taskId);

    void deleteEpicById(Integer taskId);

    void deleteSubtaskById(Integer taskId);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    List<Task> getHistory();
}