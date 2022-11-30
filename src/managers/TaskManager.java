package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubTask();

    void deleteTaskById(Integer Id);

    void deleteEpicById(Integer id);

    void deleteSubTaskById(Integer id);

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    SubTask getSubTaskById(Integer id);

    void updateTask(Integer taskID, Task newTaskObject);

    void updateEpic(Integer taskID, Epic newTaskObject);

    void updateSubTask(Integer taskID, SubTask newTaskObject);

    HashMap<Integer, Task> viewAllTask();

    HashMap<Integer, Epic> viewAllEpic();

    HashMap<Integer, SubTask> viewAllSubtask();

    HashMap<Integer, SubTask> viewSubTaskOfEpic(Integer epicID);

    List<Task> getTaskHistory();

    TreeSet<Task> getPrioritizedTasks();
}