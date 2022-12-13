package managers;

import network.HttpTaskManager;

import java.io.IOException;
import java.net.URI;

public class Managers {
    public static TaskManager getDefault(URI uri) throws IOException, InterruptedException {
        return new HttpTaskManager(uri);
    }

    public static TaskManager getFileBackedTasksManager() {
        return new FileBackedTasksManager();
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}