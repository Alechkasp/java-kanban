import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //TaskManager taskManager = new TaskManager();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        //СОЗДАНИЕ
        Task taskRemoval = new Task("Переезд", "Описание переезда", Status.NEW);
        inMemoryTaskManager.addTask(taskRemoval);
        Task taskShop = new Task("Купить еды", "Описание покупки еды", Status.NEW);
        inMemoryTaskManager.addTask(taskShop);

        Epic epicSea = new Epic("Поехать на море", "Описание поездки на море", Status.NEW);
        inMemoryTaskManager.addEpic(epicSea);
        Epic epicCar = new Epic("Отремонтировать машину", "Описание ремонта машины", Status.NEW);
        inMemoryTaskManager.addEpic(epicCar);

        SubTask subTaskShop = new SubTask("Найти купальник", "Описание купальника", Status.DONE,
                3);
        inMemoryTaskManager.addSubTask(subTaskShop);
        SubTask subTaskBuy = new SubTask("Купить круг", "Описание покупки круга", Status.IN_PROGRESS,
                3);
        inMemoryTaskManager.addSubTask(subTaskBuy);
        SubTask subTaskCar = new SubTask("Купить колесо", "Описание покупки колеса", Status.NEW,
                4);
        inMemoryTaskManager.addSubTask(subTaskCar);


        //ПОЛУЧЕНИЕ СПИСКА ВСЕХ ЗАДАЧ
        System.out.println("Получить список Task:");
        System.out.println(inMemoryTaskManager.getTasks());

        System.out.println("Получить список Epic:");
        System.out.println(inMemoryTaskManager.getEpics());

        System.out.println("Получить список SubTask:");
        System.out.println(inMemoryTaskManager.getSubTasks());


        //ОБНОВЛЕНИЕ ДАННЫХ
/*        System.out.println("Обновить Task 2");
        Task taskUpdate = new Task("Купить подарок", "Описание покупки подарка", Status.IN_PROGRESS);
        taskUpdate.setId(2);
        inMemoryTaskManager.updateTask(taskUpdate);

        System.out.println("Получить список Task:");
        System.out.println(inMemoryTaskManager.getTasks());

        System.out.println("Обновить Epic 4");
        ArrayList<Integer> subTaskList = inMemoryTaskManager.getEpic(4).getSubTasks();
        Epic epicUpdate = new Epic("Построить дом", "Описание стройки", Status.NEW);
        epicUpdate.setId(4);
        for (Integer epic : subTaskList) {
            epicUpdate.addSubTaskToEpic(epic);
        }
        inMemoryTaskManager.updateEpic(epicUpdate);

        System.out.println("Получить список Epic:");
        System.out.println(inMemoryTaskManager.getEpics());

        System.out.println("Обновить SubTask 7");
        SubTask subTaskUpdate = new SubTask("Выбрать онлайн-школу", "Описание", Status.IN_PROGRESS,
                4);
        subTaskUpdate.setId(7);
        inMemoryTaskManager.updateSubTask(subTaskUpdate);

        System.out.println("Получить список SubTask:");
        System.out.println(inMemoryTaskManager.getSubTasks());

        System.out.println("Получить список Epic:");
        System.out.println(inMemoryTaskManager.getEpics());*/


        //УДАЛЕНИЕ ПО ИДЕНТИФИКАТОРУ
/*        System.out.println("Удалить Task 2");
        inMemoryTaskManager.delTask(2);

        System.out.println("Получить список Task:");
        System.out.println(inMemoryTaskManager.getTasks());

        System.out.println("Удалить Epic 4");
        inMemoryTaskManager.delEpic(4);

        System.out.println("Получить список Epic:");
        System.out.println(inMemoryTaskManager.getEpics());

        System.out.println("Получить список SubTask:");
        System.out.println(inMemoryTaskManager.getSubTasks());

        System.out.println("Удалить SubTask 6");
        inMemoryTaskManager.delSubTask(6);

        System.out.println("Получить список Epic:");
        System.out.println(inMemoryTaskManager.getEpics());

        System.out.println("Получить список SubTask:");
        System.out.println(inMemoryTaskManager.getSubTasks());*/


        //ПОЛУЧЕНИЕ СПИСКА ВСЕХ ПОДЗАДАЧ ОПРЕДЕЛЕННОГО ЭПИКА
/*        System.out.println("Получить задачи Epic 3:");
        System.out.println(inMemoryTaskManager.getEpicSubTasks(3));*/

        //ПОЛУЧЕНИЕ ПО ИДЕНТИФИКАТОРУ
        System.out.println("Получить Task:");
        System.out.println(inMemoryTaskManager.getTask(1)); //1
        System.out.println(inMemoryTaskManager.getTask(2)); //2
        System.out.println(inMemoryTaskManager.getTask(2)); //3
        System.out.println(inMemoryTaskManager.getTask(2)); //4
        System.out.println(inMemoryTaskManager.getTask(2)); //5
        System.out.println(inMemoryTaskManager.getTask(2)); //6
        System.out.println(inMemoryTaskManager.getTask(2)); //7
        System.out.println(inMemoryTaskManager.getTask(2)); //8
        System.out.println(inMemoryTaskManager.getTask(1)); //9
        System.out.println(inMemoryTaskManager.getTask(2)); //10
        System.out.println(inMemoryTaskManager.getTask(1)); //11

        System.out.println("Получить Epic:");
        System.out.println(inMemoryTaskManager.getEpic(3)); //12
        System.out.println(inMemoryTaskManager.getEpic(4)); //13


        System.out.println("Получить SubTask:");
        System.out.println(inMemoryTaskManager.getSubTask(5)); //14

        System.out.println("Получить Task:");
        System.out.println(inMemoryTaskManager.getTask(1)); //15

        //Получить список последних просмотренных задач
        System.out.println("Последние просмотренные задачи: ");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("Размер: " + inMemoryTaskManager.getHistory().size());
    }
}