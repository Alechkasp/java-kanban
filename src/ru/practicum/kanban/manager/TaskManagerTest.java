package ru.practicum.kanban.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.kanban.models.*;

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
        void shouldAddNewEpic() {
            Epic epic = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                    Status.NEW);
            final int epicId = taskManager.addEpic(epic).getId();
            SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask1",
                    "Test addNewSubTask1 description", Status.DONE, epicId);
            taskManager.addSubTask(subTaskFirst);
            SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask2",
                    "Test addNewSubTask2 description", Status.IN_PROGRESS, epicId);
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
                    Status.NEW);
            final int epicId = taskManager.addEpic(epic).getId();

            SubTask subTask = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                    "Test addNewSubTask description", Status.NEW, epicId);
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
            Task taskFirst = new Task(TypeOfTask.TASK, "Test addNewTask", "Test addNewTask description",
                    Status.NEW);
            taskManager.addTask(taskFirst);
            Task taskSecond = new Task(TypeOfTask.TASK, "Test addNewTask", "Test addNewTask description",
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
            Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                    Status.NEW);
            taskManager.addEpic(epicFirst);
            Epic epicSecond = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                    Status.IN_PROGRESS);
            taskManager.addEpic(epicSecond);

            final List<Epic> epics = taskManager.getEpics();

            assertNotNull(epics, "Задачи нe возвращаются.");
            assertEquals(2, epics.size(), "Неверное количество задач.");
            assertEquals(epicFirst, epics.get(0), "Задачи не совпадают.");
            assertEquals(epicSecond, epics.get(1), "Задачи не совпадают.");
        }

        @Test
        void shouldGetListSubTasks() {
            Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                    Status.NEW);
            taskManager.addEpic(epicFirst);
            SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                    "Test addNewSubTask description", Status.NEW, epicFirst.getId());
            taskManager.addSubTask(subTaskFirst);
            SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                    "Test addNewSubTask description", Status.NEW, epicFirst.getId());
            taskManager.addSubTask(subTaskSecond);

            final List<SubTask> subTasks = taskManager.getSubTasks();

            assertNotNull(subTasks, "Задачи нe возвращаются.");
            assertNotNull(epicFirst, "Эпик не найден");
            assertEquals(2, subTasks.size(), "Неверное количество задач.");
            assertEquals(subTaskFirst, subTasks.get(0), "Задачи не совпадают.");
            assertEquals(subTaskSecond, subTasks.get(1), "Задачи не совпадают.");

            final List<Integer> subTaskIds = new ArrayList<>();;
            for (SubTask s : taskManager.getSubTasks()) {
                subTaskIds.add(s.getId());
            }

            assertEquals(epicFirst.getSubTasks(), subTaskIds, "Списки id подзадач эпика не совпадают");
        }

        @Test
        void shouldGetTask() {
            Task taskFirst = new Task(TypeOfTask.TASK, "Test addNewTask", "Test addNewTask description",
                    Status.NEW);
            taskManager.addTask(taskFirst);

            final int taskId = taskFirst.getId();

            final Task savedTask = taskManager.getTask(taskId);

            assertNotNull(savedTask, "Задача не найдена.");
            assertNotNull(taskManager.getTasks(), "Список тасков пустой");
            assertEquals(1, taskId, "Неверный id задачи");
            assertEquals(taskFirst, savedTask, "Задачи не совпадают.");

/*            HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
            inMemoryHistoryManager.add(taskFirst);

            final List<Task> history = inMemoryHistoryManager.getHistory();

            assertNotNull(history, "История пустая.");
            assertEquals(1, history.size(), "История не пустая.");*/
        }

        @Test
        void shouldGetEpic() {
            Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                    Status.NEW);
            taskManager.addEpic(epicFirst);
            SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                    "Test addNewSubTask description", Status.DONE, epicFirst.getId());
            taskManager.addSubTask(subTaskFirst);
            SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                    "Test addNewSubTask description", Status.DONE, epicFirst.getId());
            taskManager.addSubTask(subTaskSecond);

            final int epicId = epicFirst.getId();

            final Epic savedEpic = taskManager.getEpic(epicId);

            assertNotNull(savedEpic, "Задача не найдена.");
            assertNotNull(taskManager.getEpics(), "Список эпиков пустой");
            assertEquals(1, epicId, "Неверный id задачи");
            assertEquals(epicFirst, savedEpic, "Задачи не совпадают.");
            assertEquals(epicFirst.getStatus(), Status.DONE, "Неверно рассчитан статус Эпика.");

            final List<Integer> subTaskIds = new ArrayList<>();
            for (SubTask s : taskManager.getSubTasks()) {
                subTaskIds.add(s.getId());
            }
            assertEquals(epicFirst.getSubTasks(), subTaskIds, "Неправильный список id подзадач эпика");

/*            HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
            inMemoryHistoryManager.add(epicFirst);

            final List<Task> history = inMemoryHistoryManager.getHistory();

            assertNotNull(history, "История пустая.");
            assertEquals(1, history.size(), "История не пустая.");*/
        }

        @Test
        void shouldGetSubTask() {
            Epic epicFirst = new Epic(TypeOfTask.EPIC, "Test addNewEpic", "Test addNewEpic description",
                    Status.NEW);
            taskManager.addEpic(epicFirst);
            SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                    "Test addNewSubTask description", Status.IN_PROGRESS, epicFirst.getId());
            taskManager.addSubTask(subTaskFirst);

            final int subTaskId = subTaskFirst.getId();

            final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

            assertNotNull(savedSubTask, "Задача не найдена.");
            assertNotNull(taskManager.getSubTasks(), "Список сабтасков пустой");
            assertNotNull(epicFirst, "Эпик не найден");
            assertEquals(2, subTaskId, "Неверный id задачи");
            assertEquals(subTaskFirst, savedSubTask, "Задачи не совпадают.");
            assertEquals(epicFirst.getStatus(), Status.IN_PROGRESS, "Неверно расчитан статус Эпика");

/*            HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
            inMemoryHistoryManager.add(subTaskFirst);

            final List<Task> history = inMemoryHistoryManager.getHistory();

            assertNotNull(history, "История пустая.");
            assertEquals(1, history.size(), "История не пустая.");*/
        }

        @Test
        void shouldUpdateTask() {
            Task taskFirst = new Task(TypeOfTask.TASK, "Test addNewTask", "Test addNewTask description",
                    Status.NEW);
            taskManager.addTask(taskFirst);

            final int taskId = taskFirst.getId();

            Task taskUpdate = new Task(TypeOfTask.TASK, "Test updateTask", "Test updateTask description",
                    Status.IN_PROGRESS);
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
                    Status.NEW);
            taskManager.addEpic(epicFirst);

            final int epicId = epicFirst.getId();

            Epic epicUpdate = new Epic(TypeOfTask.EPIC, "Test updateEpic", "Test updateEpic description",
                    Status.IN_PROGRESS);
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
                    Status.NEW);
            taskManager.addEpic(epicFirst);
            SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                    "Test addNewSubTask description", Status.NEW, epicFirst.getId());
            taskManager.addSubTask(subTaskFirst);

            final int subTaskId = subTaskFirst.getId();

            SubTask subTaskUpdate = new SubTask(TypeOfTask.SUBTASK, "Test updateSubTask",
                    "Test updateSubTask description", Status.IN_PROGRESS, epicFirst.getId());
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
                    Status.NEW);
            final int taskId = taskManager.addTask(taskFirst).getId();
            Task taskSecond = new Task(TypeOfTask.TASK, "Test addNewTask2", "Test addNewTask description2",
                    Status.IN_PROGRESS);
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
                Status.NEW);
        final int epicId = taskManager.addEpic(epicFirst).getId();
        Epic epicSecond = new Epic(TypeOfTask.EPIC, "Test addNewEpic2", "Test addNewEpic description2",
                Status.NEW);
        taskManager.addEpic(epicSecond);
        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask",
                "Test addNewSubTask description", Status.NEW, epicId);
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
                Status.NEW);
        taskManager.addEpic(epicFirst);
        SubTask subTaskFirst = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask1",
                "Test addNewSubTask1 description", Status.IN_PROGRESS, epicFirst.getId());
        final int subTaskId = taskManager.addSubTask(subTaskFirst).getId();
        SubTask subTaskSecond = new SubTask(TypeOfTask.SUBTASK, "Test addNewSubTask2",
                "Test addNewSubTask2 description", Status.DONE, epicFirst.getId());
        taskManager.addSubTask(subTaskSecond);

        taskManager.delSubTask(subTaskId);

        final List<SubTask> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Задачи нe возвращаются.");
        assertEquals(2, subTaskId, "Неверный id задачи");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTaskSecond, subTasks.get(0), "Задачи не совпадают.");
        assertEquals(epicFirst.getStatus(), Status.DONE, "Неверно рассчитан статус эпика.");
    }
}