package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private Integer id;
    private Type type;
    private String title;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(Type type, String title, Status status, LocalDateTime startTime, Duration duration,
                String description) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.description = description;
    }

    public Task(Type type, String title, String description, Status status) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" dd.MM.yyyy | HH:mm ");
        String formatDateTime;
        if (startTime == null) {
            formatDateTime = "null";
        } else {
            formatDateTime = startTime.format(formatter);
        }

        String taskStatus;
        if (status == null) {
            taskStatus = "null";
        } else {
            taskStatus = status.toString();
        }

        String taskDuration;
        if (duration == null) {
            taskDuration = "null";
        } else {
            taskDuration = duration.toString();
        }

        return String.join(",", id.toString(), type.toString(), title, taskStatus, formatDateTime,
                taskDuration, description);
    }
}