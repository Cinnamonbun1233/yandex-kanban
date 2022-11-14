package task;

import java.util.HashMap;

public class EpicTask extends NormalTask {
    HashMap<Integer, SubTask> intlSubtask = new HashMap<>();

    public EpicTask(Type type, String title, Status status, String description) {
        super(type, title, status, description);
    }

    public void addSubtaskToEpic(Integer id, SubTask subTask) {
        intlSubtask.put(id, subTask);
    }

    public HashMap<Integer, SubTask> getIntlSubtask() {
        return intlSubtask;
    }

    public void setIntlSubtask(HashMap<Integer, SubTask> intlSubtask) {
        this.intlSubtask = intlSubtask;
    }
}
