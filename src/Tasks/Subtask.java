package Tasks;

public class Subtask extends Task {
    Integer epicID;

    public Subtask(String title, String description, Status status, Integer epicID) {
        super(title, description, status);
        this.epicID = epicID;
    }

    public Integer getEpicID() {
        return epicID;
    }

    public void setEpicID(Integer epicID) {
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "ID='" + getID() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}