package manager;

import task.NormalTask;

import java.util.List;

public interface HistoryManager {

    void add(NormalTask normalTask);

    void remove(Integer taskId);

    List<NormalTask> getHistory();
}