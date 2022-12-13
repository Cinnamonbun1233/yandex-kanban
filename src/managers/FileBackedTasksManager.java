package managers;

import exception.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

import static java.nio.file.Files.createDirectory;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    final String HEAD_SAVE_FILE = "id, type, name, status, description, startTime, duration, epic";
    final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(" dd.MM.yyyy | HH:mm ");
    private final File fileForSave = new File("src/data", "data.csv");

    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringJoiner historyInString = new StringJoiner(",");
        for (Task task : history) {
            historyInString.add(Integer.toString(task.getId()));
        }
        return historyInString.toString();
    }

    void checkOrCreateDirAndFile(File fileForSave) {
        String home = fileForSave.getParent();
        String FileName = fileForSave.getName();
        System.out.println("Проверяем директорию:");
        if (Files.exists(Paths.get(home))) {
            System.out.println("директория существует");
        } else {
            System.out.println("директория не существует, пробуем создать новую директорию");
            try {
                createDirectory(Paths.get(home));
            } catch (IOException e) {
                throw new ManagerSaveException("невозможно создать директорию" + e.getMessage());
            }
            System.out.println("директория создана");
        }
        System.out.println("Проверяем файл:");
        if (Files.exists(Paths.get(home, FileName))) {
            System.out.println("файл существует");
        } else {
            System.out.println("файл не существует, пробуем создать новый файл");
            try {
                Files.createFile(Paths.get(home, "data.csv"));
            } catch (IOException e) {
                throw new ManagerSaveException("невозможно создать файл" + e.getMessage());
            }
            System.out.println("файл создан");
        }
    }

    public String readFileInString(File fileForSave) {
        checkOrCreateDirAndFile(fileForSave);
        StringJoiner value = new StringJoiner("\n");
        try (BufferedReader br = new BufferedReader(new FileReader(fileForSave))) {
            while (br.ready()) {
                String line = br.readLine();
                value.add(line);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла сохранения" + e.getMessage());
        }
        return value.toString();
    }

    public void save() {
        StringJoiner managerData = new StringJoiner("\n");
        managerData.add(HEAD_SAVE_FILE);
        for (Task task : tasks.values()) {
            managerData.add(task.toString());
        }
        for (Task task : epics.values()) {
            managerData.add(task.toString());
        }
        for (Task task : subTasks.values()) {
            managerData.add(task.toString());
        }
        if (!historyManager.getHistory().isEmpty()) {
            managerData.add("\n    \n" + historyToString(historyManager));
        }
        try (FileWriter fileWriter = new FileWriter(fileForSave)) {
            fileWriter.write(managerData.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла сохранения");
        }
    }

    Status setTaskType(String value) {
        if (value.equals("null")) {
            return null;
        } else {
            return Status.valueOf(value);
        }
    }

    void managerFromString(String value) {
        String splitter = "\n    \n";
        String tasksString = "";
        String historyString = "";
        if (value.contains(splitter)) {
            String[] taskAndHistory = value.split(splitter);
            tasksString = taskAndHistory[0];
            historyString = taskAndHistory[1];
        } else {
            tasksString = value;
        }
        if (!tasksString.isBlank()) {
            tasksFromString(tasksString);
            sortedListRecovery();
        }
        if (!historyString.isBlank()) {
            historyFromString(historyString);
        }
    }

    void tasksFromString(String taskString) {
        String[] splitTasksInString = taskString.split("\n");
        for (int i = 1; i < splitTasksInString.length; i++) {
            String[] splitValue = splitTasksInString[i].split(",");
            Integer id = Integer.parseInt(splitValue[0]);
            Type type = Type.valueOf(splitValue[1]);
            String name = splitValue[2];
            Status status = setTaskType(splitValue[3]);
            LocalDateTime startTime;
            if (splitValue[4].equals("null")) {
                startTime = null;
            } else {
                startTime = LocalDateTime.parse(splitValue[4], DATE_FORMATTER);
            }
            Duration duration;
            if (splitValue[5].equals("null")) {
                duration = null;
            } else {
                duration = Duration.parse(splitValue[5]);
            }
            String description = splitValue[6];
            if (type == Type.TASK) {
                Task task = new Task(type, name, status, startTime, duration, description);
                task.setId(id);
                tasks.put(task.getId(), task);
                if (this.id <= task.getId()) {
                    this.id = task.getId() + 1;
                }
            } else if (type == Type.EPIC) {
                Epic epic = new Epic(type, name, description, status);
                epic.setId(id);
                epic.setStartTime(startTime);
                epic.setDuration(duration);
                epic.setEndTime(epic.getEndTime());
                epics.put(epic.getId(), epic);
                if (this.id < epic.getId()) {
                    this.id = epic.getId() + 1;
                }
            } else {
                int epicId = Integer.parseInt(splitValue[7]);
                SubTask subTask = new SubTask(type, name, status, startTime, duration, description, epicId);
                subTask.setId(id);
                subTasks.put(subTask.getId(), subTask);
                Epic epic = epics.get(epicId);
                epic.addNewSubtaskInEpic(subTask);
                if (this.id < subTask.getId()) {
                    this.id = subTask.getId() + 1;
                }
            }
        }
    }

    void historyFromString(String historyInString) {
        String[] history = historyInString.split(",");
        for (String indexTask : history) {
            Integer index = Integer.parseInt(indexTask);
            if (tasks.containsKey(index)) {
                historyManager.add(tasks.get(index));
            } else if (epics.containsKey(index)) {
                historyManager.add(epics.get(index));
            } else if (subTasks.containsKey(index)) {
                historyManager.add(subTasks.get(index));
            }
        }
    }

    void sortedListRecovery() {
        sortedTasks.addAll(tasks.values());
        sortedTasks.addAll(subTasks.values());
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        if (task != null) {
            save();
        }
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = super.getEpicById(id);
        if (epic != null) {
            save();
        }
        return epic;
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask subTask = super.getSubTaskById(id);
        if (subTask != null) {
            save();
        }
        return subTask;
    }

    @Override
    public void updateTask(Integer taskID, Task newTaskObject) {
        super.updateTask(taskID, newTaskObject);
        save();
    }

    @Override
    public void updateEpic(Integer taskID, Epic newTaskObject) {
        super.updateEpic(taskID, newTaskObject);
        save();
    }

    @Override
    public void updateSubTask(Integer taskID, SubTask newTaskObject) {
        super.updateSubTask(taskID, newTaskObject);
        save();
    }
}