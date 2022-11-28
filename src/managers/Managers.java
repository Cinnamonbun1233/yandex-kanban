package managers;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("src/data", "data.csv"));
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}