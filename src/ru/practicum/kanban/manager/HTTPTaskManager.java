package ru.practicum.kanban.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.practicum.kanban.client.KVTaskClient;
import ru.practicum.kanban.models.Epic;
import ru.practicum.kanban.models.SubTask;
import ru.practicum.kanban.models.Task;
import ru.practicum.kanban.server.StartTimeAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final String urlStorage;
    private KVTaskClient kvTaskClient;

    private final Gson gson = new GsonBuilder().
            registerTypeAdapter(Instant.class, new StartTimeAdapter()).create();

    public HTTPTaskManager(String urlStorage) {
        this.urlStorage = urlStorage;
    }

    public void getApiToken() throws IOException, InterruptedException {
        kvTaskClient = new KVTaskClient(urlStorage);
        kvTaskClient.register();
    }

    public void save() throws IOException, InterruptedException {
        kvTaskClient.put("/tasks/task", gson.toJson(getTasks()));
        kvTaskClient.put("/tasks/epic", gson.toJson(getEpics()));
        kvTaskClient.put("/tasks/subtask", gson.toJson(getSubTasks()));
        kvTaskClient.put("/tasks/history", gson.toJson(inMemoryHistoryManager.getHistory()));
    }

    public void load() throws IOException, InterruptedException {
        String gsonStringTask = kvTaskClient.load("/tasks/task");
        Type type = new TypeToken<List<Task>>() {}.getType();
        List<Task> tasks = gson.fromJson(gsonStringTask, type);

        for (Task task : tasks) {
            addTaskFromKVServer(task);
        }

        String gsonStringEpic = kvTaskClient.load("/tasks/epic");
        type = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epics = gson.fromJson(gsonStringEpic, type);

        for (Epic epic : epics) {
            addEpicFromKVServer(epic);
        }

        String gsonStringSubTask = kvTaskClient.load("/tasks/subtask");
        type = new TypeToken<List<SubTask>>() {
        }.getType();
        List<SubTask> subTasks = gson.fromJson(gsonStringSubTask, type);

        for (SubTask subTask : subTasks) {
            addSubtaskFromKVServer(subTask);
        }

        String gsonStringHistory = kvTaskClient.load("/tasks/history");
        Type typeHistory = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> history = gson.fromJson(gsonStringHistory, typeHistory);

        for (Task task : history) {
            inMemoryHistoryManager.add(task);
        }
    }

    public void addTaskFromKVServer(Task task) throws IOException, InterruptedException {
        id++;
        task.setId(id);
        addToPrioritizedTasksList(task);
        tableTasks.put(id, task);
        save();
    }

    public void addEpicFromKVServer(Epic epic) throws IOException, InterruptedException {
        id++;
        epic.setId(id);
        tableEpics.put(id, epic);
        save();
    }

    public void addSubtaskFromKVServer(SubTask subTask) throws IOException, InterruptedException {
        id++;
        subTask.setId(id);
        addToPrioritizedTasksList(subTask);
        tableSubTasks.put(id, subTask);
        getEpicForSubTask(subTask.getEpicId()).addSubTaskToEpic(id);
        updateEpicStatus(subTask.getEpicId());
        save();
    }

    @Override
    public Task addTask(Task task) {
        try {
            save();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return super.addTask(task);
    }

    @Override
    public Epic addEpic(Epic epic) {
        try {
            save();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return super.addEpic(epic);
    }

    @Override
    public SubTask addSubTask(SubTask subTask) {
        try {
            save();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return super.addSubTask(subTask);
    }

    @Override
    public void updateTask(Task task) {
        try {
            save();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.updateTask(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        try {
            save();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.updateEpic(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        try {
            save();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.updateSubTask(subTask);
    }

    @Override
    public void delTask(int id) {
        try {
            save();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.delTask(id);
    }

    @Override
    public void delEpic(int id) {
        try {
            save();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.delEpic(id);
    }

    @Override
    public void delSubTask(int id) {
        try {
            save();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.delSubTask(id);
    }
}