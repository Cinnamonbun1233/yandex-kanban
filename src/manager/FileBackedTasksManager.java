package manager;

import exception.ManagerSaveException;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import static java.nio.file.Files.createDirectory;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
        String value = readFileInString(file);
        managerFromString(value);
    }

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        NormalTask normalTask1 = new NormalTask(Type.TASK, "Задача 1", Status.NEW, "Проверка задачи 1");
        EpicTask epicTask2 = new EpicTask(Type.EPIC, "Задача 2", Status.NEW, "Проверка задачи 2");
        SubTask task3 = new SubTask(Type.SUBTASK, "Задача 3", Status.NEW, "Проверка задачи 3", 2);

        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("добавили обычный таск");

        manager.createNewTask(normalTask1);
        System.out.println(manager.getTaskById(1) + "\n");

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("добавили эпик");

        manager.createNewEpic(epicTask2);
        System.out.println(manager.getEpicById(2) + "\n");

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("добавили сабтаск");

        manager.createNewSubtask(task3, 2);
        System.out.println(manager.getSubtaskById(3) + "\n");

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("проверили заполнение истории");

        System.out.println(manager.getHistory() + "\n");

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("проверили изменение истории");

        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getHistory() + "\n");

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("проверили восстановление информации из файла");

        TaskManager newMan = new FileBackedTasksManager(new File("src/data", "data.csv"));
        System.out.println(newMan.getHistory());
        System.out.println(newMan.getTaskById(1));
        System.out.println(newMan.getEpicById(2));
        System.out.println(newMan.getSubtaskById(3));
    }

    static void checkFileAndDir(File file) {
        String home = file.getParent();
        String name = file.getName();
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
        if (Files.exists(Paths.get(home, name))) {
            System.out.println("файл существует");
        } else {
            System.out.println("файла не существует, пробуем создать новый файл");
            try {
                Files.createFile(Paths.get(home, "data.csv"));
            } catch (IOException e) {
                throw new ManagerSaveException("невозможно создать файл" + e.getMessage());
            }
            System.out.println("файл создан");
        }
    }

    static String historyToString(HistoryManager manager) {
        List<NormalTask> history = manager.getHistory();
        StringJoiner historyInString = new StringJoiner(",");
        for (NormalTask normalTask : history) {
            historyInString.add(Integer.toString(normalTask.getId()));
        }
        return historyInString.toString();
    }

    public String readFileInString(File fileForSave) {
        checkFileAndDir(fileForSave);
        StringJoiner value = new StringJoiner("\n");
        try (Reader fileReader = new FileReader(fileForSave);
             BufferedReader br = new BufferedReader(fileReader)) {
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
        final String HEAD_SAVE_FILE = "id,type,name,status,description,epic";
        StringJoiner managerData = new StringJoiner("\n");
        managerData.add(HEAD_SAVE_FILE);
        HashMap<Integer, NormalTask> allTask = new HashMap<>();
        allTask.putAll(normalTasks);
        allTask.putAll(epicTasks);
        allTask.putAll(SubTasks);
        for (NormalTask normalTask : allTask.values()) {
            managerData.add(normalTask.toString());
        }
        if (!historyManager.getHistory().isEmpty()) {
            managerData.add("\n    \n" + historyToString(historyManager));
        }
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(managerData.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла сохранения");
        }
    }

    Status setTaskType(String value) {
        return Status.valueOf(value);
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
        }
        if (!historyString.isBlank()) {
            historyFromString(historyString);
        }
    }

    void tasksFromString(String taskString) {
        String[] splitTasksInString = taskString.split("\n");
        Type type;
        for (int i = 1; i < splitTasksInString.length; i++) {
            String[] splitValue = splitTasksInString[i].split(",");
            Integer id = Integer.parseInt(splitValue[0]);
            String name = splitValue[2];
            Status status = setTaskType(splitValue[3]);
            String description = splitValue[4];
            if (splitValue[1].equals("TASK")) {
                type = Type.TASK;
                NormalTask normalTask = new NormalTask(type, name, status, description);
                normalTask.setId(id);
                normalTasks.put(normalTask.getId(), normalTask);
                if (taskId <= normalTask.getId()) {
                    taskId = normalTask.getId() + 1;
                }
            } else if (splitValue[1].equals("EPIC")) {
                type = Type.EPIC;
                EpicTask epicTask = new EpicTask(type, name, status, description);
                epicTask.setId(id);
                epicTasks.put(epicTask.getId(), epicTask);
                if (taskId < epicTask.getId()) {
                    taskId = epicTask.getId() + 1;
                }
            } else {
                type = Type.SUBTASK;
                int epicId = Integer.parseInt(splitValue[5]);
                SubTask subTask = new SubTask(type, name, status, description, epicId);
                subTask.setId(id);
                SubTasks.put(subTask.getId(), subTask);
                if (taskId < subTask.getId()) {
                    taskId = subTask.getId() + 1;
                }
            }
        }
    }

    void historyFromString(String historyInString) {
        String[] history = historyInString.split(",");
        for (String indexTask : history) {
            Integer index = Integer.parseInt(indexTask);
            if (normalTasks.containsKey(index)) {
                historyManager.add(normalTasks.get(index));
            } else if (epicTasks.containsKey(index)) {
                historyManager.add(epicTasks.get(index));
            } else if (SubTasks.containsKey(index)) {
                historyManager.add(SubTasks.get(index));
            }
        }
    }

    @Override
    public void printAllMaps(HashMap<Integer, NormalTask> map) {
        super.printAllMaps(map);
    }

    @Override
    public void createNewTask(NormalTask normalTask) {
        super.createNewTask(normalTask);
        save();
    }

    @Override
    public void createNewEpic(EpicTask epicTask) {
        super.createNewEpic(epicTask);
        save();
    }

    @Override
    public void createNewSubtask(SubTask subTask, Integer epicId) {
        super.createNewSubtask(subTask, epicId);
        save();
    }

    @Override
    public void updateTask(Integer taskId, NormalTask normalTask) {
        super.updateTask(taskId, normalTask);
        save();
    }

    @Override
    public void updateEpic(Integer taskId, EpicTask epicTask) {
        super.updateEpic(taskId, epicTask);
        save();
    }

    @Override
    public void updateSubtask(Integer taskId, SubTask subTask) {
        super.updateSubtask(taskId, subTask);
        save();
    }

    @Override
    public void updateEpicStatus(Integer epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public NormalTask getTaskById(Integer taskId) {
        historyManager.add(normalTasks.get(taskId));
        save();
        return normalTasks.get(taskId);
    }

    @Override
    public EpicTask getEpicById(Integer taskId) {
        historyManager.add(epicTasks.get(taskId));
        save();
        return epicTasks.get(taskId);
    }

    @Override
    public SubTask getSubtaskById(Integer taskId) {
        historyManager.add(SubTasks.get(taskId));
        save();
        return SubTasks.get(taskId);
    }

    @Override
    public HashMap<Integer, SubTask> getSubtaskByEpic(Integer epicId) {
        return super.getSubtaskByEpic(epicId);
    }

    @Override
    public void deleteTaskById(Integer taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(Integer taskId) {
        super.deleteEpicById(taskId);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer taskId) {
        super.deleteSubtaskById(taskId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public List<NormalTask> getHistory() {
        return super.getHistory();
    }
}