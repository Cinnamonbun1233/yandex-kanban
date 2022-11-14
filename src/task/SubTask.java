package task;

public class SubTask extends NormalTask {
    Integer epicId;

    public SubTask(Type type, String title, Status status, String description, Integer epicId) {
        super(type, title, status, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.join(",", getId().toString(), getType().toString(), getTitle(),
                getTaskStatus().toString(), getDescription(), getEpicId().toString());
    }
}