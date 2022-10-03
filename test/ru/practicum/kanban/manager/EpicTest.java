package ru.practicum.kanban.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.kanban.models.Epic;
import ru.practicum.kanban.models.Status;
import ru.practicum.kanban.models.SubTask;
import ru.practicum.kanban.models.TypeOfTask;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private final Managers managers = new Managers();
    private final TaskManager taskManager = managers.getDefault();

    @Test
    void addNewEpic() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description", Status.NEW);
        final int epicId = taskManager.addEpic(epic).getId();

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи нe возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith0SubTasks() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.IN_PROGRESS);
        final int epicId = taskManager.addEpic(epic).getId();

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(epic.getStatus(), savedEpic.getStatus(), "Статусы не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith3SubTasksWithStatusNEW() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.IN_PROGRESS);
        final int epicId = taskManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId,
                Instant.ofEpochMilli(1662023410000L), 10);
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId,
                Instant.ofEpochMilli(1662027010000L), 15);
        taskManager.addSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId,
                Instant.ofEpochMilli(1662027010000L), 5);
        taskManager.addSubTask(subTaskThird);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(savedEpic.getStatus(), Status.NEW, "Статусы не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith3SubTasksWithStatusDONE() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.IN_PROGRESS);
        final int epicId = taskManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.DONE, epicId,
                Instant.ofEpochMilli(1662023410000L), 10);
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.DONE, epicId,
                Instant.ofEpochMilli(1662027010000L), 15);
        taskManager.addSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.DONE, epicId,
                Instant.ofEpochMilli(1662027010000L), 5);
        taskManager.addSubTask(subTaskThird);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(savedEpic.getStatus(), Status.DONE, "Статусы не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith3SubTasksWithStatusNEWandDONE() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        final int epicId = taskManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId,
                Instant.ofEpochMilli(1662023410000L), 10);
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId,
                Instant.ofEpochMilli(1662027010000L), 15);
        taskManager.addSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.DONE, epicId,
                Instant.ofEpochMilli(1662027010000L), 5);
        taskManager.addSubTask(subTaskThird);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(savedEpic.getStatus(), Status.IN_PROGRESS, "Статусы не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith3SubTasksWithStatusIN_PROGRESS() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        final int epicId = taskManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId,
                Instant.ofEpochMilli(1662023410000L), 10);
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId,
                Instant.ofEpochMilli(1662027010000L), 15);
        taskManager.addSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId,
                Instant.ofEpochMilli(1662027010000L), 5);
        taskManager.addSubTask(subTaskThird);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(epic.getStatus(), savedEpic.getStatus(), "Статусы не совпадают.");
    }

    @Test
    void shouldCalculateTimeWithSubTasks() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        final int epicId = taskManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId,
                Instant.ofEpochMilli(1662023410000L), 10);
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId,
                Instant.ofEpochMilli(1662027010000L), 15);
        taskManager.addSubTask(subTaskSecond);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(savedEpic.getStartTime(), subTaskFirst.getStartTime(), "Время начала эпика неверное.");
        assertEquals(savedEpic.getEndTime(), subTaskSecond.getEndTime(), "Время конца эпика неверное.");
        assertEquals(savedEpic.getDuration(), 75, "Продолжительность эпика неверная.");
    }

    @Test
    void shouldCalculateTimeWithoutSubTasks() {
        Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        final int epicId = taskManager.addEpic(epic).getId();

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(savedEpic.getStartTime(), Instant.ofEpochMilli(0), "Время начала эпика неверное.");
        assertEquals(savedEpic.getEndTime(), Instant.ofEpochMilli(0), "Время конца эпика неверное.");
        assertEquals(savedEpic.getDuration(), 0, "Продолжительность эпика неверная.");
    }
}