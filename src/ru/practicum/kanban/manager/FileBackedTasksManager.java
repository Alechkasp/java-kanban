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

    public FileBackedTasksManager(Map<Integer, Task> tableTasks, Map<Integer, Epic> tableEpics,
                                  Map<Integer, SubTask> tableSubTasks) {
        this.tableTasks = tableTasks;
        this.tableEpics = tableEpics;
        this.tableSubTasks = tableSubTasks;
    }

    private String lineSeparator = System.lineSeparator();

    public void save() {
        try (FileWriter fileWriter = new FileWriter("C:\\Users\\Aliya\\git\\java-kanban\\resources\\file.csv",
                false)) {
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
        Map<Integer, Task> tableTasks = new HashMap<>();
        Map<Integer, Epic> tableEpics = new HashMap<>();
        Map<Integer, SubTask> tableSubTasks = new HashMap<>();

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
            System.out.println(tableTasks);
            System.out.println(tableEpics);
            System.out.println(tableSubTasks);

            System.out.println(historyFromString(rows[rows.length - 1]));

            return new FileBackedTasksManager(tableTasks, tableEpics, tableSubTasks);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при восстановлении данных из файла");
        }
    }

    //метод создания задачи из строки, если задача типа Task
    static Task fromStringTask(String value) {
        String[] taskString = value.split(",");
        if (taskString[1].equals("TASK")) {
            if (taskString[3].equals("NEW")) {
                Task task = new Task(TypeOfTask.TASK, taskString[2], taskString[4], Status.NEW);
                task.setId(Integer.parseInt(taskString[0]));
                return task;
            }
            if (taskString[3].equals("IN_PROGRESS")) {
                Task task = new Task(TypeOfTask.TASK, taskString[2], taskString[4], Status.IN_PROGRESS);
                task.setId(Integer.parseInt(taskString[0]));
                return task;
            }
            if (taskString[3].equals("DONE")) {
                Task task = new Task(TypeOfTask.TASK, taskString[2], taskString[4], Status.DONE);
                task.setId(Integer.parseInt(taskString[0]));
                return task;
            }
        }
        return null;
    }

    //метод создания задачи из строки, если задача типа Epic
    static Epic fromStringEpic(String value) {
        String[] taskString = value.split(",");
        if (taskString[1].equals("EPIC")) {
            if (taskString[3].equals("NEW")) {
                Epic epic = new Epic(TypeOfTask.EPIC, taskString[2], taskString[4], Status.NEW);
                epic.setId(Integer.parseInt(taskString[0]));
                return epic;
            }
            if (taskString[3].equals("IN_PROGRESS")) {
                Epic epic = new Epic(TypeOfTask.EPIC, taskString[2], taskString[4], Status.IN_PROGRESS);
                epic.setId(Integer.parseInt(taskString[0]));
                return epic;
            }
            if (taskString[3].equals("DONE")) {
                Epic epic = new Epic(TypeOfTask.EPIC, taskString[2], taskString[4], Status.DONE);
                epic.setId(Integer.parseInt(taskString[0]));
                return epic;
            }
        }
        return null;
    }

    //метод создания задачи из строки, если задача типа SubTask
    static SubTask fromStringSubTask(String value) {
        String[] taskString = value.split(",");
        if (taskString[1].equals("SUBTASK")) {
            if (taskString[3].equals("NEW")) {
                SubTask subTask = new SubTask(TypeOfTask.SUBTASK, taskString[2], taskString[4], Status.NEW,
                        Integer.parseInt(taskString[5]));
                subTask.setId(Integer.parseInt(taskString[0]));
                return subTask;
            }
            if (taskString[3].equals("IN_PROGRESS")) {
                SubTask subTask = new SubTask(TypeOfTask.EPIC, taskString[2], taskString[4], Status.IN_PROGRESS,
                        Integer.parseInt(taskString[5]));
                subTask.setId(Integer.parseInt(taskString[0]));
                return subTask;
            }
            if (taskString[3].equals("DONE")) {
                SubTask subTask = new SubTask(TypeOfTask.EPIC, taskString[2], taskString[4], Status.DONE,
                        Integer.parseInt(taskString[5]));
                subTask.setId(Integer.parseInt(taskString[0]));
                return subTask;
            }
        }
        return null;
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
        save();
        return t;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic e = super.addEpic(epic);
        save();
        return e;
    }

    @Override
    public SubTask addSubTask(SubTask subTask) {
        SubTask s = super.addSubTask(subTask);
        save();
        return s;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> l = super.getTasks();
        return l;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> e = super.getEpics();
        return e;
    }

    @Override
    public List<SubTask> getSubTasks() {
        List<SubTask> s = super.getSubTasks();
        return s;
    }

    @Override
    public void deleteListTasks() {
        super.deleteListTasks();
    }

    @Override
    public void deleteListEpics() {
        super.deleteListEpics();
    }

    @Override
    public void deleteListSubTask() {
        super.deleteListSubTask();
    }

    @Override
    public Task getTask(int id) {
        Task t = super.getTask(id);
        save();
        return t;
    }

    @Override
    public Epic getEpic(int id) {
        Epic e = super.getEpic(id);
        save();
        return e;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask s = super.getSubTask(id);
        save();
        return s;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void delTask(int id) {
        super.delTask(id);
        save();
    }

    @Override
    public void delEpic(int id) {
        super.delEpic(id);
        save();
    }

    @Override
    public void delSubTask(int id) {
        super.delSubTask(id);
        save();
    }

    @Override
    public List<SubTask> getEpicSubTasks(int id) {
        return super.getEpicSubTasks(id);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
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

        Path filePath = Path.of("C:\\Users\\Aliya\\git\\java-kanban\\resources\\file.csv");
        loadFromFile(filePath);
    }
}