import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

public class Main {

    public static void main(String[] args) {

        /* Что-то я совсем поплыл в последнем спринте. Буду все каникулы разбираться в приницпах ООП, понял в лучшем
        случае половину материала. Чувствую, что сделал в ДЗ что-то неправильно, но не могу понять, что и именно.
         */

        TaskManager taskManager = Managers.getDefault();

        System.out.println("Тестирование методов");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Проверяем методы добавления задач, эпиков и подзадач");

        Task task1 = new Task("Погладить рубашки", "Погладить рубашки для работы", Status.NEW);
        Task task2 = new Task("Протереть пыль", "Протереть пыль в спальне", Status.NEW);
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);

        Epic epic1 = new Epic("Продукты", "Сходить в магазин и купить продукты", Status.NEW);
        Epic epic2 = new Epic("Книги", "Список книг для чтения", Status.NEW);
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);

        Subtask subtask1 = new Subtask("Помидоры", "Только спелые", Status.NEW, 3);
        Subtask subtask2 = new Subtask("Анна Каренина", "Взять у сестры", Status.NEW, 4);
        Subtask subtask3 = new Subtask("Бесы", "Взять в библиотеке", Status.NEW, 4);
        taskManager.createNewSubtask(subtask1, epic1.getId());
        taskManager.createNewSubtask(subtask2, epic2.getId());
        taskManager.createNewSubtask(subtask3, epic2.getId());

        taskManager.printAllMaps();

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Тестирование проверки истории");

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        System.out.println(taskManager.getHistory());

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Проверяем методы обновления задач, эпиков и подзадач");

        Task task3 = new Task("Погладить рубашки", "Погладить рубашки для отпуска", Status.NEW);
        taskManager.updateTask(task1.getId(), task3);
        Epic epic3 = new Epic("Продукты", "Сходить в магазин", Status.NEW);
        taskManager.updateEpic(epic1.getId(), epic3);
        Subtask subtask4 =
                new Subtask("Братья Карамазовы>", "Взять в библиотеке", Status.IN_PROGRESS, 4);
        taskManager.updateSubtask(subtask3.getId(), subtask4);

        taskManager.printAllMaps();

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Проверяем методы получения задач, эпиков и подзадач");

        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println(taskManager.getSubtaskByEpic(3));

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Проверяем методы удаления задач, эпиков и подзадач по ID");

        taskManager.deleteTaskById(1);
        taskManager.deleteEpicById(3);
        taskManager.deleteSubtaskById(7);

        taskManager.printAllMaps();

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Проверяем методы удаления всех задач, эпиков и подзадач");

        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();

        taskManager.printAllMaps();
    }
}