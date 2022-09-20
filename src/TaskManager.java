import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    static int taskID;
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();

    public void printAllMaps() {
        HashMap<Integer, Task> allMaps = new HashMap<>();
        allMaps.putAll(taskHashMap);
        allMaps.putAll(epicHashMap);
        for (Task task : allMaps.values()) {
            System.out.println(task);
        }
    }

    public void createNewTask(Task task) {
        taskID++;
        task.setID(taskID);
        taskHashMap.put(task.getID(), task);
    }

    public void createNewEpic(Epic epic) {
        taskID++;
        epic.setID(taskID);
        epicHashMap.put(epic.getID(), epic);
    }

    public void createNewSubtask(Subtask subTask, Integer epicID) {
        taskID++;
        subTask.setID(taskID);
        subtaskHashMap.put(subTask.getID(), subTask);
        Epic epic = epicHashMap.get(epicID);
        epic.addSubtaskInEpic(taskID, subTask);
        updateEpicStatus(epicID);
    }

    public void updateTask(Integer ID, Task task) {
        task.setID(ID);
        taskHashMap.put(ID, task);
    }

    public void updateEpic(Integer ID, Epic epic) {
        epic.setID(ID);
        epic.setInternalSubtask(epicHashMap.get(ID).getInternalSubtask());
        epicHashMap.put(ID, epic);
    }

    public void updateSubtask(Integer ID, Subtask subTask) {
        Integer epicID = subtaskHashMap.get(ID).getEpicID();
        subTask.setEpicID(epicID);
        subTask.setID(ID);
        subtaskHashMap.put(ID, subTask);
        updateEpicStatus(epicID);
        Epic epic = epicHashMap.get(epicID);
        epic.addSubtaskInEpic(ID, subTask);
    }

    public void updateEpicStatus(Integer epicID) {
        ArrayList<Status> subtaskStatus = new ArrayList<>();
        for (Integer integer : subtaskHashMap.keySet()) {
            if (subtaskHashMap.get(integer).getEpicID().equals(epicID)) {
                subtaskStatus.add(subtaskHashMap.get(integer).getStatus());
            }
        }
        if ((!subtaskStatus.contains(Status.IN_PROGRESS) && !subtaskStatus.contains(Status.NEW))) {
            epicHashMap.get(epicID).setStatus(Status.DONE);
        } else if ((!subtaskStatus.contains(Status.IN_PROGRESS) && !subtaskStatus.contains(Status.DONE))) {
            epicHashMap.get(epicID).setStatus(Status.NEW);
        } else {
            epicHashMap.get(epicID).setStatus(Status.IN_PROGRESS);
        }
    }

    public Task getTaskByID(Integer ID) {
        return taskHashMap.get(ID);
    }

    public Epic getEpicByID(Integer ID) {
        return epicHashMap.get(ID);
    }

    public Subtask getSubtaskByID(Integer ID) {
        return subtaskHashMap.get(ID);
    }

    public HashMap<Integer, Subtask> getSubtaskByEpic(Integer epicID) {
        Epic epic = epicHashMap.get(epicID);
        return epic.getInternalSubtask();
    }

    public void deleteTaskByID(Integer ID) {
        taskHashMap.remove(ID);
    }

    public void deleteEpicById(Integer ID) {
        Epic epic = epicHashMap.get(ID);
        HashMap<Integer, Subtask> internalSubtask = epic.getInternalSubtask();
        for (Integer integer : internalSubtask.keySet()) {
            subtaskHashMap.remove(integer);
        }
        epicHashMap.remove(ID);
    }

    public void deleteSubtaskByID(Integer ID) {
        Subtask subtaskForDelete = subtaskHashMap.get(ID);
        HashMap<Integer, Subtask> newInternalSubtask = epicHashMap.get(subtaskForDelete.getEpicID()).getInternalSubtask();
        if (newInternalSubtask != null) {
            newInternalSubtask.remove(ID);
        }
        subtaskHashMap.remove(ID);
        updateEpicStatus(subtaskForDelete.getEpicID());
    }

    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    public void deleteAllEpics() {
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    public void deleteAllSubtasks() {
        subtaskHashMap.clear();
        epicHashMap.clear();
    }
}