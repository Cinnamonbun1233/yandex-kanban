import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

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

        SubTask subTask1 = new SubTask("Помидоры", "Только спелые", Status.NEW, 3);
        SubTask subTask2 = new SubTask("Анна Каренина", "Взять у сестры", Status.NEW, 4);
        SubTask subTask3 = new SubTask("Бесы", "Взять в библиотеке", Status.NEW, 4);
        taskManager.createNewSubtask(subTask1, epic1.getID());
        taskManager.createNewSubtask(subTask2, epic2.getID());
        taskManager.createNewSubtask(subTask3, epic2.getID());

        taskManager.viewAllTask();

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Проверяем методы обновления задач, эпиков и подзадач");

        Task task3 = new Task("Погладить рубашки", "Погладить рубашки для отпуска", Status.NEW);
        taskManager.updateTask(task1.getID(), task3);

        Epic epic3 = new Epic("Продукты", "Сходить в магазин", Status.NEW);
        taskManager.updateEpic(epic1.getID(), epic3);

        SubTask subTask4 = new SubTask("Братья Карамазовы>", "Взять в библиотеке", Status.IN_PROGRESS, 4);
        taskManager.updateSubtask(subTask3.getID(), subTask4);

        taskManager.viewAllTask();

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
        taskManager.deleteSubTaskById(7);

        taskManager.viewAllTask();

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Проверяем методы удаления всех задач, эпиков и подзадач");

        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubTasks();

        taskManager.viewAllTask();
    }
}