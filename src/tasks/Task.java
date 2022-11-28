package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private Type type;
    private Integer id = 1;
    private String title;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(Type type, String title, Status status, LocalDateTime startTime,
                Duration duration, String description) {
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm");
        String dateTime;
        String status;
        String duration;

        if (this.startTime == null) {
            dateTime = "null";
        } else {
            dateTime = this.startTime.format(formatter);
        }

        if (this.status == null) {
            status = "null";
        } else {
            status = this.status.toString();
        }

        if (this.duration == null) {
            duration = "null";
        } else {
            duration = this.duration.toString();
        }

        return String.join(",", id.toString(), type.toString(), title, status, dateTime,
                duration, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return type == task.type && Objects.equals(id, task.id) &&
                Objects.equals(title, task.title) && Objects.equals(description, task.description) &&
                status == task.status && Objects.equals(duration, task.duration) &&
                Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, title, description, status, duration, startTime);
    }
}