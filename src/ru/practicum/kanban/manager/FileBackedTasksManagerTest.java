package ru.practicum.kanban.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.kanban.models.*;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager());
    }

    private static final Path path = Path.of("resources/programResults.csv");
    private final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();

    @Test
    public void shouldSaveAndLoadNormal() {
        Task taskFirst = new Task(TypeOfTask.TASK, "Test addNewTask1", "Test addNewTask description1",
                Status.NEW, Instant.now(), 0);
        final int taskFirstId = fileBackedTasksManager.addTask(taskFirst).getId();
        Task taskSecond = new Task(TypeOfTask.TASK, "Test addNewTask2", "Test addNewTask description2",
                Status.IN_PROGRESS, Instant.now(), 0);
        final int taskSecondId = fileBackedTasksManager.addTask(taskSecond).getId();

        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic1", "Test addNewEpic description1",
                Status.NEW, Instant.now(), 0);
        final int epicFirstId = fileBackedTasksManager.addEpic(epicFirst).getId();
        Epic epicSecond = new Epic(TypeOfTask.EPIC, "Test addNewEpic2", "Test addNewEpic description2",
                Status.NEW, Instant.now(), 0);
        final int epicSecondId = fileBackedTasksManager.addEpic(epicSecond).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask1",
                "Test addNewSubTask1 description", Status.IN_PROGRESS, epicFirstId, Instant.now(), 0);
        final int subTaskFirstId = fileBackedTasksManager.addSubTask(subTaskFirst).getId();
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask2",
                "Test addNewSubTask2 description", Status.DONE, epicSecondId, Instant.now(), 0);
        final int subTaskSecondId = fileBackedTasksManager.addSubTask(subTaskSecond).getId();

        fileBackedTasksManager.getTask(taskFirstId);
        fileBackedTasksManager.getTask(taskSecondId);
        fileBackedTasksManager.getTask(taskSecondId);
        fileBackedTasksManager.getTask(taskSecondId);
        fileBackedTasksManager.getTask(taskSecondId);
        fileBackedTasksManager.getEpic(epicFirstId);
        fileBackedTasksManager.getEpic(epicSecondId);
        fileBackedTasksManager.getSubTask(subTaskFirstId);
        fileBackedTasksManager.getSubTask(subTaskSecondId);

        fileBackedTasksManager.save(path);

        FileBackedTasksManager fileBackedTasksManagerFromFile = FileBackedTasksManager.loadFromFile(path);
        int id = fileBackedTasksManagerFromFile.id;
        Map<Integer, Task> tableTasks = fileBackedTasksManagerFromFile.tableTasks;
        Map<Integer, Epic> tableEpics = fileBackedTasksManagerFromFile.tableEpics;
        Map<Integer, SubTask> tableSubTasks = fileBackedTasksManagerFromFile.tableSubTasks;

        assertEquals(6, id, "Неверный id");
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

        assertEquals(6, count, "Истории не равны");
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
        Task taskFirst = new Task(TypeOfTask.TASK, "Test addNewTask1", "Test addNewTask description1",
                Status.NEW, Instant.now(), 0);
        final int taskFirstId = fileBackedTasksManager.addTask(taskFirst).getId();
        Task taskSecond = new Task(TypeOfTask.TASK, "Test addNewTask2", "Test addNewTask description2",
                Status.IN_PROGRESS, Instant.now(), 0);
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
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask2",
                "Test addNewSubTask2 description", Status.DONE, epicSecondId, Instant.now(), 0);
        fileBackedTasksManager.addSubTask(subTaskSecond);

        fileBackedTasksManager.save(path);

        FileBackedTasksManager fileBackedTasksManagerFromFile = FileBackedTasksManager.loadFromFile(path);
        int id = fileBackedTasksManagerFromFile.id;
        Map<Integer, Task> tableTasks = fileBackedTasksManagerFromFile.tableTasks;
        Map<Integer, Epic> tableEpics = fileBackedTasksManagerFromFile.tableEpics;
        Map<Integer, SubTask> tableSubTasks = fileBackedTasksManagerFromFile.tableSubTasks;

        assertEquals(6, id, "Неверный id");
        assertEquals(fileBackedTasksManager.tableTasks, tableTasks, "Таблицы с тасками не соответствуют");
        assertEquals(fileBackedTasksManager.tableEpics, tableEpics, "Таблицы с эпиками не соответствуют");
        assertEquals(fileBackedTasksManager.tableSubTasks, tableSubTasks, "Таблицы с подзадачами не соответствуют");
        assertEquals(fileBackedTasksManager.getHistory().size(), fileBackedTasksManagerFromFile.getHistory().size(),
                "Неверный размер истории");
    }
}