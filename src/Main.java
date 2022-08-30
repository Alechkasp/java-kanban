import ru.practicum.kanban.manager.Managers;
import ru.practicum.kanban.manager.TaskManager;
import ru.practicum.kanban.models.Epic;
import ru.practicum.kanban.models.Status;
import ru.practicum.kanban.models.SubTask;
import ru.practicum.kanban.models.Task;

public class Main {

    public static void main(String[] args) {

        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();


        //СОЗДАНИЕ
        Task taskRemoval = new Task("Задача 1", "Описание первой задачи", Status.NEW);
        taskManager.addTask(taskRemoval);
        Task taskShop = new Task("Задача 2", "Описание второй задачи", Status.NEW);
        taskManager.addTask(taskShop);

        Epic epicSea = new Epic("Эпик 1", "Описание первого эпика", Status.NEW);
        taskManager.addEpic(epicSea);
        Epic epicCar = new Epic("Эпик 2", "Описание второго эпика", Status.NEW);
        taskManager.addEpic(epicCar);

        SubTask subTaskShop = new SubTask("Сабтаск 1", "Описание первого сабтаска", Status.DONE,
                3);
        taskManager.addSubTask(subTaskShop);
        SubTask subTaskBuy = new SubTask("Сабтаск 2", "Описание второго сабтаска", Status.IN_PROGRESS,
                3);
        taskManager.addSubTask(subTaskBuy);
        SubTask subTaskCar = new SubTask("Сабтаск 3", "Описание третьего сабтаска", Status.NEW,
                3);
        taskManager.addSubTask(subTaskCar);


        //ПОЛУЧЕНИЕ СПИСКА ВСЕХ ЗАДАЧ
        System.out.println("Получить список Task:");
        System.out.println(taskManager.getTasks());

        System.out.println("Получить список Epic:");
        System.out.println(taskManager.getEpics());

        System.out.println("Получить список SubTask:");
        System.out.println(taskManager.getSubTasks());


        //ПОЛУЧЕНИЕ ПО ИДЕНТИФИКАТОРУ
        System.out.println("Получить Task:");
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(1));

        System.out.println("Получить Epic:");
        System.out.println(taskManager.getEpic(3));
        System.out.println(taskManager.getEpic(4));
        System.out.println(taskManager.getEpic(3));

        System.out.println("Получить SubTask:");
        System.out.println(taskManager.getSubTask(5));
        System.out.println(taskManager.getSubTask(6));
        System.out.println(taskManager.getSubTask(7));
        System.out.println(taskManager.getSubTask(6));

        System.out.println("Получить Task:");
        System.out.println(taskManager.getTask(1));

        //Получить список последних просмотренных задач
        System.out.println("Последние просмотренные задачи: ");
        System.out.println(taskManager.getHistory());
        System.out.println("Размер списка: " + taskManager.getHistory().size());

        //УДАЛЕНИЕ ПО ИДЕНТИФИКАТОРУ
        taskManager.delTask(2);
        taskManager.delEpic(3);

        //Получить список последних просмотренных задач
        System.out.println("Последние просмотренные задачи: ");
        System.out.println(taskManager.getHistory());
        System.out.println("Размер списка: " + taskManager.getHistory().size());
    }
}