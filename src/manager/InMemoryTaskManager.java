package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    static int taskId;
    public final HistoryManager historyManager = Managers.getDefaultHistory();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    @Override
    public void printAllMaps() {
        HashMap<Integer, Task> allMaps = new HashMap<>();
        allMaps.putAll(tasks);
        allMaps.putAll(epics);
        for (Task task : allMaps.values()) {
            System.out.println(task);
        }
    }

    @Override
    public void createNewTask(Task task) {
        taskId++;
        task.setId(taskId);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createNewEpic(Epic epic) {
        taskId++;
        epic.setId(taskId);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createNewSubtask(Subtask subtask, Integer epicId) {
        taskId++;
        subtask.setId(taskId);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(epicId);
        epic.addSubtaskInEpic(taskId, subtask);
        updateEpicStatus(epicId);
    }

    @Override
    public void updateTask(Integer id, Task task) {
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void updateEpic(Integer id, Epic epic) {
        epic.setId(id);
        epic.setInternalSubtask(epics.get(id).getInternalSubtask());
        epics.put(id, epic);
    }

    @Override
    public void updateSubtask(Integer id, Subtask subTask) {
        Integer epicID = subtasks.get(id).getEpicId();
        subTask.setEpicId(epicID);
        subTask.setId(id);
        subtasks.put(id, subTask);
        updateEpicStatus(epicID);
        Epic epic = epics.get(epicID);
        epic.addSubtaskInEpic(id, subTask);
    }

    @Override
    public void updateEpicStatus(Integer epicId) {
        ArrayList<Status> subtaskStatus = new ArrayList<>();
        for (Integer integer : subtasks.keySet()) {
            if (subtasks.get(integer).getEpicId().equals(epicId)) {
                subtaskStatus.add(subtasks.get(integer).getStatus());
            }
        }
        if ((!subtaskStatus.contains(Status.IN_PROGRESS) && !subtaskStatus.contains(Status.NEW))) {
            epics.get(epicId).setStatus(Status.DONE);
        } else if ((!subtaskStatus.contains(Status.IN_PROGRESS) && !subtaskStatus.contains(Status.DONE))) {
            epics.get(epicId).setStatus(Status.NEW);
        } else {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public Task getTaskById(Integer id) {
        historyManager.addInHistory(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(Integer id) {
        historyManager.addInHistory(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        historyManager.addInHistory(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public HashMap<Integer, Subtask> getSubtaskByEpic(Integer epicId) {
        Epic epic = epics.get(epicId);
        return epic.getInternalSubtask();
    }

    @Override
    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(Integer id) {
        Epic epic = epics.get(id);
        HashMap<Integer, Subtask> internalSubtask = epic.getInternalSubtask();
        for (Integer integer : internalSubtask.keySet()) {
            subtasks.remove(integer);
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        Subtask subtaskForDelete = subtasks.get(id);
        HashMap<Integer, Subtask> newInternalSubtask = epics.get(subtaskForDelete.getEpicId()).getInternalSubtask();
        if (newInternalSubtask != null) {
            newInternalSubtask.remove(id);
        }
        subtasks.remove(id);
        updateEpicStatus(subtaskForDelete.getEpicId());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
}