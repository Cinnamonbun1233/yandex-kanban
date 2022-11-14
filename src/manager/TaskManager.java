package manager;

import task.EpicTask;
import task.SubTask;
import task.NormalTask;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    void printAllMaps(HashMap<Integer, NormalTask> map);

    void createNewTask(NormalTask normalTask);

    void createNewEpic(EpicTask epicTask);

    void createNewSubtask(SubTask subtask, Integer epicId);

    void updateTask(Integer taskId, NormalTask normalTask);

    void updateEpic(Integer taskId, EpicTask epicTask);

    void updateSubtask(Integer taskId, SubTask subTask);

    void updateEpicStatus(Integer epicId);

    NormalTask getTaskById(Integer taskId);

    EpicTask getEpicById(Integer taskId);

    SubTask getSubtaskById(Integer taskId);

    HashMap<Integer, SubTask> getSubtaskByEpic(Integer epicId);

    void deleteTaskById(Integer taskId);

    void deleteEpicById(Integer taskId);

    void deleteSubtaskById(Integer taskId);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    List<NormalTask> getHistory();
}