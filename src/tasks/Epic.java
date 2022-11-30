package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    HashMap<Integer, SubTask> innerSubTask = new HashMap<>();
    LocalDateTime endTime;

    public Epic(Type type, String title, String description, Status status) {
        super(type, title, description, status);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void refreshTimeEpic() {
        if (getInnerSubTask().isEmpty()) {
            setStartTime(null);
            setEndTime(null);
            setDuration(null);
            System.out.println("Не удалось установить время эпика");
        } else {
            LocalDateTime startTime = LocalDateTime.of(3000, 1, 1, 1, 1, 1);
            LocalDateTime endTime = LocalDateTime.of(1000, 1, 1, 1, 1, 1);
            Duration duration = Duration.ZERO;
            for (SubTask subTask : innerSubTask.values()) {
                LocalDateTime subTaskStartTime = subTask.getStartTime();
                LocalDateTime subTaskEndTime = subTask.getEndTime();
                duration = duration.plus(subTask.getDuration());
                if (subTaskStartTime.isBefore(startTime)) {
                    startTime = subTaskStartTime;
                }
                if (subTaskEndTime.isAfter(endTime)) {
                    endTime = subTaskEndTime;
                }
            }
            setStartTime(startTime);
            System.out.println("Установлена дата начала эпика: " + startTime);
            setEndTime(endTime);
            System.out.println("Установлена дата конца эпика: " + endTime);
            setDuration(duration);
            System.out.println("Установлена продолжительность эпика: " + duration);
        }
    }

    public void addNewSubtaskInEpic(SubTask subTask) {
        if (subTask != null) {
            innerSubTask.put(subTask.getId(), subTask);
            refreshTimeEpic();
            updateEpicStatus();
        }
    }

    public void updateEpicStatus() {
        Status epicStatus;
        if (innerSubTask.isEmpty()) {
            epicStatus = Status.NEW;
            System.out.println("Новый пустой эпик");
        } else {
            ArrayList<Status> subStatusList = new ArrayList<>();
            for (SubTask sub : innerSubTask.values()) {
                subStatusList.add(sub.getStatus());
            }
            if (subStatusList.contains(Status.DONE) && (!subStatusList.contains(Status.IN_PROGRESS)
                    && !subStatusList.contains(Status.NEW))) {
                epicStatus = Status.DONE;
                System.out.println("DONE ");
            } else if (subStatusList.contains(Status.NEW) && (!subStatusList.contains(Status.IN_PROGRESS)
                    && !subStatusList.contains(Status.DONE))) {
                epicStatus = Status.NEW;
                System.out.println("NEW ");
            } else {
                epicStatus = Status.IN_PROGRESS;
                System.out.println("IN_PROGRESS ");
            }
        }
        setStatus(epicStatus);
    }

    public HashMap<Integer, SubTask> getInnerSubTask() {
        return innerSubTask;
    }

    public void setInnerSubTask(HashMap<Integer, SubTask> innerSubTask) {
        if (innerSubTask != null) {
            this.innerSubTask = innerSubTask;
        }
    }
}