package ru.practicum.kanban.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.kanban.models.*;

import java.time.Instant;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest<T extends TaskManager> {
    private final TaskManager taskManager;
    public TaskManagerTest(T manager) {
        taskManager = manager;
    }

        @Test
        void shouldAddNewTask() {
            Task task = new Task(TypeOfTask.TASK, "Test addNewTask", "Test addNewTask description",
                    Status.NEW, Instant.ofEpochMilli(1665386700000L), 20);
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
        void shouldAddNewEpic() {
            Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                    Status.NEW, Instant.ofEpochMilli(0), 0);
            final int epicId = taskManager.addEpic(epic).getId();
            SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask1",
                    "Test addNewSubTask1 description", Status.DONE, epicId,
                    Instant.now(), 10);
            taskManager.addSubTask(subTaskFirst);
            SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask2",
                    "Test addNewSubTask2 description", Status.IN_PROGRESS, epicId,
                    Instant.now(), 15);
            taskManager.addSubTask(subTaskSecond);

            final Epic savedEpic = taskManager.getEpic(epicId);

            assertNotNull(savedEpic, "Задача не найдена.");
            assertEquals(epic, savedEpic, "Задачи не совпадают.");
            assertEquals(savedEpic.getStatus(), Status.IN_PROGRESS, "Неверно рассчитан статус Эпика.");

            final List<Epic> epics = taskManager.getEpics();

            assertNotNull(epics, "Задачи нe возвращаются.");
            assertEquals(1, epics.size(), "Неверное количество задач.");
            assertEquals(epic, epics.get(0), "Задачи не совпадают.");
        }

        @Test
        void shouldAddNewSubTask() {
            Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                    Status.NEW, Instant.ofEpochMilli(0), 0);
            final int epicId = taskManager.addEpic(epic).getId();

            SubTask subTask = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                    "Test addNewSubTask description", Status.NEW, epicId,
                    Instant.ofEpochMilli(1665401100000L), 15);
            final int subTaskId = taskManager.addSubTask(subTask).getId();

            final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

            assertNotNull(savedSubTask, "Задача не найдена.");
            assertNotNull(epic, "Эпик не найден");
            assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

            final List<SubTask> subTasks = taskManager.getSubTasks();

            assertNotNull(subTasks, "Задачи нe возвращаются.");
            assertEquals(1, subTasks.size(), "Неверное количество задач.");
            assertEquals(subTask, subTasks.get(0), "Задачи не совпадают.");
        }

        @Test
        void shouldGetListTasks() {
            final List<Task> epics = taskManager.getTasks();

            assertNotNull(epics, "Задачи нe возвращаются.");
        }

        @Test
        void shouldGetListEpics() {
            final List<Epic> epics = taskManager.getEpics();

            assertNotNull(epics, "Задачи нe возвращаются.");
        }

        @Test
        void shouldGetListSubTasks() {
            final List<SubTask> subTasks = taskManager.getSubTasks();

            assertNotNull(subTasks, "Задачи нe возвращаются.");
        }

        @Test
        void shouldGetTask() {
            Task task = new Task(TypeOfTask.TASK, "Test addNewTask", "Test addNewTask description",
                    Status.NEW, Instant.ofEpochMilli(1665386700000L), 20);
            final int taskId = taskManager.addTask(task).getId();

            final Task savedTask = taskManager.getTask(taskId);

            assertNotNull(savedTask, "Задача не найдена.");
            assertEquals(1, taskId, "Неверный id задачи");
            assertEquals(task, savedTask, "Задачи не совпадают.");
        }

        @Test
        void shouldGetEpic() {
            Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                    Status.NEW, Instant.ofEpochMilli(0), 0);
            taskManager.addEpic(epicFirst);
            SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                    "Test addNewSubTask description", Status.DONE, epicFirst.getId(),
                    Instant.now(), 15);
            taskManager.addSubTask(subTaskFirst);

            final int epicId = epicFirst.getId();

            final Epic savedEpic = taskManager.getEpic(epicId);

            assertNotNull(savedEpic, "Задача не найдена.");
            assertEquals(1, epicId, "Неверный id задачи");
            assertEquals(epicFirst, savedEpic, "Задачи не совпадают.");
            assertEquals(savedEpic.getStatus(), Status.DONE, "Неверно рассчитан статус Эпика.");
            assertEquals(epicFirst.getStartTime(), subTaskFirst.getStartTime(), "Неверное startTime у эпика");
            assertEquals(epicFirst.getEndTime(), subTaskFirst.getEndTime(), "Неверное endTime у эпика");
            assertEquals(15, savedEpic.getDuration(), "Неверная продолжительность Эпика.");

            final List<Integer> subTaskIds = new ArrayList<>();
            for (SubTask s : taskManager.getSubTasks()) {
                subTaskIds.add(s.getId());
            }
            assertEquals(epicFirst.getSubTasks(), subTaskIds, "Неправильный список id подзадач эпика");
        }

        @Test
        void shouldGetSubTask() {
            Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                    Status.NEW, Instant.ofEpochMilli(0), 0);
            taskManager.addEpic(epicFirst);
            SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                    "Test addNewSubTask description", Status.IN_PROGRESS, epicFirst.getId(),
                    Instant.now(), 15);
            taskManager.addSubTask(subTaskFirst);

            final int subTaskId = subTaskFirst.getId();

            final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

            assertNotNull(savedSubTask, "Задача не найдена.");
            assertNotNull(taskManager.getSubTasks(), "Список сабтасков пустой");
            assertEquals(2, subTaskId, "Неверный id задачи");
            assertEquals(subTaskFirst, savedSubTask, "Задачи не совпадают.");
        }

        @Test
        void shouldUpdateTask() {
            Task taskFirst = new Task(TypeOfTask.TASK, "Test addNewTask", "Test addNewTask description",
                    Status.NEW, Instant.now(), 10);
            taskManager.addTask(taskFirst);

            final int taskId = taskFirst.getId();

            Task taskUpdate = new Task(TypeOfTask.TASK, "Test updateTask", "Test updateTask description",
                    Status.IN_PROGRESS, Instant.now(), 10);
            taskUpdate.setId(taskId);
            taskManager.updateTask(taskUpdate);

            final Task savedTask = taskManager.getTask(taskId);

            assertNotNull(savedTask, "Задача не найдена.");
            assertNotNull(taskManager.getTasks(), "Список тасков пустой");
            assertEquals(taskUpdate, savedTask, "Задачи не совпадают.");
        }

        @Test
        void shouldUpdateEpic() {
            Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                    Status.NEW, Instant.ofEpochMilli(0), 0);
            taskManager.addEpic(epicFirst);

            final int epicId = epicFirst.getId();

            Epic epicUpdate = new Epic(TypeOfTask.EPIC, "Test updateEpic", "Test updateEpic description",
                    Status.IN_PROGRESS, Instant.ofEpochMilli(0), 0);
            epicUpdate.setId(epicId);
            taskManager.updateEpic(epicUpdate);

            final Epic savedEpic = taskManager.getEpic(epicId);

            assertNotNull(savedEpic, "Задача не найдена.");
            assertNotNull(taskManager.getEpics(), "Список эпиков пустой");
            assertEquals(epicUpdate, savedEpic, "Задачи не совпадают.");
        }

        @Test
        void shouldUpdateSubTask() {
            Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                    Status.NEW, Instant.now(), 0);
            taskManager.addEpic(epicFirst);
            SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                    "Test addNewSubTask description", Status.NEW, epicFirst.getId(), Instant.now(), 5);
            taskManager.addSubTask(subTaskFirst);

            final int subTaskId = subTaskFirst.getId();

            SubTask subTaskUpdate = new SubTask(TypeOfTask.SUBTASK, "Test updateSubTask",
                    "Test updateSubTask description", Status.IN_PROGRESS, epicFirst.getId(),
                    Instant.now(), 5);
            subTaskUpdate.setId(subTaskId);
            taskManager.updateSubTask(subTaskUpdate);

            final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

            assertNotNull(savedSubTask, "Задача не найдена.");
            assertNotNull(taskManager.getSubTasks(), "Список подзадач пустой");
            assertEquals(subTaskUpdate, savedSubTask, "Задачи не совпадают.");
        }

        @Test
        void shouldDelTask() {
            Task taskFirst = new Task(TypeOfTask.TASK, "Test addNewTask1", "Test addNewTask description1",
                    Status.NEW, Instant.now(), 0);
            final int taskId = taskManager.addTask(taskFirst).getId();
            Task taskSecond = new Task(TypeOfTask.TASK, "Test addNewTask2", "Test addNewTask description2",
                    Status.IN_PROGRESS, Instant.now(), 0);
            taskManager.addTask(taskSecond);

            taskManager.delTask(taskId);

            final List<Task> tasks = taskManager.getTasks();

            assertNotNull(tasks, "Задачи нe возвращаются.");
            assertEquals(1, taskId, "Неверный id задачи");
            assertEquals(1, tasks.size(), "Неверное количество задач.");
            assertEquals(taskSecond, tasks.get(0), "Задачи не совпадают.");
        }

    @Test
    void shouldDelEpic() {
        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic1", "Test addNewEpic description1",
                Status.NEW, Instant.now(), 0);
        final int epicId = taskManager.addEpic(epicFirst).getId();
        Epic epicSecond = new Epic(TypeOfTask.EPIC, "Test addNewEpic2", "Test addNewEpic description2",
                Status.NEW, Instant.now(), 0);
        taskManager.addEpic(epicSecond);
        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId, Instant.now(), 0);
        taskManager.addSubTask(subTaskFirst);

        taskManager.delEpic(epicId);

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи нe возвращаются.");
        assertEquals(1, epicId, "Неверный id задачи");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epicSecond, epics.get(0), "Задачи не совпадают.");
        assertEquals(taskManager.getSubTasks().size(), 0,"У удаленного эпика остались подзадачи.");
    }

    @Test
    void shouldDelSubTask() {
        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.NEW, Instant.now(), 0);
        taskManager.addEpic(epicFirst);
        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask1",
                "Test addNewSubTask1 description", Status.IN_PROGRESS, epicFirst.getId(), Instant.now(), 0);
        final int subTaskId = taskManager.addSubTask(subTaskFirst).getId();
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask2",
                "Test addNewSubTask2 description", Status.DONE, epicFirst.getId(), Instant.now(), 0);
        taskManager.addSubTask(subTaskSecond);

        taskManager.delSubTask(subTaskId);

        final List<SubTask> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Задачи нe возвращаются.");
        assertEquals(2, subTaskId, "Неверный id задачи");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTaskSecond, subTasks.get(0), "Задачи не совпадают.");
        assertEquals(epicFirst.getStatus(), Status.DONE, "Неверно рассчитан статус эпика.");
    }

    @Test
    void shouldGetEpicSubTask() {
        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.NEW, Instant.now(), 0);
        final int epicFirstId = taskManager.addEpic(epicFirst).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask1",
                "Test addNewSubTask1 description", Status.IN_PROGRESS, epicFirstId, Instant.now(), 0);
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask2",
                "Test addNewSubTask2 description", Status.DONE, epicFirstId, Instant.now(), 0);
        taskManager.addSubTask(subTaskSecond);

        final List<SubTask> savedSubTasks = taskManager.getEpicSubTasks(epicFirstId);

        final List<SubTask> subTasks = new ArrayList<>();
        for (SubTask s : taskManager.getSubTasks()) {
            subTasks.add(s);
        }

        assertNotNull(subTasks, "Задачи нe возвращаются.");
        assertEquals(1, epicFirstId, "Неверный id эпика");
        assertEquals(savedSubTasks, subTasks, "Неправильный список подзадач эпика");
        assertEquals(epicFirst.getStatus(), Status.IN_PROGRESS, "Неверно рассчитан статус эпика.");
    }

    @Test
    void shouldDelListTasks() {
        Task taskFirst = new Task(TypeOfTask.TASK, "Test addNewTask1", "Test addNewTask description1",
                Status.NEW, Instant.now(), 0);
        taskManager.addTask(taskFirst);
        Task taskSecond = new Task(TypeOfTask.TASK, "Test addNewTask2", "Test addNewTask description2",
                Status.IN_PROGRESS, Instant.now(), 0);
        taskManager.addTask(taskSecond);

        assertNotNull(taskManager.getTasks(), "Задачи нe возвращаются.");

        taskManager.deleteListTasks();

        assertEquals(0, taskManager.getTasks().size(), "Задачи не удалены.");
    }

    @Test
    void shouldDelListEpics() {
        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic1", "Test addNewEpic description1",
                Status.NEW, Instant.now(), 0);
        taskManager.addEpic(epicFirst);
        Epic epicSecond = new Epic(TypeOfTask.EPIC, "Test addNewEpic2", "Test addNewEpic description2",
                Status.NEW, Instant.now(), 0);
        taskManager.addEpic(epicSecond);
        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.IN_PROGRESS, epicFirst.getId(), Instant.now(), 0);
        taskManager.addSubTask(subTaskFirst);

        assertNotNull(taskManager.getEpics(), "Задачи нe возвращаются.");
        assertNotNull(taskManager.getSubTasks(), "Задачи нe возвращаются.");
        assertEquals(1, epicFirst.getId(), "Неверный id эпика");
        assertEquals(epicFirst.getStatus(), Status.IN_PROGRESS, "Неверно рассчитан статус эпика.");

        taskManager.deleteListEpics();

        assertEquals(0, taskManager.getEpics().size(), "Задачи не удалены.");
        assertEquals(0, taskManager.getSubTasks().size(), "Задачи не удалены.");
    }

    @Test
    void shouldDelListSubTasks() {
        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                Status.NEW, Instant.now(), 0);
        final int epicFirstId = taskManager.addEpic(epicFirst).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask1",
                "Test addNewSubTask1 description", Status.IN_PROGRESS, epicFirstId, Instant.now(), 0);
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask2",
                "Test addNewSubTask2 description", Status.DONE, epicFirstId, Instant.now(), 0);
        taskManager.addSubTask(subTaskSecond);

        assertNotNull(taskManager.getEpics(), "Задачи нe возвращаются.");
        assertNotNull(taskManager.getSubTasks(), "Задачи нe возвращаются.");
        assertEquals(1, epicFirstId, "Неверный id эпика");
        assertEquals(epicFirst.getStatus(), Status.IN_PROGRESS, "Неверно рассчитан статус эпика.");

        taskManager.deleteListSubTask();

        assertEquals(0, taskManager.getSubTasks().size(), "Задачи не удалены.");
        assertEquals(0, taskManager.getEpicSubTasks(epicFirstId).size(), "Сабтаски не удалены из эпика.");
        assertEquals(epicFirst.getStatus(), Status.NEW, "Неверно рассчитан статус эпика.");
    }

    @Test
    void shouldNoAddToHistory() {
        Task taskFirst = new Task(TypeOfTask.TASK, "Test addNewTask1", "Test addNewTask description1",
                Status.NEW, Instant.now(), 0);
        taskManager.addTask(taskFirst);
        Task taskSecond = new Task(TypeOfTask.TASK, "Test addNewTask2", "Test addNewTask description2",
                Status.IN_PROGRESS, Instant.now(), 0);
        taskManager.addTask(taskSecond);

        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic1", "Test addNewEpic description1",
                Status.NEW, Instant.now(), 0);
        final int epicFirstId = taskManager.addEpic(epicFirst).getId();
        Epic epicSecond = new Epic(TypeOfTask.EPIC, "Test addNewEpic2", "Test addNewEpic description2",
                Status.NEW, Instant.now(), 0);
        final int epicSecondId = taskManager.addEpic(epicSecond).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask1",
                "Test addNewSubTask1 description", Status.IN_PROGRESS, epicFirstId, Instant.now(), 0);
        taskManager.addSubTask(subTaskFirst);
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask2",
                "Test addNewSubTask2 description", Status.DONE, epicSecondId, Instant.now(), 0);
        taskManager.addSubTask(subTaskSecond);

        final List<Task> savedHistory = taskManager.getHistory();

        assertNotNull(taskManager.getHistory(), "История нe возвращается.");
        assertEquals(0, savedHistory.size(), "Неверное количество задач в истории.");
    }

    @Test
    void shouldAddToHistory() {
        Task taskFirst = new Task(TypeOfTask.TASK, "Test addNewTask1", "Test addNewTask description1",
                Status.NEW, Instant.now(), 0);
        final int taskFirstId = taskManager.addTask(taskFirst).getId();
        Task taskSecond = new Task(TypeOfTask.TASK, "Test addNewTask2", "Test addNewTask description2",
                Status.IN_PROGRESS, Instant.now(), 0);
        final int taskSecondId = taskManager.addTask(taskSecond).getId();

        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic1", "Test addNewEpic description1",
                Status.NEW, Instant.now(), 0);
        final int epicFirstId = taskManager.addEpic(epicFirst).getId();
        Epic epicSecond = new Epic(TypeOfTask.EPIC, "Test addNewEpic2", "Test addNewEpic description2",
                Status.NEW, Instant.now(), 0);
        final int epicSecondId = taskManager.addEpic(epicSecond).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask1",
                "Test addNewSubTask1 description", Status.IN_PROGRESS, epicFirstId, Instant.now(), 0);
        final int subTaskFirstId = taskManager.addSubTask(subTaskFirst).getId();
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask2",
                "Test addNewSubTask2 description", Status.DONE, epicSecondId, Instant.now(), 0);
        final int subTaskSecondId = taskManager.addSubTask(subTaskSecond).getId();

        taskManager.getTask(taskFirstId);
        taskManager.getTask(taskSecondId);
        taskManager.getTask(taskSecondId);
        taskManager.getTask(taskSecondId);
        taskManager.getTask(taskSecondId);
        taskManager.getEpic(epicFirstId);
        taskManager.getEpic(epicSecondId);
        taskManager.getSubTask(subTaskFirstId);
        taskManager.getSubTask(subTaskSecondId);

        assertNotNull(taskManager.getTasks(), "Задачи нe возвращаются.");
        assertNotNull(taskManager.getEpics(), "Эпики нe возвращаются.");
        assertNotNull(taskManager.getSubTasks(), "Сабтаски нe возвращаются.");
        assertEquals(3, epicFirstId, "Неверный id эпика");
        assertEquals(5, subTaskFirstId, "Неверный id сабтаска");
        assertEquals(epicFirst.getStatus(), Status.IN_PROGRESS, "Неверно рассчитан статус эпика.");

        final List<Task> savedHistory = taskManager.getHistory();

        assertNotNull(taskManager.getHistory(), "История нe возвращается.");
        assertEquals(6, savedHistory.size(), "Неверное количество задач в истории.");
    }

    @Test
    void shouldRemoveFromHistory() {
        Task taskFirst = new Task(TypeOfTask.TASK, "Test addNewTask1", "Test addNewTask description1",
                Status.NEW, Instant.now(), 0);
        final int taskFirstId = taskManager.addTask(taskFirst).getId();
        Task taskSecond = new Task(TypeOfTask.TASK, "Test addNewTask2", "Test addNewTask description2",
                Status.IN_PROGRESS, Instant.now(), 0);
        final int taskSecondId = taskManager.addTask(taskSecond).getId();

        Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic1", "Test addNewEpic description1",
                Status.NEW, Instant.now(), 0);
        final int epicFirstId = taskManager.addEpic(epicFirst).getId();
        Epic epicSecond = new Epic(TypeOfTask.EPIC, "Test addNewEpic2", "Test addNewEpic description2",
                Status.NEW, Instant.now(), 0);
        final int epicSecondId = taskManager.addEpic(epicSecond).getId();

        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask1",
                "Test addNewSubTask1 description", Status.IN_PROGRESS, epicFirstId, Instant.now(), 0);
        final int subTaskFirstId = taskManager.addSubTask(subTaskFirst).getId();
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask2",
                "Test addNewSubTask2 description", Status.DONE, epicSecondId, Instant.now(), 0);
        final int subTaskSecondId = taskManager.addSubTask(subTaskSecond).getId();

        taskManager.getTask(taskFirstId);
        taskManager.getTask(taskSecondId);
        taskManager.getTask(taskSecondId);
        taskManager.getTask(taskSecondId);
        taskManager.getTask(taskSecondId);
        taskManager.getEpic(epicFirstId);
        taskManager.getEpic(epicSecondId);
        taskManager.getSubTask(subTaskFirstId);
        taskManager.getSubTask(subTaskSecondId);

        assertNotNull(taskManager.getTasks(), "Задачи нe возвращаются.");
        assertNotNull(taskManager.getEpics(), "Эпики нe возвращаются.");
        assertNotNull(taskManager.getSubTasks(), "Сабтаски нe возвращаются.");
        assertEquals(3, epicFirstId, "Неверный id эпика");
        assertEquals(5, subTaskFirstId, "Неверный id сабтаска");
        assertEquals(epicFirst.getStatus(), Status.IN_PROGRESS, "Неверно рассчитан статус эпика.");

        final List<Task> savedHistory = taskManager.getHistory();

        savedHistory.remove(0);

        assertNotNull(taskManager.getHistory(), "История нe возвращается.");
        assertEquals(5, savedHistory.size(), "Неверное количество задач в истории.");
    }
}