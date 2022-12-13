import managers.Managers;
import managers.TaskManager;
import network.HttpTaskServer;
import network.KVServer;
import tasks.*;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("_________________________________________________________________________________________");
        System.out.println("запускаем сервер");
        new KVServer().start();
        URI uriKVServer = KVServer.getServerURL();
        System.out.println("_________________________________________________________________________________________");
        System.out.println("вызываем менеджера и загрузки задач с сервера");
        TaskManager manager = Managers.getDefault(uriKVServer);
        System.out.println("_________________________________________________________________________________________");
        System.out.println("запускаем http сервер менеджера");
        new HttpTaskServer(manager).start();
        System.out.println("_________________________________________________________________________________________");
        System.out.println("создаем задачи");
        Task task1 = new Task(Type.TASK, "Тестовый таск 1", Status.NEW,
                LocalDateTime.of(2001, 1, 1, 1, 1, 1),
                Duration.ofMinutes(20), "Описание тестового таска 1");
        Task task2 = new Task(Type.TASK, "Тестовый таск 2", Status.IN_PROGRESS,
                LocalDateTime.of(2006, 1, 1, 1, 1, 1),
                Duration.ofMinutes(20), "Описание тестового таска 2");
        Epic epic3 = new Epic(Type.EPIC, "Тестовый эпик 1", "Описание тестового эпика 1", Status.NEW);
        Epic epic4 = new Epic(Type.EPIC, "Тестовый эпик 2", "Описание тестового эпика 2", Status.NEW);
        SubTask subtask5 = new SubTask(Type.SUBTASK, "Тестовый сабтаск 1", Status.DONE,
                LocalDateTime.of(2009, 1, 1, 1, 1, 1),
                Duration.ofMinutes(20), "Описание тестового сабтаска 1", 2);
        System.out.println("_________________________________________________________________________________________");
        System.out.println("добавление task1");
        manager.addTask(task1);
        System.out.println("_________________________________________________________________________________________");
        System.out.println("добавление epic2");
        manager.addEpic(epic3);
        System.out.println("_________________________________________________________________________________________");
        System.out.println("добавление subtask3");
        manager.addSubTask(subtask5);
        System.out.println("_________________________________________________________________________________________");
        System.out.println("обновление task1");
        manager.updateTask(1, task2);
        System.out.println("_________________________________________________________________________________________");
        System.out.println("обновление epic2");
        manager.updateEpic(1, epic4);
        System.out.println("_________________________________________________________________________________________");
        System.out.println("вызов task1");
        System.out.println(manager.getTaskById(1));
        System.out.println("_________________________________________________________________________________________");
        System.out.println("вызов epic2");
        System.out.println(manager.getEpicById(2));
        System.out.println("_________________________________________________________________________________________");
        System.out.println("проверка истории" + '\n' + manager.getTaskHistory());
    }
}