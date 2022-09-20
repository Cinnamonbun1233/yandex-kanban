package Tasks;

import java.util.HashMap;

public class Epic extends Task {
    HashMap<Integer, Subtask> internalSubtask = new HashMap<>();

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    public void addSubtaskInEpic(Integer ID, Subtask subTask) {
        internalSubtask.put(ID, subTask);
    }

    public HashMap<Integer, Subtask> getInternalSubtask() {
        return internalSubtask;
    }

    public void setInternalSubtask(HashMap<Integer, Subtask> internalSubtask) {
        this.internalSubtask = internalSubtask;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "ID='" + getID() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", internalSubtask='" + getInternalSubtask() + '\'' +
                '}';
    }
}