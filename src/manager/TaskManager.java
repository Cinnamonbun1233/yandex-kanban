package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {
    void printAllMaps();

    void createNewTask(Task task);

    void createNewEpic(Epic epic);

    void createNewSubtask(Subtask subtask, Integer epicId);

    void updateTask(Integer id, Task task);

    void updateEpic(Integer id, Epic epic);

    void updateSubtask(Integer id, Subtask subTask);

    void updateEpicStatus(Integer epicId);

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    Subtask getSubtaskById(Integer id);

    HashMap<Integer, Subtask> getSubtaskByEpic(Integer epicId);

    void deleteTaskById(Integer id);

    void deleteEpicById(Integer id);

    void deleteSubtaskById(Integer id);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    ArrayList<Task> getHistory();
}