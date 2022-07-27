import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        TasksManager tasksManager = new TasksManager();

        //СОЗДАНИЕ
        Task taskRemoval = new Task("Переезд", "Описание переезда", "NEW");
        tasksManager.addTask(taskRemoval);
        Task taskShop = new Task("Купить еды", "Описание покупки еды", "NEW");
        tasksManager.addTask(taskShop);

        Epic epicSea = new Epic("Поехать на море", "Описание поездки на море", "");
        tasksManager.addEpic(epicSea);
        Epic epicCar = new Epic("Отремонтировать машину", "Описание ремонта машины", "");
        tasksManager.addEpic(epicCar);

        SubTask subTaskShop = new SubTask("Найти купальник", "Описание купальника", "DONE",
                3);
        tasksManager.addSubTask(subTaskShop);
        SubTask subTaskBuy = new SubTask("Купить круг", "Описание покупки круга", "IN_PROGRESS",
                3);
        tasksManager.addSubTask(subTaskBuy);
        SubTask subTaskCar = new SubTask("Купить колесо", "Описание покупки колеса", "NEW",
                4);
        tasksManager.addSubTask(subTaskCar);


        //ПОЛУЧЕНИЕ СПИСКА ВСЕХ ЗАДАЧ
        System.out.println("Получить список Task: ");
        System.out.println(tasksManager.getTasks());

        System.out.println("Получить список Epic: ");
        System.out.println(tasksManager.getEpics());

        System.out.println("Получить список SubTask: ");
        System.out.println(tasksManager.getSubTasks());


        //ОБНОВЛЕНИЕ ДАННЫХ
        System.out.println("Какой Task обновить? ");
        int taskId = scanner.nextInt();
        Task taskUpdate = new Task("Купить подарок", "Описание покупки подарка", "IN_PROGRESS");
        taskUpdate.setId(taskId);
        tasksManager.updateTask(taskUpdate);

        System.out.println("Получить список Task: ");
        System.out.println(tasksManager.getTasks());

        System.out.println("Какой Epic обновить? ");
        int epicId = scanner.nextInt();
        ArrayList<Integer> subTaskList = tasksManager.getEpic(epicId).getSubTasks();
        Epic epicUpdate = new Epic("Построить дом", "Описание стройки", "");
        epicUpdate.setId(epicId);
        for (Integer epic : subTaskList) {
            epicUpdate.addSubTaskToEpic(epic);
        }
        tasksManager.updateEpic(epicUpdate);

        System.out.println("Получить список Epic: ");
        System.out.println(tasksManager.getEpics());

        System.out.println("Какой SubTask обновить? ");
        int subTaskId = scanner.nextInt();
        SubTask subTaskUpdate = new SubTask("Выбрать онлайн-школу", "Описание", "IN_PROGRESS", 4);
        subTaskUpdate.setId(subTaskId);
        tasksManager.updateSubTask(subTaskUpdate);

        System.out.println("Получить список SubTask: ");
        System.out.println(tasksManager.getSubTasks());

        System.out.println("Получить список Epic: ");
        System.out.println(tasksManager.getEpics());


        //УДАЛЕНИЕ ПО ИДЕНТИФИКАТОРУ
        System.out.println("Какой Task удалить? ");
        int delTask = scanner.nextInt();
        tasksManager.delTask(delTask);

        System.out.println("Получить список Task: ");
        System.out.println(tasksManager.getTasks());

        System.out.println("Какой Epic удалить? ");
        int delEpic = scanner.nextInt();
        tasksManager.delEpic(delEpic);

        System.out.println("Получить список Epic: ");
        System.out.println(tasksManager.getEpics());

        System.out.println("Получить список SubTask: ");
        System.out.println(tasksManager.getSubTasks());

        System.out.println("Какой SubTask удалить? ");
        int delSubTask = scanner.nextInt();
        tasksManager.delSubTask(delSubTask);

        System.out.println("Получить список Epic: ");
        System.out.println(tasksManager.getEpics());

        System.out.println("Получить список SubTask: ");
        System.out.println(tasksManager.getSubTasks());


        //ПОЛУЧЕНИЕ СПИСКА ВСЕХ ПОДЗАДАЧ ОПРЕДЕЛЕННОГО ЭПИКА
        System.out.println("Задачи какого Epic вы хотите получить? ");
        int epicIdSubTask = scanner.nextInt();
        System.out.println(tasksManager.getEpicSubTasks(epicIdSubTask));
    }
}
