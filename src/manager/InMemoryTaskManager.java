package manager;

import task.EpicTask;
import task.SubTask;
import task.NormalTask;
import task.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    static int taskId = 0;
    public HistoryManager historyManager = Managers.getDefaultHistory();
    public Map<Integer, NormalTask> normalTasks = new HashMap<>();
    public Map<Integer, EpicTask> epicTasks = new HashMap<>();
    public Map<Integer, SubTask> SubTasks = new HashMap<>();

    @Override
    public void printAllMaps(HashMap<Integer, NormalTask> map) {
        System.out.println(map);
    }

    @Override
    public void createNewTask(NormalTask normalTask) {
        taskId++;
        normalTask.setId(taskId);
        normalTasks.put(normalTask.getId(), normalTask);
    }

    @Override
    public void createNewEpic(EpicTask epicTask) {
        taskId++;
        epicTask.setId(taskId);
        epicTasks.put(epicTask.getId(), epicTask);
    }

    @Override
    public void createNewSubtask(SubTask subTask, Integer epicId) {
        taskId++;
        subTask.setId(taskId);
        SubTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epicTasks.get(epicId);
        epicTask.addSubtaskToEpic(taskId, subTask);
        updateEpicStatus(epicId);
    }

    @Override
    public void updateTask(Integer taskId, NormalTask normalTask) {
        normalTask.setId(taskId);
        normalTasks.put(taskId, normalTask);
    }

    @Override
    public void updateEpic(Integer taskId, EpicTask epicTask) {
        epicTask.setId(taskId);
        epicTask.setIntlSubtask(epicTasks.get(taskId).getIntlSubtask());
        epicTasks.put(taskId, epicTask);
    }

    @Override
    public void updateSubtask(Integer taskId, SubTask subTask) {
        Integer epicID = SubTasks.get(taskId).getEpicId();
        subTask.setEpicId(epicID);
        subTask.setId(taskId);
        SubTasks.put(taskId, subTask);
        updateEpicStatus(epicID);
        EpicTask epicTask = epicTasks.get(epicID);
        epicTask.addSubtaskToEpic(taskId, subTask);
    }

    @Override
    public void updateEpicStatus(Integer epicId) {
        ArrayList<Status> subtaskStatuses = new ArrayList<>();
        for (Integer integer : SubTasks.keySet()) {
            if (SubTasks.get(integer).getEpicId().equals(epicId)) {
                subtaskStatuses.add(SubTasks.get(integer).getTaskStatus());
            }
        }
        Status status;
        if ((!subtaskStatuses.contains(Status.IN_PROGRESS) && !subtaskStatuses.contains(Status.NEW))) {
            status = Status.DONE;
        } else if ((!subtaskStatuses.contains(Status.IN_PROGRESS) && !subtaskStatuses.contains(Status.DONE))) {
            status = Status.NEW;
        } else {
            status = Status.IN_PROGRESS;
        }
    }

    @Override
    public NormalTask getTaskById(Integer taskId) {
        historyManager.add(normalTasks.get(taskId));
        return normalTasks.get(taskId);
    }

    @Override
    public EpicTask getEpicById(Integer taskId) {
        historyManager.add(epicTasks.get(taskId));
        return epicTasks.get(taskId);
    }

    @Override
    public SubTask getSubtaskById(Integer taskId) {
        historyManager.add(SubTasks.get(taskId));
        return SubTasks.get(taskId);
    }

    @Override
    public HashMap<Integer, SubTask> getSubtaskByEpic(Integer epicId) {
        EpicTask epicTask = epicTasks.get(epicId);
        return epicTask.getIntlSubtask();
    }

    @Override
    public void deleteTaskById(Integer taskId) {
        normalTasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpicById(Integer taskId) {
        EpicTask epicTask = epicTasks.get(taskId);
        HashMap<Integer, SubTask> internalSubtask = epicTask.getIntlSubtask();
        for (Integer integer : internalSubtask.keySet()) {
            SubTasks.remove(integer);
        }
        epicTasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteSubtaskById(Integer taskId) {
        SubTask subTaskForDelete = SubTasks.get(taskId);
        HashMap<Integer, SubTask> newInternalSubtask = epicTasks.get(subTaskForDelete.getEpicId()).getIntlSubtask();
        if (newInternalSubtask != null) {
            newInternalSubtask.remove(taskId);
        }
        SubTasks.remove(taskId);
        updateEpicStatus(subTaskForDelete.getEpicId());
        historyManager.remove(taskId);
    }

    @Override
    public void deleteAllTasks() {
        for (Integer taskId : normalTasks.keySet()) {
            historyManager.remove(taskId);
        }
        normalTasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer taskId : epicTasks.keySet()) {
            historyManager.remove(taskId);
        }
        for (Integer taskId : SubTasks.keySet()) {
            historyManager.remove(taskId);
        }
        epicTasks.clear();
        SubTasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Integer taskId : SubTasks.keySet()) {
            historyManager.remove(taskId);
        }
        SubTasks.clear();
    }

    @Override
    public List<NormalTask> getHistory() {
        return historyManager.getHistory();
    }
}