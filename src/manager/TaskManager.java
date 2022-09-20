package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    static int taskId;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public void printAllMaps() {
        HashMap<Integer, Task> allMaps = new HashMap<>();
        allMaps.putAll(tasks);
        allMaps.putAll(epics);
        for (Task task : allMaps.values()) {
            System.out.println(task);
        }
    }

    public void createNewTask(Task task) {
        taskId++;
        task.setId(taskId);
        tasks.put(task.getId(), task);
    }

    public void createNewEpic(Epic epic) {
        taskId++;
        epic.setId(taskId);
        epics.put(epic.getId(), epic);
    }

    public void createNewSubtask(Subtask subtask, Integer epicId) {
        taskId++;
        subtask.setId(taskId);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(epicId);
        epic.addSubtaskInEpic(taskId, subtask);
        updateEpicStatus(epicId);
    }

    public void updateTask(Integer id, Task task) {
        task.setId(id);
        tasks.put(id, task);
    }

    public void updateEpic(Integer id, Epic epic) {
        epic.setId(id);
        epic.setInternalSubtask(epics.get(id).getInternalSubtask());
        epics.put(id, epic);
    }

    public void updateSubtask(Integer id, Subtask subTask) {
        Integer epicID = subtasks.get(id).getEpicId();
        subTask.setEpicId(epicID);
        subTask.setId(id);
        subtasks.put(id, subTask);
        updateEpicStatus(epicID);
        Epic epic = epics.get(epicID);
        epic.addSubtaskInEpic(id, subTask);
    }

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

    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(Integer id) {
        return subtasks.get(id);
    }

    public HashMap<Integer, Subtask> getSubtaskByEpic(Integer epicId) {
        Epic epic = epics.get(epicId);
        return epic.getInternalSubtask();
    }

    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }

    public void deleteEpicById(Integer id) {
        Epic epic = epics.get(id);
        HashMap<Integer, Subtask> internalSubtask = epic.getInternalSubtask();
        for (Integer integer : internalSubtask.keySet()) {
            subtasks.remove(integer);
        }
        epics.remove(id);
    }

    public void deleteSubtaskById(Integer id) {
        Subtask subtaskForDelete = subtasks.get(id);
        HashMap<Integer, Subtask> newInternalSubtask = epics.get(subtaskForDelete.getEpicId()).getInternalSubtask();
        if (newInternalSubtask != null) {
            newInternalSubtask.remove(id);
        }
        subtasks.remove(id);
        updateEpicStatus(subtaskForDelete.getEpicId());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        epics.clear();
    }
}