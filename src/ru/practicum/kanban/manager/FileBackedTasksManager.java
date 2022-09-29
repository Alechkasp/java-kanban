package ru.practicum.kanban.manager;

import ru.practicum.kanban.models.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public FileBackedTasksManager() {
    }

    public FileBackedTasksManager(int id, Map<Integer, Task> tableTasks, Map<Integer, Epic> tableEpics,
                                  Map<Integer, SubTask> tableSubTasks, InMemoryHistoryManager inMemoryHistoryManager) {
        this.id = id;
        this.tableTasks = tableTasks;
        this.tableEpics = tableEpics;
        this.tableSubTasks = tableSubTasks;
        this.inMemoryHistoryManager = inMemoryHistoryManager;
    }

    private String lineSeparator = System.lineSeparator();
    static final Path path = Path.of("resources/file.csv");

    public void save(Path path) {
        try (FileWriter fileWriter = new FileWriter(path.toFile(), false)) {
            StringBuilder builder = new StringBuilder();
            builder.append("id,type,name,status,description,epic" + lineSeparator);
            for (Task t : getTasks()) {
                builder.append(t.toString() + lineSeparator);
            }
            for (Epic e : getEpics()) {
                builder.append(e.toString() + lineSeparator);
            }
            for (SubTask s : getSubTasks()) {
                builder.append(s.toString() + lineSeparator);
            }
            builder.append(lineSeparator);
            builder.append(historyToString(inMemoryHistoryManager));
            String asString = builder.toString();
            fileWriter.write(asString);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл");
        }
    }

    //метод сохранения менеджера истории из CVS
    static String historyToString(HistoryManager manager) {
        String history = "";
        for (Task task: manager.getHistory()) {
            String taskString = task.toString();
            String[] split = taskString.split(",");
            if (history.length() == 0) {
                history = split[0];
            } else if (history.length() > 0) {
                history = split[0] + "," + history;
            }
        }
        return history;
    }

    //метод восстановления данных менеджера из файла при запуске программы
    static FileBackedTasksManager loadFromFile(Path path) {
        int id = 0;
        Map<Integer, Task> tableTasks = new HashMap<>();
        Map<Integer, Epic> tableEpics = new HashMap<>();
        Map<Integer, SubTask> tableSubTasks = new HashMap<>();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

        try {
            String file = Files.readString(path);
            String[] rows = file.split(System.lineSeparator());
            for (int i = 1; i < rows.length - 2; i++) {
                String[] s = rows[i].split(",");
                if (s[1].equals("TASK")) {
                    tableTasks.put(fromStringTask(rows[i]).getId(), fromStringTask(rows[i]));
                }
                if (s[1].equals("EPIC")) {
                    tableEpics.put(fromStringEpic(rows[i]).getId(), fromStringEpic(rows[i]));
                }
                if (s[1].equals("SUBTASK")) {
                    tableSubTasks.put(fromStringSubTask(rows[i]).getId(), fromStringSubTask(rows[i]));
                }
            }

            for (Task t : tableTasks.values()) {
                if (id < t.getId()) {
                    id = t.getId();
                }
            }
            for (Task t : tableEpics.values()) {
                if (id < t.getId()) {
                    id = t.getId();
                }
            }
            for (Task t : tableSubTasks.values()) {
                if (id < t.getId()) {
                    id = t.getId();
                }
            }

            for (Integer i : historyFromString(rows[rows.length - 1])) {
                for (Task t : tableTasks.values()) {
                    if (t.getId() == i) {
                        inMemoryHistoryManager.add(t);
                    }
                }
                for (Epic e : tableEpics.values()) {
                    if (e.getId() == i) {
                        inMemoryHistoryManager.add(e);
                    }
                }
                for (SubTask s : tableSubTasks.values()) {
                    if (s.getId() == i) {
                        inMemoryHistoryManager.add(s);
                    }
                }
            }

            System.out.println(tableTasks);
            System.out.println(tableEpics);
            System.out.println(tableSubTasks);

            System.out.println(historyFromString(rows[rows.length - 1]));

            return new FileBackedTasksManager(id, tableTasks, tableEpics, tableSubTasks, inMemoryHistoryManager);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при восстановлении данных из файла");
        }
    }

    //метод создания задачи из строки, если задача типа Task
    static Task fromStringTask(String value) {
        String[] taskString = value.split(",");
        if (taskString[1].equals("TASK")) {
            Task task = new Task(TypeOfTask.TASK, taskString[2], taskString[4], statusDetermination(value));
            task.setId(Integer.parseInt(taskString[0]));
            return task;
        }
        return null;
    }

    //метод создания задачи из строки, если задача типа Epic
    static Epic fromStringEpic(String value) {
        String[] taskString = value.split(",");
        if (taskString[1].equals("EPIC")) {
            Epic epic = new Epic(TypeOfTask.EPIC, taskString[2], taskString[4], statusDetermination(value));
            epic.setId(Integer.parseInt(taskString[0]));
            return epic;
        }
        return null;
    }

    //метод создания задачи из строки, если задача типа SubTask
    static SubTask fromStringSubTask(String value) {
        String[] taskString = value.split(",");
        if (taskString[1].equals("SUBTASK")) {
            SubTask subTask = new SubTask(TypeOfTask.SUBTASK, taskString[2], taskString[4], statusDetermination(value),
                        Integer.parseInt(taskString[5]));
            subTask.setId(Integer.parseInt(taskString[0]));
            return subTask;
        }
        return null;
    }

    //метод определения статуса
    static Status statusDetermination(String value) {
        String[] taskString = value.split(",");
        switch (taskString[3]) {
            case "NEW":
                return Status.NEW;
            case "IN_PROGRESS":
                return Status.IN_PROGRESS;
            case "DONE":
                return Status.DONE;
            default:
                System.out.println("Такого статуса нет");
                return null;
        }
    }

    //метод восстановления менеджера истории из CVS
    static List<Integer> historyFromString(String value) {
        List<Integer> list = new ArrayList<>();
        for (String s : value.split(",")) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    @Override
    public Task addTask(Task task) {
        Task t = super.addTask(task);
        save(path);
        return t;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic e = super.addEpic(epic);
        save(path);
        return e;
    }

    @Override
    public SubTask addSubTask(SubTask subTask) {
        SubTask s = super.addSubTask(subTask);
        save(path);
        return s;
    }

    @Override
    public Task getTask(int id) {
        Task t = super.getTask(id);
        save(path);
        return t;
    }

    @Override
    public Epic getEpic(int id) {
        Epic e = super.getEpic(id);
        save(path);
        return e;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask s = super.getSubTask(id);
        save(path);
        return s;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save(path);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save(path);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save(path);
    }

    @Override
    public void delTask(int id) {
        super.delTask(id);
        save(path);
    }

    @Override
    public void delEpic(int id) {
        super.delEpic(id);
        save(path);
    }

    @Override
    public void delSubTask(int id) {
        super.delSubTask(id);
        save(path);
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();

        //СОЗДАНИЕ
        Task task1 = new Task(TypeOfTask.TASK, "Новая задача 1", "Описание первой задачи", Status.NEW);
        fileBackedTasksManager.addTask(task1);
        Task task2 = new Task(TypeOfTask.TASK, "Новая задача 2", "Описание второй задачи", Status.NEW);
        fileBackedTasksManager.addTask(task2);

        Epic epic3 = new Epic(TypeOfTask.EPIC, "Новый эпик 1", "Описание первого эпика", Status.NEW);
        fileBackedTasksManager.addEpic(epic3);
        Epic epic4 = new Epic(TypeOfTask.EPIC, "Новый эпик 2", "Описание второго эпика", Status.NEW);
        fileBackedTasksManager.addEpic(epic4);

        SubTask subTaskShop = new SubTask(TypeOfTask.SUBTASK, "Новый сабтаск 1", "Описание первого сабтаска",
                Status.DONE, 3);
        fileBackedTasksManager.addSubTask(subTaskShop);
        SubTask subTaskBuy = new SubTask(TypeOfTask.SUBTASK, "Новый сабтаск 2", "Описание второго сабтаска",
                Status.IN_PROGRESS, 3);
        fileBackedTasksManager.addSubTask(subTaskBuy);
        SubTask subTaskCar = new SubTask(TypeOfTask.SUBTASK, "Новый сабтаск 3", "Описание третьего сабтаска",
                Status.NEW, 3);
        fileBackedTasksManager.addSubTask(subTaskCar);

        //ПОЛУЧЕНИЕ ПО ИДЕНТИФИКАТОРУ
        System.out.println("Получить Task:");
        System.out.println(fileBackedTasksManager.getTask(1));
        System.out.println(fileBackedTasksManager.getTask(1));
        System.out.println(fileBackedTasksManager.getTask(2));
        System.out.println(fileBackedTasksManager.getTask(2));
        System.out.println(fileBackedTasksManager.getTask(1));

        System.out.println("Получить Epic:");
        System.out.println(fileBackedTasksManager.getEpic(3));
        System.out.println(fileBackedTasksManager.getEpic(4));
        System.out.println(fileBackedTasksManager.getEpic(3));

        System.out.println("Получить SubTask:");
        System.out.println(fileBackedTasksManager.getSubTask(5));
        System.out.println(fileBackedTasksManager.getSubTask(6));
        System.out.println(fileBackedTasksManager.getSubTask(7));
        System.out.println(fileBackedTasksManager.getSubTask(6));

        System.out.println("Получить Task:");
        System.out.println(fileBackedTasksManager.getTask(1));

        //Получить список последних просмотренных задач
        System.out.println("Последние просмотренные задачи: ");
        System.out.println(fileBackedTasksManager.getHistory());
        System.out.println("Размер списка: " + fileBackedTasksManager.getHistory().size());

        //УДАЛЕНИЕ ПО ИДЕНТИФИКАТОРУ
        fileBackedTasksManager.delTask(2);
        fileBackedTasksManager.delEpic(4);

        //Получить список последних просмотренных задач
        System.out.println("Последние просмотренные задачи: ");
        System.out.println(fileBackedTasksManager.getHistory());
        System.out.println("Размер списка: " + fileBackedTasksManager.getHistory().size());

        Path filePath = Path.of("resources/file.csv");
        loadFromFile(filePath);
    }
}