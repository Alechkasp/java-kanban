package ru.practicum.kanban.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.kanban.manager.Managers;
import ru.practicum.kanban.manager.TaskManager;
import ru.practicum.kanban.models.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    private final Managers managers = new Managers();
    private final TaskManager taskManager = managers.getDefault();
    //private final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();

    @Test
    void addNewTask() {
        Task task = new Task(TypeOfTask.TASK,"Test addNewTask", "Test addNewTask description",
                Status.NEW);
        final int taskId = taskManager.addTask(task).getId();

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
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
    void addNewSubTask() {
        Epic epic = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        final int epicId = taskManager.addEpic(epic).getId();

        SubTask subTask = new SubTask(TypeOfTask.SUBTASK,"Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId);
        final int subTaskId = taskManager.addSubTask(subTask).getId();

        final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Задачи нe возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldGetListTasks() {
        Task taskFirst = new Task(TypeOfTask.TASK,"Test addNewTask", "Test addNewTask description",
                Status.NEW);
        taskManager.addTask(taskFirst);
        Task taskSecond = new Task(TypeOfTask.TASK,"Test addNewTask", "Test addNewTask description",
                Status.NEW);
        taskManager.addTask(taskSecond);

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(taskFirst, tasks.get(0), "Задачи не совпадают.");
        assertEquals(taskSecond, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void shouldGetListEpics() {
        Epic epicFirst = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        taskManager.addEpic(epicFirst);
        Epic epicSecond = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        taskManager.addEpic(epicSecond);

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи нe возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество задач.");
        assertEquals(epicFirst, epics.get(0), "Задачи не совпадают.");
        assertEquals(epicSecond, epics.get(1), "Задачи не совпадают.");
    }

    @Test
    void shouldGetListSubTasks() {
        Epic epicFirst = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        taskManager.addEpic(epicFirst);
        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK,"Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicFirst.getId());
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK,"Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicFirst.getId());
        taskManager.addSubTask(subTaskSecond);

        final List<SubTask> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Задачи нe возвращаются.");
        assertEquals(2, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTaskFirst, subTasks.get(0), "Задачи не совпадают.");
        assertEquals(subTaskSecond, subTasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void shouldGetTask() {
        Task taskFirst = new Task(TypeOfTask.TASK,"Test addNewTask", "Test addNewTask description",
                Status.NEW);
        taskManager.addTask(taskFirst);

        final int taskId = taskFirst.getId();

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(taskFirst, savedTask, "Задачи не совпадают.");
    }

    @Test
    void shouldGetEpic() {
        Epic epicFirst = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        taskManager.addEpic(epicFirst);

        final int epicId = epicFirst.getId();

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epicFirst, savedEpic, "Задачи не совпадают.");
    }

    @Test
    void shouldGetSubTask() {
        Epic epicFirst = new Epic(TypeOfTask.EPIC,"Test addNewEpic", "Test addNewEpic description",
                Status.NEW);
        taskManager.addEpic(epicFirst);
        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK,"Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicFirst.getId());
        taskManager.addSubTask(subTaskFirst);

        final int subTaskId = subTaskFirst.getId();

        final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTaskFirst, savedSubTask, "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateTask() {
        Task taskFirst = new Task(TypeOfTask.TASK,"Test addNewTask", "Test addNewTask description",
                Status.NEW);
        taskManager.addTask(taskFirst);

        final int taskId = taskFirst.getId();

        Task taskUpdate = new Task(TypeOfTask.TASK,"Test updateTask", "Test updateTask description",
                Status.IN_PROGRESS);
        taskUpdate.setId(taskId);
        taskManager.updateTask(taskUpdate);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(taskUpdate, savedTask, "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateEpic() {

    }

    @Test
    void shouldUpdateSubTask() {

    }
}