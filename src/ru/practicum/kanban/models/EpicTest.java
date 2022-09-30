package ru.practicum.kanban.models;

import org.junit.jupiter.api.Test;
import ru.practicum.kanban.manager.Managers;
import ru.practicum.kanban.manager.TaskManager;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private final Managers managers = new Managers();
    private final TaskManager taskManager = managers.getDefault();

    @Test
    void addNewEpic() {
        Epic epic = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description", Status.NEW,
                Instant.now(), 0);
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
        Epic epic = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description",
                Status.IN_PROGRESS, Instant.now(), 0);
        final int epicId = taskManager.addEpic(epic).getId();

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(epic.getStatus(), savedEpic.getStatus(), "Статусы не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith3SubTasksWithStatusNEW() {
        Epic epic = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description",
                Status.IN_PROGRESS, Instant.now(), 0);
        final int epicId = taskManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskThird);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(epic.getStatus(), savedEpic.getStatus(), "Статусы не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith3SubTasksWithStatusDONE() {
        Epic epic = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description",
                Status.IN_PROGRESS, Instant.now(), 0);
        final int epicId = taskManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.DONE, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.DONE, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.DONE, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskThird);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(epic.getStatus(), savedEpic.getStatus(), "Статусы не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith3SubTasksWithStatusNEWandDONE() {
        Epic epic = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description",
                Status.NEW, Instant.now(), 0);
        final int epicId = taskManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.DONE, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskThird);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(epic.getStatus(), savedEpic.getStatus(), "Статусы не совпадают.");
    }

    @Test
    void shouldUpdateEpicStatusWith3SubTasksWithStatusIN_PROGRESS() {
        Epic epic = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description",
                Status.NEW, Instant.now(), 0);
        final int epicId = taskManager.addEpic(epic).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskSecond);
        SubTask subTaskThird = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskThird);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(epic.getStatus(), savedEpic.getStatus(), "Статусы не совпадают.");
    }
}