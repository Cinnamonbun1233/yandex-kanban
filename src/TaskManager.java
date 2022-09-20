import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public static int ID = 0;
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    HashMap<Integer, SubTask> subtaskHashMap = new HashMap<>();


    //✅
    public void viewAllTask() {
        HashMap<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(taskHashMap);
        allTasks.putAll(epicHashMap);
        for (Task task : allTasks.values()) {
            System.out.println(task);
        }
    }

    //✅
    public void createNewTask(Task task) {
        ID++;
        task.setID(ID);
        taskHashMap.put(task.getID(), task);
    }

    //✅
    public void createNewEpic(Epic epic) {
        ID++;
        epic.setID(ID);
        epicHashMap.put(epic.getID(), epic);
    }

    //✅
    public void createNewSubtask(SubTask subTask, Integer epicId) {
        ID++;
        subTask.setID(ID);
        subtaskHashMap.put(subTask.getID(), subTask);
        Epic epic = epicHashMap.get(epicId);
        epic.addNewSubtaskInEpic(ID, subTask);
        updateEpicStatus(epicId);
    }

    //✅
    public void updateTask(Integer ID, Task task) {
        task.setID(ID);
        taskHashMap.put(ID, task);
    }

    //✅
    public void updateEpic(Integer ID, Epic epic) {
        epic.setID(ID);
        epic.setInternalSubtask(epicHashMap.get(ID).getInternalSubtask());
        epicHashMap.put(ID, epic);
    }

    //✅
    public void updateSubtask(Integer ID, SubTask subTask) {
        Integer epicId = subtaskHashMap.get(ID).getEpicId();
        subTask.setEpicId(epicId);
        subTask.setID(ID);
        subtaskHashMap.put(ID, subTask);
        updateEpicStatus(epicId);
        Epic epic = epicHashMap.get(epicId);
        epic.addNewSubtaskInEpic(ID, subTask);
    }

    //✅
    public void updateEpicStatus(Integer epicID) {
        ArrayList<Status> subtaskStatus = new ArrayList<>();
        Status updateEpicStatus;
        for (Integer subtaskKey : subtaskHashMap.keySet()) {
            if (subtaskHashMap.get(subtaskKey).getEpicId().equals(epicID)) {
                subtaskStatus.add(subtaskHashMap.get(subtaskKey).getStatus());
            }
        }
        if ((!subtaskStatus.contains(Status.IN_PROGRESS) && !subtaskStatus.contains(Status.NEW))) {
            updateEpicStatus = Status.DONE;
        } else if ((!subtaskStatus.contains(Status.IN_PROGRESS) && !subtaskStatus.contains(Status.DONE))) {
            updateEpicStatus = Status.NEW;
        } else {
            updateEpicStatus = Status.IN_PROGRESS;
        }
        epicHashMap.get(epicID).setStatus(updateEpicStatus);
    }

    //✅
    public Task getTaskById(Integer id) {
        return taskHashMap.get(id);
    }

    //✅
    public Epic getEpicById(Integer id) {
        return epicHashMap.get(id);
    }

    //✅
    public SubTask getSubtaskById(Integer id) {
        return subtaskHashMap.get(id);
    }

    //✅
    public HashMap<Integer, SubTask> getSubtaskByEpic(Integer epicID) {
        Epic epic = epicHashMap.get(epicID);
        return epic.getInternalSubtask();
    }

    //✅
    public void deleteTaskById(Integer Id) {
        taskHashMap.remove(Id);
    }

    //✅
    public void deleteEpicById(Integer id) {
        Epic epic = epicHashMap.get(id);
        HashMap<Integer, SubTask> internalSubtask = epic.getInternalSubtask();
        for (Integer subTasksKey : internalSubtask.keySet()) {
            subtaskHashMap.remove(subTasksKey);
        }
        epicHashMap.remove(id);
    }

    //✅
    public void deleteSubTaskById(Integer id) {
        SubTask subTaskForDelete = subtaskHashMap.get(id);
        HashMap<Integer, SubTask> newInnerSubTask = epicHashMap.get(subTaskForDelete.getEpicId()).getInternalSubtask();
        if (newInnerSubTask != null) {
            newInnerSubTask.remove(id);
        }
        subtaskHashMap.remove(id);
        updateEpicStatus(subTaskForDelete.getEpicId());
    }

    //✅
    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    //✅
    public void deleteAllEpics() {
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    //✅
    public void deleteAllSubTasks() {
        subtaskHashMap.clear();
        epicHashMap.clear();
    }
}