package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public void updateTask(Integer taskId, Task task) {
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    @Override
    public void updateEpic(Integer taskId, Epic epic) {
        epic.setId(taskId);
        epic.setInternalSubtask(epics.get(taskId).getInternalSubtask());
        epics.put(taskId, epic);
    }

    @Override
    public void updateSubtask(Integer taskId, Subtask subTask) {
        Integer epicID = subtasks.get(taskId).getEpicId();
        subTask.setEpicId(epicID);
        subTask.setId(taskId);
        subtasks.put(taskId, subTask);
        updateEpicStatus(epicID);
        Epic epic = epics.get(epicID);
        epic.addSubtaskInEpic(taskId, subTask);
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
    public Task getTaskById(Integer taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicById(Integer taskId) {
        historyManager.add(epics.get(taskId));
        return epics.get(taskId);
    }

    @Override
    public Subtask getSubtaskById(Integer taskId) {
        historyManager.add(subtasks.get(taskId));
        return subtasks.get(taskId);
    }

    @Override
    public HashMap<Integer, Subtask> getSubtaskByEpic(Integer epicId) {
        Epic epic = epics.get(epicId);
        return epic.getInternalSubtask();
    }

    @Override
    public void deleteTaskById(Integer taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpicById(Integer taskId) {
        Epic epic = epics.get(taskId);
        HashMap<Integer, Subtask> internalSubtask = epic.getInternalSubtask();
        for (Integer integer : internalSubtask.keySet()) {
            subtasks.remove(integer);
        }
        epics.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteSubtaskById(Integer taskId) {
        Subtask subtaskForDelete = subtasks.get(taskId);
        HashMap<Integer, Subtask> newInternalSubtask = epics.get(subtaskForDelete.getEpicId()).getInternalSubtask();
        if (newInternalSubtask != null) {
            newInternalSubtask.remove(taskId);
        }
        subtasks.remove(taskId);
        updateEpicStatus(subtaskForDelete.getEpicId());
        historyManager.remove(taskId);
    }

    @Override
    public void deleteAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer taskId : epics.keySet()) {
            historyManager.remove(taskId);
        }
        for (Integer taskId : subtasks.keySet()) {
            historyManager.remove(taskId);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Integer taskId : subtasks.keySet()) {
            historyManager.remove(taskId);
        }
        subtasks.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}