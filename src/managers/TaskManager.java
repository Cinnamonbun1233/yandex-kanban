package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    void deleteTaskById(Integer Id);

    void deleteEpicById(Integer id);

    void deleteSubTaskById(Integer id);

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    SubTask getSubTaskById(Integer id);

    void updateTask(Integer taskID, Task newTaskObject);

    void updateEpic(Integer taskID, Epic newTaskObject);

    void updateSubTask(Integer taskID, SubTask newTaskObject);

    HashMap<Integer, Task> printAllTasks();

    HashMap<Integer, Epic> printAllEpics();

    HashMap<Integer, SubTask> printAllSubtasks();

    HashMap<Integer, SubTask> printSubTasksOfEpic(Integer epicID);

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();
}