package ru.practicum.kanban.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.kanban.models.*;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager());
    }

    private static final Path path = Path.of("resources/programResults.csv");
    private final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();

    @Test
    public void shouldSaveAndLoadNormal() {
        Task taskFirst = new Task(TypeOfTask.TASK, "Новая задача 1", "Описание первой задачи", Status.NEW,
                Instant.ofEpochMilli(1665385200000L), 2);
        fileBackedTasksManager.addTask(taskFirst);
        Task taskSecond = new Task(TypeOfTask.TASK, "Новая задача 2", "Описание второй задачи", Status.NEW,
                Instant.ofEpochMilli(1665388800000L), 10);
        fileBackedTasksManager.addTask(taskSecond);

        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Новый эпик 1", "Описание первого эпика", Status.NEW,
                Instant.ofEpochMilli(0), 0);
        fileBackedTasksManager.addEpic(epicFirst);
        Epic epicSecond = new Epic(TypeOfTask.EPIC, "Новый эпик 2", "Описание второго эпика", Status.NEW,
                Instant.ofEpochMilli(0), 0);
        fileBackedTasksManager.addEpic(epicSecond);

        SubTask subTaskShop = new SubTask(TypeOfTask.SUBTASK, "Новый сабтаск 1", "Описание первого сабтаска",
                Status.DONE, 3, Instant.ofEpochMilli(1665392400000L), 10);
        fileBackedTasksManager.addSubTask(subTaskShop);
        SubTask subTaskBuy = new SubTask(TypeOfTask.SUBTASK, "Новый сабтаск 2", "Описание второго сабтаска",
                Status.IN_PROGRESS, 3, Instant.ofEpochMilli(1665399600000L), 15);
        fileBackedTasksManager.addSubTask(subTaskBuy);
        SubTask subTaskCar = new SubTask(TypeOfTask.SUBTASK, "Новый сабтаск 3", "Описание третьего сабтаска",
                Status.NEW, 4, Instant.ofEpochMilli(1665406800000L), 15);
        fileBackedTasksManager.addSubTask(subTaskCar);

        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getTask(2);
        fileBackedTasksManager.getTask(2);
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getEpic(3);
        fileBackedTasksManager.getEpic(4);
        fileBackedTasksManager.getEpic(3);
        fileBackedTasksManager.getSubTask(5);
        fileBackedTasksManager.getSubTask(6);
        fileBackedTasksManager.getSubTask(7);
        fileBackedTasksManager.getSubTask(6);
        fileBackedTasksManager.getTask(1);

        fileBackedTasksManager.save(path);

        FileBackedTasksManager fileBackedTasksManagerFromFile = FileBackedTasksManager.loadFromFile(path);
        int id = fileBackedTasksManagerFromFile.id;
        Map<Integer, Task> tableTasks = fileBackedTasksManagerFromFile.tableTasks;
        Map<Integer, Epic> tableEpics = fileBackedTasksManagerFromFile.tableEpics;
        Map<Integer, SubTask> tableSubTasks = fileBackedTasksManagerFromFile.tableSubTasks;

        assertEquals(7, id, "Неверный id");
        assertEquals(fileBackedTasksManager.tableTasks, tableTasks, "Таблицы с тасками не соответствуют");
        assertEquals(fileBackedTasksManager.tableEpics, tableEpics, "Таблицы с эпиками не соответствуют");
        assertEquals(fileBackedTasksManager.tableSubTasks, tableSubTasks, "Таблицы с подзадачами не соответствуют");

        int count = 0;

        for (int i = 0; i < fileBackedTasksManager.getHistory().size(); i++) {
            for (int j = fileBackedTasksManagerFromFile.getHistory().size() - 1; j >= 0; j--) {
                if (fileBackedTasksManager.getHistory().get(i)
                        .equals(fileBackedTasksManagerFromFile.getHistory().get(j))) {
                    count++;
                }
            }
        }

        assertEquals(7, count, "Истории не равны");
    }

    @Test
    void whenAssertingException() {
        Throwable exception = assertThrows(
                ManagerSaveException.class,
                () -> {
                    throw new ManagerSaveException("Ошибка при сохранении данных в файл");
                }
        );
        assertEquals("Ошибка при сохранении данных в файл", exception.getMessage());
    }

    @Test
    public void shouldSaveAndLoadWithoutTasks() {
        fileBackedTasksManager.save(path);

        FileBackedTasksManager fileBackedTasksManagerFromFile = FileBackedTasksManager.loadFromFile(path);

        assertEquals(0, fileBackedTasksManager.getTasks().size(), "Список задач не пустой");
        assertEquals(0, fileBackedTasksManager.getEpics().size(), "Список эпиков не пустой");
        assertEquals(0, fileBackedTasksManager.getSubTasks().size(), "Список подзадач не пустой");
        assertEquals(0, fileBackedTasksManager.getHistory().size(), "История не пустая");

        assertEquals(0, fileBackedTasksManagerFromFile.getTasks().size(), "Список задач не пустой");
        assertEquals(0, fileBackedTasksManagerFromFile.getEpics().size(), "Список эпиков не пустой");
        assertEquals(0, fileBackedTasksManagerFromFile.getSubTasks().size(), "Список подзадач не пустой");
        assertEquals(0, fileBackedTasksManagerFromFile.getHistory().size(), "История не пустая");
    }

    @Test
    public void shouldSaveAndLoadEpicWithoutSubTasks() {
        Task taskFirst = new Task(TypeOfTask.TASK, "Новая задача 1", "Описание первой задачи", Status.NEW,
                Instant.ofEpochMilli(1665385200000L), 2);
        final int taskFirstId = fileBackedTasksManager.addTask(taskFirst).getId();
        Task taskSecond = new Task(TypeOfTask.TASK, "Новая задача 2", "Описание второй задачи", Status.NEW,
                Instant.ofEpochMilli(1665388800000L), 10);
        final int taskSecondId = fileBackedTasksManager.addTask(taskSecond).getId();

        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic1", "Test addNewEpic description1",
                Status.NEW, Instant.now(), 0);
        final int epicFirstId = fileBackedTasksManager.addEpic(epicFirst).getId();
        Epic epicSecond = new Epic(TypeOfTask.EPIC, "Test addNewEpic2", "Test addNewEpic description2",
                Status.NEW, Instant.now(), 0);
        final int epicSecondId = fileBackedTasksManager.addEpic(epicSecond).getId();

        fileBackedTasksManager.getTask(taskFirstId);
        fileBackedTasksManager.getTask(taskSecondId);
        fileBackedTasksManager.getTask(taskSecondId);
        fileBackedTasksManager.getTask(taskSecondId);
        fileBackedTasksManager.getTask(taskSecondId);
        fileBackedTasksManager.getEpic(epicFirstId);
        fileBackedTasksManager.getEpic(epicSecondId);

        fileBackedTasksManager.save(path);

        FileBackedTasksManager fileBackedTasksManagerFromFile = FileBackedTasksManager.loadFromFile(path);
        int id = fileBackedTasksManagerFromFile.id;
        Map<Integer, Task> tableTasks = fileBackedTasksManagerFromFile.tableTasks;
        Map<Integer, Epic> tableEpics = fileBackedTasksManagerFromFile.tableEpics;
        Map<Integer, SubTask> tableSubTasks = fileBackedTasksManagerFromFile.tableSubTasks;

        assertEquals(4, id, "Неверный id");
        assertEquals(fileBackedTasksManager.tableTasks, tableTasks, "Таблицы с тасками не соответствуют");
        assertEquals(fileBackedTasksManager.tableEpics, tableEpics, "Таблицы с эпиками не соответствуют");
        assertEquals(fileBackedTasksManager.tableSubTasks, tableSubTasks, "Таблицы с подзадачами не соответствуют");
        assertEquals(fileBackedTasksManager.tableEpics.get(epicFirstId).getStatus(),
                fileBackedTasksManagerFromFile.tableEpics.get(epicFirstId).getStatus(), "Статусы эпиков не равны");

        int count = 0;

        for (int i = 0; i < fileBackedTasksManager.getHistory().size(); i++) {
            for (int j = fileBackedTasksManagerFromFile.getHistory().size() - 1; j >= 0; j--) {
                if (fileBackedTasksManager.getHistory().get(i)
                        .equals(fileBackedTasksManagerFromFile.getHistory().get(j))) {
                    count++;
                }
            }
        }

        assertEquals(4, count, "Истории не равны");
    }

    @Test
    public void shouldSaveAndLoadWithoutHistory() {
        Task taskFirst = new Task(TypeOfTask.TASK, "Test addNewTask1", "Test addNewTask description1",
                Status.NEW, Instant.now(), 0);
        fileBackedTasksManager.addTask(taskFirst);
        Task taskSecond = new Task(TypeOfTask.TASK, "Test addNewTask2", "Test addNewTask description2",
                Status.IN_PROGRESS, Instant.now(), 0);
        fileBackedTasksManager.addTask(taskSecond);

        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic1", "Test addNewEpic description1",
                Status.NEW, Instant.now(), 0);
        final int epicFirstId = fileBackedTasksManager.addEpic(epicFirst).getId();
        Epic epicSecond = new Epic(TypeOfTask.EPIC, "Test addNewEpic2", "Test addNewEpic description2",
                Status.NEW, Instant.now(), 0);
        final int epicSecondId = fileBackedTasksManager.addEpic(epicSecond).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask1",
                "Test addNewSubTask1 description", Status.IN_PROGRESS, epicFirstId, Instant.now(), 0);
        fileBackedTasksManager.addSubTask(subTaskFirst);

        fileBackedTasksManager.save(path);

        FileBackedTasksManager fileBackedTasksManagerFromFile = FileBackedTasksManager.loadFromFile(path);
        int id = fileBackedTasksManagerFromFile.id;
        Map<Integer, Task> tableTasks = fileBackedTasksManagerFromFile.tableTasks;
        Map<Integer, Epic> tableEpics = fileBackedTasksManagerFromFile.tableEpics;
        Map<Integer, SubTask> tableSubTasks = fileBackedTasksManagerFromFile.tableSubTasks;

        assertEquals(5, id, "Неверный id");
        assertEquals(fileBackedTasksManager.tableTasks, tableTasks, "Таблицы с тасками не соответствуют");
        assertEquals(fileBackedTasksManager.tableEpics, tableEpics, "Таблицы с эпиками не соответствуют");
        assertEquals(fileBackedTasksManager.tableSubTasks, tableSubTasks, "Таблицы с подзадачами не соответствуют");
        assertEquals(fileBackedTasksManager.getHistory().size(), fileBackedTasksManagerFromFile.getHistory().size(),
                "Неверный размер истории");
    }
}