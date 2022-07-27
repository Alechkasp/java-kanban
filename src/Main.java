import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

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
        System.out.println("Получить список Task:");
        System.out.println(tasksManager.getTasks());

        System.out.println("Получить список Epic:");
        System.out.println(tasksManager.getEpics());

        System.out.println("Получить список SubTask:");
        System.out.println(tasksManager.getSubTasks());


        //ОБНОВЛЕНИЕ ДАННЫХ
        System.out.println("Обновить Task 2");
        Task taskUpdate = new Task("Купить подарок", "Описание покупки подарка", "IN_PROGRESS");
        taskUpdate.setId(2);
        tasksManager.updateTask(taskUpdate);

        System.out.println("Получить список Task:");
        System.out.println(tasksManager.getTasks());

        System.out.println("Обновить Epic 4");
        ArrayList<Integer> subTaskList = tasksManager.getEpic(4).getSubTasks();
        Epic epicUpdate = new Epic("Построить дом", "Описание стройки", "");
        epicUpdate.setId(4);
        for (Integer epic : subTaskList) {
            epicUpdate.addSubTaskToEpic(epic);
        }
        tasksManager.updateEpic(epicUpdate);

        System.out.println("Получить список Epic:");
        System.out.println(tasksManager.getEpics());

        System.out.println("Обновить SubTask 7");
        SubTask subTaskUpdate = new SubTask("Выбрать онлайн-школу", "Описание", "IN_PROGRESS", 4);
        subTaskUpdate.setId(7);
        tasksManager.updateSubTask(subTaskUpdate);

        System.out.println("Получить список SubTask:");
        System.out.println(tasksManager.getSubTasks());

        System.out.println("Получить список Epic:");
        System.out.println(tasksManager.getEpics());


        //УДАЛЕНИЕ ПО ИДЕНТИФИКАТОРУ
        System.out.println("Удалить Task 2");
        tasksManager.delTask(2);

        System.out.println("Получить список Task:");
        System.out.println(tasksManager.getTasks());

        System.out.println("Удалить Epic 4");
        tasksManager.delEpic(4);

        System.out.println("Получить список Epic:");
        System.out.println(tasksManager.getEpics());

        System.out.println("Получить список SubTask:");
        System.out.println(tasksManager.getSubTasks());

        System.out.println("Удалить SubTask 6");
        tasksManager.delSubTask(6);

        System.out.println("Получить список Epic:");
        System.out.println(tasksManager.getEpics());

        System.out.println("Получить список SubTask:");
        System.out.println(tasksManager.getSubTasks());


        //ПОЛУЧЕНИЕ СПИСКА ВСЕХ ПОДЗАДАЧ ОПРЕДЕЛЕННОГО ЭПИКА
        System.out.println("Получить задачи Epic 3:");
        System.out.println(tasksManager.getEpicSubTasks(3));
    }
}