package Tasks;

import java.util.HashMap;

public class Epic extends Task {
    HashMap<Integer, SubTask> internalSubtask = new HashMap();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public void addNewSubtaskInEpic(Integer id, SubTask subTask) {
        if (subTask != null) {
            internalSubtask.put(id, subTask);
        }
    }

    public HashMap<Integer, SubTask> getInternalSubtask() {
        return internalSubtask;
    }

    public void setInternalSubtask(HashMap<Integer, SubTask> internalSubtask) {
        if (internalSubtask != null) {
            this.internalSubtask = internalSubtask;
        }
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id='" + getID() + '\'' +
                ", name='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", innerSubTask='" + getInternalSubtask() + '\'' +
                '}';
    }
}