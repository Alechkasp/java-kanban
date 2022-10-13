package ru.practicum.kanban.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.kanban.models.*;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager());
    }

    private static final Path path = Path.of("resources/programResults.csv");
    private final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();

    @Test
    void addNewEpic() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description", Status.NEW);
        final int epicId = fileBackedTasksManager.addEpic(epic).getId();

        final Epic savedEpic = fileBackedTasksManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = fileBackedTasksManager.getEpics();

        assertNotNull(epics, "Задачи нe возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith0SubTasks() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.IN_PROGRESS);
        final int epicId = fileBackedTasksManager.addEpic(epic).getId();

        final Epic savedEpic = fileBackedTasksManager.getEpic(epicId);

        assertEquals(epic.getStatus(), savedEpic.getStatus(), "Статусы не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith3SubTasksWithStatusNEW() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.IN_PROGRESS);
        final int epicId = fileBackedTasksManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId,
                Instant.ofEpochMilli(1662023410000L), 10);
        fileBackedTasksManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId,
                Instant.ofEpochMilli(1662027010000L), 15);
        fileBackedTasksManager.addSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId,
                Instant.ofEpochMilli(1662027010000L), 5);
        fileBackedTasksManager.addSubTask(subTaskThird);

        final Epic savedEpic = fileBackedTasksManager.getEpic(epicId);

        assertEquals(savedEpic.getStatus(), Status.NEW, "Статусы не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith3SubTasksWithStatusDONE() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.IN_PROGRESS);
        final int epicId = fileBackedTasksManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.DONE, epicId,
                Instant.ofEpochMilli(1662023410000L), 10);
        fileBackedTasksManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.DONE, epicId,
                Instant.ofEpochMilli(1662027010000L), 15);
        fileBackedTasksManager.addSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.DONE, epicId,
                Instant.ofEpochMilli(1662027010000L), 5);
        fileBackedTasksManager.addSubTask(subTaskThird);

        final Epic savedEpic = fileBackedTasksManager.getEpic(epicId);

        assertEquals(savedEpic.getStatus(), Status.DONE, "Статусы не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith3SubTasksWithStatusNEWandDONE() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        final int epicId = fileBackedTasksManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId,
                Instant.ofEpochMilli(1662023410000L), 10);
        fileBackedTasksManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId,
                Instant.ofEpochMilli(1662027010000L), 15);
        fileBackedTasksManager.addSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.DONE, epicId,
                Instant.ofEpochMilli(1662027010000L), 5);
        fileBackedTasksManager.addSubTask(subTaskThird);

        final Epic savedEpic = fileBackedTasksManager.getEpic(epicId);

        assertEquals(savedEpic.getStatus(), Status.IN_PROGRESS, "Статусы не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith3SubTasksWithStatusIN_PROGRESS() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        final int epicId = fileBackedTasksManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId,
                Instant.ofEpochMilli(1662023410000L), 10);
        fileBackedTasksManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId,
                Instant.ofEpochMilli(1662027010000L), 15);
        fileBackedTasksManager.addSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId,
                Instant.ofEpochMilli(1662027010000L), 5);
        fileBackedTasksManager.addSubTask(subTaskThird);

        final Epic savedEpic = fileBackedTasksManager.getEpic(epicId);

        assertEquals(epic.getStatus(), savedEpic.getStatus(), "Статусы не совпадают.");
    }

    @Test
    void shouldCalculateTimeWithSubTasks() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        final int epicId = fileBackedTasksManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId,
                Instant.ofEpochMilli(1662023410000L), 10);
        fileBackedTasksManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId,
                Instant.ofEpochMilli(1662027010000L), 15);
        fileBackedTasksManager.addSubTask(subTaskSecond);

        final Epic savedEpic = fileBackedTasksManager.getEpic(epicId);

        assertEquals(savedEpic.getStartTime(), subTaskFirst.getStartTime(), "Время начала эпика неверное.");
        assertEquals(savedEpic.getEndTime(), subTaskSecond.getEndTime(), "Время конца эпика неверное.");
        assertEquals(savedEpic.getDuration(), 75, "Продолжительность эпика неверная.");
    }

    @Test
    void shouldCalculateTimeWithoutSubTasks() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        final int epicId = fileBackedTasksManager.addEpic(epic).getId();

        final Epic savedEpic = fileBackedTasksManager.getEpic(epicId);

        assertEquals(savedEpic.getStartTime(), Instant.ofEpochMilli(0), "Время начала эпика неверное.");
        assertEquals(savedEpic.getEndTime(), Instant.ofEpochMilli(0), "Время конца эпика неверное.");
        assertEquals(savedEpic.getDuration(), 0, "Продолжительность эпика неверная.");
    }

    @Test
    public void shouldSaveAndLoadNormal() {
        Task taskFirst = new Task(TypeOfTask.TASK, "Новая задача 1", "Описание первой задачи", Status.NEW,
                Instant.ofEpochMilli(1665385200000L), 2);
        fileBackedTasksManager.addTask(taskFirst);
        Task taskSecond = new Task(TypeOfTask.TASK, "Новая задача 2", "Описание второй задачи", Status.NEW,
                Instant.ofEpochMilli(1665388800000L), 10);
        fileBackedTasksManager.addTask(taskSecond);

        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Новый эпик 1", "Описание первого эпика", Status.NEW);
        fileBackedTasksManager.addEpic(epicFirst);
        Epic epicSecond = new Epic(TypeOfTask.EPIC, "Новый эпик 2", "Описание второго эпика", Status.NEW);
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

        FileBackedTasksManager fileBackedTasksManagerFromFile = fileBackedTasksManager.loadFromFile(path);

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

        FileBackedTasksManager fileBackedTasksManagerFromFile = fileBackedTasksManager.loadFromFile(path);

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
                Status.NEW);
        final int epicFirstId = fileBackedTasksManager.addEpic(epicFirst).getId();
        Epic epicSecond = new Epic(TypeOfTask.EPIC, "Test addNewEpic2", "Test addNewEpic description2",
                Status.NEW);
        final int epicSecondId = fileBackedTasksManager.addEpic(epicSecond).getId();

        fileBackedTasksManager.getTask(taskFirstId);
        fileBackedTasksManager.getTask(taskSecondId);
        fileBackedTasksManager.getTask(taskSecondId);
        fileBackedTasksManager.getTask(taskSecondId);
        fileBackedTasksManager.getTask(taskSecondId);
        fileBackedTasksManager.getEpic(epicFirstId);
        fileBackedTasksManager.getEpic(epicSecondId);

        fileBackedTasksManager.save(path);

        FileBackedTasksManager fileBackedTasksManagerFromFile = fileBackedTasksManager.loadFromFile(path);
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
                Status.NEW);
        final int epicFirstId = fileBackedTasksManager.addEpic(epicFirst).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask1",
                "Test addNewSubTask1 description", Status.IN_PROGRESS, epicFirstId, Instant.now(), 0);
        fileBackedTasksManager.addSubTask(subTaskFirst);

        fileBackedTasksManager.save(path);

        FileBackedTasksManager fileBackedTasksManagerFromFile = fileBackedTasksManager.loadFromFile(path);
        int id = fileBackedTasksManagerFromFile.id;
        Map<Integer, Task> tableTasks = fileBackedTasksManagerFromFile.tableTasks;
        Map<Integer, Epic> tableEpics = fileBackedTasksManagerFromFile.tableEpics;
        Map<Integer, SubTask> tableSubTasks = fileBackedTasksManagerFromFile.tableSubTasks;

        assertEquals(4, id, "Неверный id");
        assertEquals(fileBackedTasksManager.tableTasks, tableTasks, "Таблицы с тасками не соответствуют");
        assertEquals(fileBackedTasksManager.tableEpics, tableEpics, "Таблицы с эпиками не соответствуют");
        assertEquals(fileBackedTasksManager.tableSubTasks, tableSubTasks, "Таблицы с подзадачами не соответствуют");
        assertEquals(fileBackedTasksManager.getHistory().size(), fileBackedTasksManagerFromFile.getHistory().size(),
                "Неверный размер истории");
    }
}