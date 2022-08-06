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
        Task taskRemoval = new Task("Переезд", "Описание переезда", Status.NEW);
        taskManager.addTask(taskRemoval);
        Task taskShop = new Task("Купить еды", "Описание покупки еды", Status.NEW);
        taskManager.addTask(taskShop);

        Epic epicSea = new Epic("Поехать на море", "Описание поездки на море", Status.NEW);
        taskManager.addEpic(epicSea);
        Epic epicCar = new Epic("Отремонтировать машину", "Описание ремонта машины", Status.NEW);
        taskManager.addEpic(epicCar);

        SubTask subTaskShop = new SubTask("Найти купальник", "Описание купальника", Status.DONE,
                3);
        taskManager.addSubTask(subTaskShop);
        SubTask subTaskBuy = new SubTask("Купить круг", "Описание покупки круга", Status.IN_PROGRESS,
                3);
        taskManager.addSubTask(subTaskBuy);
        SubTask subTaskCar = new SubTask("Купить колесо", "Описание покупки колеса", Status.NEW,
                4);
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
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(1));

        System.out.println("Получить Epic:");
        System.out.println(taskManager.getEpic(3));
        System.out.println(taskManager.getEpic(4));

        System.out.println("Получить SubTask:");
        System.out.println(taskManager.getSubTask(5));

        System.out.println("Получить Task:");
        System.out.println(taskManager.getTask(1));

        //Получить список последних просмотренных задач
        System.out.println("Последние просмотренные задачи: ");
        System.out.println(taskManager.getHistory());
        System.out.println("Размер списка: " + taskManager.getHistory().size());
    }
}