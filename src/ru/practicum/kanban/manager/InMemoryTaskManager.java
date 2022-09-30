package ru.practicum.kanban.manager;

import ru.practicum.kanban.models.Epic;
import ru.practicum.kanban.models.Status;
import ru.practicum.kanban.models.SubTask;
import ru.practicum.kanban.models.Task;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    int id = 0;
    protected Map<Integer, Task> tableTasks = new HashMap<>();
    protected Map<Integer, Epic> tableEpics = new HashMap<>();
    protected Map<Integer, SubTask> tableSubTasks = new HashMap<>();
    protected HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
/*    protected static long EARLY_SUBTASK_TIME_STATUS_NEW = 2_000_000_000_000L;
    protected static long LATE_SUBTASK_TIME_STATUS_NEW = 0L;
    protected static long EARLY_SUBTASK_TIME_STATUS_DONE = 2_000_000_000_000L;
    protected static long LATE_SUBTASK_TIME_STATUS_DONE = 0L;*/

    //добавить задачу типа Task
    @Override
    public Task addTask(Task task) {
        id++;
        task.setId(id);
        tableTasks.put(id, task);
        return task;
    }

    //добавить задачу типа Epic
    @Override
    public Epic addEpic(Epic epic) {
        id++;
        epic.setId(id);
        tableEpics.put(id, epic);
        return epic;
    }

    //добавить задачу SubTask
    @Override
    public SubTask addSubTask(SubTask subTask) {
        id++;
        subTask.setId(id);
        tableSubTasks.put(id, subTask);
        getEpicForSubTask(subTask.getEpicId()).addSubTaskToEpic(id);
        updateEpicStatus(subTask.getEpicId());
        return subTask;
    }

    //получить список все задач типа Task
    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Integer task : tableTasks.keySet()) {
            tasks.add(tableTasks.get(task));
        }
        return tasks;
    }

    //получить список все задач типа Epic
    @Override
    public List<Epic> getEpics() {
        List<Epic> epics = new ArrayList<>();
        for (Integer epic : tableEpics.keySet()) {
            epics.add(tableEpics.get(epic));
        }
        return epics;
    }

    //получить список все задач типа SubTask
    @Override
    public List<SubTask> getSubTasks() {
        List<SubTask> subTasks = new ArrayList<>();
        for (Integer subTask : tableSubTasks.keySet()) {
            subTasks.add(tableSubTasks.get(subTask));
        }
        return subTasks;
    }

    //удалить все задачи типа Task
    @Override
    public void deleteListTasks() {
        tableTasks.clear();
    }

    //удалить все задачи типа Epic
    @Override
    public void deleteListEpics() {
        tableEpics.clear();
        tableSubTasks.clear();
    }

    //удалить все задачи типа SubTask
    @Override
    public void deleteListSubTask() {
        for (Epic e : tableEpics.values()) {
            e.clearSubTasksIds();
            updateEpicStatus(e.getId());
        }
        tableSubTasks.clear();
    }

    //получение по идентификатору задачи типа Task
    @Override
    public Task getTask(int id) {
        inMemoryHistoryManager.add(tableTasks.get(id));
        return tableTasks.get(id);
    }

    //получение по идентификатору задачи типа Epic
    @Override
    public Epic getEpic(int id) {
        inMemoryHistoryManager.add(tableEpics.get(id));
        return tableEpics.get(id);
    }

    //получение по идентификатору задачи типа Epic при создании subTask
    public Epic getEpicForSubTask(int id) {
        return tableEpics.get(id);
    }

    //получение по идентификатору задачи типа SubTask
    @Override
    public SubTask getSubTask(int id) {
        inMemoryHistoryManager.add(tableSubTasks.get(id));
        return tableSubTasks.get(id);
    }

    //обновление задачи типа Task
    @Override
    public void updateTask(Task task) {
        if (tableTasks.containsKey(task.getId())) {
            tableTasks.put(task.getId(), task);
        }
    }

    //обновление задачи типа Epic
    @Override
    public void updateEpic(Epic epic) {
        tableEpics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    //обновление задачи типа SubTask
    @Override
    public void updateSubTask(SubTask subTask) {
        if (tableSubTasks.containsKey(subTask.getId())) {
            if (tableEpics.containsKey(subTask.getEpicId())) {
                tableSubTasks.put(subTask.getId(), subTask);
                updateEpicStatus(tableEpics.get(subTask.getEpicId()).getId());
            }
        }
    }

    //удаление по идентификатору задачи типа Task
    @Override
    public void delTask(int id) {
        inMemoryHistoryManager.remove(id);
        tableTasks.remove(id);
    }

    //удаление по идентификатору задачи типа Epic
    @Override
    public void delEpic(int id) {
        for (Integer subTask : getEpic(id).getSubTasks()) {
            if (tableSubTasks.containsKey(subTask)) {
                inMemoryHistoryManager.remove(tableSubTasks.get(subTask).getId());
                tableSubTasks.remove(subTask);
            }
        }
        inMemoryHistoryManager.remove(id);
        tableEpics.remove(id);
    }

    //удаление по идентификатору задачи типа SubTask
    @Override
    public void delSubTask(int id) {
        if (getSubTasks().contains(tableSubTasks.get(id))) {
            getEpic(tableSubTasks.get(id).getEpicId()).delSubTaskFromEpic(id);
            updateEpic(getEpic(tableSubTasks.get(id).getEpicId()));
            inMemoryHistoryManager.remove(id);
            tableSubTasks.remove(id);
        }
    }

    //получение списка всех подзадач определённого эпика
    @Override
    public List<SubTask> getEpicSubTasks(int id) {
        List<SubTask> subTaskFromEpic = new ArrayList<>();
        for (Integer subTask : tableSubTasks.keySet()) {
            for (int i = 0; i < getEpic(id).getSubTasks().size(); i++) {
                if (subTask.equals(getEpic(id).getSubTasks().get(i))) {
                    subTaskFromEpic.add(tableSubTasks.get(subTask));
                }
            }
        }
        return subTaskFromEpic;
    }

    private void updateEpicStatus(int epicId) {
        int countNew = 0;
        int countDone = 0;
/*        Instant startTimeNew = Instant.ofEpochMilli(tableSubTasks.get(0).getStartTime());
        Instant endTimeNew = Instant.ofEpochMilli(tableSubTasks.get(0).getEndTime());*/

        for (Integer subTask : tableSubTasks.keySet()) {
            if (getEpicForSubTask(epicId).getSubTasks().contains(subTask)) {
                if (tableSubTasks.get(subTask).getStatus().equals(Status.NEW)) {
                    countNew++;
                }
/*                if (Instant.ofEpochMilli(tableSubTasks.get(subTask).getStartTime()).isBefore(startTimeNew)) {
                    startTimeNew = Instant.ofEpochMilli(tableSubTasks.get(subTask).getStartTime());
                }
                if (Instant.ofEpochMilli(tableSubTasks.get(subTask).getEndTime()).isAfter(endTimeNew)) {
                    endTimeNew = Instant.ofEpochMilli(tableSubTasks.get(subTask).getEndTime());
                }*/
            }
        }

        for (Integer subTask : tableSubTasks.keySet()) {
            if (getEpicForSubTask(epicId).getSubTasks().contains(subTask)) {
                if (tableSubTasks.get(subTask).getStatus().equals(Status.DONE)) {
                    countDone++;
                }
//                if (tableSubTasks.get(subTask).getDuration() < EARLY_SUBTASK_TIME_STATUS_DONE) {
//                    EARLY_SUBTASK_TIME_STATUS_DONE = tableSubTasks.get(subTask).getDuration();
//                }
//                if (tableSubTasks.get(subTask).getDuration() > LATE_SUBTASK_TIME_STATUS_DONE) {
//                    LATE_SUBTASK_TIME_STATUS_DONE = tableSubTasks.get(subTask).getDuration();
//                }
            }
        }

        //если у эпика нет подзадач
        boolean withoutSubTask = getEpicForSubTask(epicId).getSubTasks().isEmpty();
        //если подзадачи у эпика есть и все имеют статус "NEW"
        boolean withSubTaskStatusNew = countNew == getEpicForSubTask(epicId).getSubTasks().size();
        //если все подзадачи у эпика имеют статус "DONE"
        boolean withSubTaskStatusDone = countDone == getEpicForSubTask(epicId).getSubTasks().size();

        if (withoutSubTask || withSubTaskStatusNew) {
            getEpicForSubTask(epicId).setStatus(Status.NEW);
/*            endTime = EARLY_SUBTASK_TIME_STATUS_NEW +

            long SECONDS_IN_MINUTE = 60L;
            return startTime.plusSeconds(duration * SECONDS_IN_MINUTE).toEpochMilli();

            getEpicForSubTask(epicId).setDuration();*/
        } else if (withSubTaskStatusDone) {
            getEpicForSubTask(epicId).setStatus(Status.DONE);
        } else {
            getEpicForSubTask(epicId).setStatus(Status.IN_PROGRESS);
        }
    }

    //получить список последних просмотренных задач
    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}