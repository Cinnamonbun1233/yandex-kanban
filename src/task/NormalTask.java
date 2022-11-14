package task;

import java.util.Objects;

public class NormalTask {

    private String title;
    private String description;
    private Status status;
    private Integer id = 1;
    private Type type;

    public NormalTask(Type type, String title, Status status, String description) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.join(",", id.toString(), type.toString(), title, status.toString(), description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NormalTask normalTask = (NormalTask) o;
        return Objects.equals(title, normalTask.title) && Objects.equals(description, normalTask.description) &&
                status == normalTask.status && Objects.equals(id, normalTask.id) && type == normalTask.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, status, id, type);
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

    public Status getTaskStatus() {
        return status;
    }

    public void setTaskStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}