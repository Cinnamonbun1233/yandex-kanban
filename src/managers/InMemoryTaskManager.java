package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public HistoryManager historyManager = Managers.getDefaultHistory();
    public int id = 0;

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return sortedTasks;
    }

    @Override
    public void addTask(Task task) {
        if (tasks != null && task != null) {
            if (timeNotBusy(task)) {
                task.setId(NextIndex());
                tasks.put(task.getId(), task);
                System.out.println("Таск добавлен: " + task);
                sortedTasks.add(task);
            } else {
                System.out.println("Пересекает время другого таска. Таск не добавлен");
            }
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (epics != null && epic != null) {
            epic.setId(NextIndex());
            epic.updateEpicStatus();
            epics.put(epic.getId(), epic);
            System.out.println("Эпик добавлен: " + epic);
        }
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (subTasks != null && subTask != null && epics != null) {
            if (epics.containsKey(subTask.getEpicId())) {
                if (timeNotBusy(subTask)) {
                    subTask.setId(NextIndex());
                    subTasks.put(subTask.getId(), subTask);
                    System.out.println("Сабтаск добавлен: " + subTask);
                    Epic epic = epics.get(subTask.getEpicId());
                    if (epic != null) {
                        epic.addNewSubtaskInEpic(subTask);
                        updateEpic(epic.getId(), epic);
                        sortedTasks.add(subTask);
                    }
                } else {
                    System.out.println("Время занято другим таском. Сабтаск не добавлен");
                }
            } else {
                System.out.println("Нет эпика с таким id");
            }
        }
    }

    boolean timeNotBusy(Task task) {
        if (task.getStartTime() != null && task.getEndTime() != null) {
            LocalDateTime startTask = task.getStartTime();
            LocalDateTime endTask = task.getEndTime();
            if (task.getStartTime() != null && task.getDuration() != null) {
                for (Task treeTask : getPrioritizedTasks()) {
                    if (treeTask.getStartTime() != null && treeTask.getEndTime() != null) {
                        LocalDateTime startTreeTask = treeTask.getStartTime();
                        LocalDateTime endTreeTask = treeTask.getEndTime();
                        if (startTask.isBefore(endTreeTask) && startTask.isAfter(startTreeTask)
                                || endTask.isBefore(endTreeTask) && endTask.isAfter(startTreeTask)
                                || startTask.isBefore(startTreeTask) && endTask.isAfter(endTreeTask)
                                || startTask.equals(startTreeTask) || endTask.equals(endTreeTask)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void deleteAllTask() {
        if (tasks != null && sortedTasks != null && historyManager != null) {
            for (Task task : tasks.values()) {
                historyManager.remove(task.getId());
                sortedTasks.remove(task);
            }
            tasks.clear();
            System.out.println("Все таски удалены");
        }
    }

    @Override
    public void deleteAllEpic() {
        if (epics != null && subTasks != null && sortedTasks != null && historyManager != null) {
            for (Task task : epics.values()) {
                historyManager.remove(task.getId());
            }
            for (Task task : subTasks.values()) {
                historyManager.remove(task.getId());
                sortedTasks.remove(task);
            }
            epics.clear();
            subTasks.clear();
            System.out.println("Все эпики удалены");
        }
    }

    @Override
    public void deleteAllSubTask() {
        if (subTasks != null && sortedTasks != null && historyManager != null) {
            for (Task task : subTasks.values()) {
                historyManager.remove(task.getId());
                sortedTasks.remove(task);
            }
            subTasks.clear();
            System.out.println("Все сабтаски удалены");

        }
    }

    @Override
    public void deleteTaskById(Integer id) {
        if (tasks != null && sortedTasks != null && historyManager != null && tasks.containsKey(id)) {
            sortedTasks.remove(tasks.get(id));
            historyManager.remove(id);
            tasks.remove(id);
        }
    }

    @Override
    public void deleteEpicById(Integer id) {
        if (epics != null && epics.containsKey(id) && subTasks != null && sortedTasks != null) {
            historyManager.remove(id);
            Epic epic = epics.get(id);
            HashMap<Integer, SubTask> innerSubTaskMap = epic.getInnerSubTask();
            for (Task subtask : innerSubTaskMap.values()) {
                subTasks.remove(subtask.getId());
                sortedTasks.remove(subtask);
            }
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        if (subTasks.containsKey(id) && epics != null && epics.containsKey(id) && sortedTasks != null) {
            SubTask subTaskForDelete = subTasks.get(id);
            Epic epicForUpdate = epics.get(subTaskForDelete.getEpicId());
            HashMap<Integer, SubTask> newInnerSubTask = epicForUpdate.getInnerSubTask();
            if (newInnerSubTask != null) {
                newInnerSubTask.remove(id);
            }
            epicForUpdate.setInnerSubTask(newInnerSubTask);
            Integer epicId = epicForUpdate.getId();
            updateEpic(epicId, epicForUpdate);
            historyManager.remove(id);
            subTasks.remove(id);
            System.out.println("Cабтаск удален");
        }
    }

    @Override
    public Task getTaskById(Integer id) {
        if (tasks != null && tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpicById(Integer id) {
        if (epics != null && epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
        return null;
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        if (subTasks != null && subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        }
        return null;
    }

    @Override
    public void updateTask(Integer taskID, Task newTaskObject) {
        if (tasks != null && newTaskObject != null && tasks.containsKey(taskID)) {
            newTaskObject.setId(taskID);
            tasks.put(taskID, newTaskObject);
            System.out.println("Эпик обновлен");
        }
    }

    @Override
    public void updateEpic(Integer taskID, Epic newTaskObject) {
        if (epics != null && newTaskObject != null && epics.containsKey(taskID)) {
            newTaskObject.setId(taskID);
            newTaskObject.setInnerSubTask(epics.get(taskID).getInnerSubTask());
            newTaskObject.updateEpicStatus();
            epics.put(taskID, newTaskObject);
            System.out.println("Эпик обновлен");
        }
    }

    @Override
    public void updateSubTask(Integer taskID, SubTask newTaskObject) {
        if (subTasks != null && newTaskObject != null && subTasks.containsKey(taskID)) {
            Integer epicId = subTasks.get(taskID).getEpicId();
            newTaskObject.setEpicId(epicId);
            newTaskObject.setId(taskID);
            subTasks.put(taskID, newTaskObject);
            System.out.println("Сабтаск обновлен");
            Epic epicForUpdate = epics.get(epicId);
            HashMap<Integer, SubTask> newInnerSubTask = epicForUpdate.getInnerSubTask();
            if (newInnerSubTask != null) {
                newInnerSubTask.put(taskID, newTaskObject);
            }
            epicForUpdate.setInnerSubTask(newInnerSubTask);
            updateEpic(epicId, epicForUpdate);
            System.out.println("Эпик тоже обновлен");
        } else {
            System.out.println("Ошибка!");
        }
    }

    @Override
    public HashMap<Integer, Task> viewAllTask() {
        if (tasks != null) {
            return tasks;
        }
        return null;
    }

    @Override
    public HashMap<Integer, Epic> viewAllEpic() {
        if (epics != null) {
            return epics;
        }
        return null;
    }

    @Override
    public HashMap<Integer, SubTask> viewAllSubtask() {
        if (subTasks != null) {
            return subTasks;
        }
        return null;
    }

    @Override
    public HashMap<Integer, SubTask> viewSubTaskOfEpic(Integer epicID) {
        if (epics != null && epics.containsKey(epicID)) {
            Epic epic = epics.get(epicID);
            return epic.getInnerSubTask();
        }
        return null;
    }

    @Override
    public List<Task> getTaskHistory() {
        return historyManager.getHistory();
    }

    Comparator<Task> startTimeComparator = (t1, t2) -> {
        if (t1.getStartTime() == null && t2.getStartTime() == null) {
            return t1.getId() - t2.getId();
        } else if (t1.getStartTime() == null) {
            return -1;
        } else if (t2.getStartTime() == null) {
            return 1;
        } else {
            return t1.getStartTime().compareTo(t2.getStartTime());
        }
    };

    public TreeSet<Task> sortedTasks = new TreeSet<>(startTimeComparator);

    private int NextIndex() {
        return ++id;
    }
}