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
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private String urlStorage;
    private KVTaskClient kvTaskClient ;

    private final Gson gson = new GsonBuilder().
            registerTypeAdapter(Instant.class, new StartTimeAdapter()).create();

    public HTTPTaskManager(String urlStorage) {
        this.urlStorage = urlStorage;
    }

    public void getApiToken() throws IOException, InterruptedException {
        kvTaskClient = new KVTaskClient(urlStorage);
        System.out.println("url " + urlStorage);
        kvTaskClient.register();
    }

    public void save() throws IOException, InterruptedException {
        Managers managers = new Managers();
        FileBackedTasksManager fileBackedTasksManager = managers.getDefaultFileBackedTasksManager()
                .loadFromFile((Path.of("resources/file.csv")));
        kvTaskClient.put("/tasks/task", gson.toJson(fileBackedTasksManager.getTasks()));
        kvTaskClient.put("/tasks/epic", gson.toJson(fileBackedTasksManager.getEpics()));
        kvTaskClient.put("/tasks/subtask", gson.toJson(fileBackedTasksManager.getSubTasks()));
    }

    public void load() throws IOException, InterruptedException {
        String gsonStringTask = kvTaskClient.load("/tasks/task");
        Type type = new TypeToken<List<Task>>(){}.getType();
        List<Task> tasks = gson.fromJson(gsonStringTask, type);

        for (Task task : tasks) {
            addTaskFromKVServer(task);
        }

        String gsonStringEpic = kvTaskClient.load("/tasks/epic");
        type = new TypeToken<List<Epic>>(){}.getType();
        List<Epic> epics = gson.fromJson(gsonStringEpic, type);

        for (Epic epic : epics) {
            addEpicFromKVServer(epic);
        }

        String gsonStringSubTask = kvTaskClient.load("/tasks/subtask");
        type = new TypeToken<List<SubTask>>(){}.getType();
        List<SubTask> subTasks = gson.fromJson(gsonStringSubTask, type);

        for (SubTask subTask : subTasks) {
            addSubtaskFromKVServer(subTask);
        }

        System.out.println("tasks " + subTasks);
        System.out.println("epics " + epics);
        System.out.println("subtasks " + subTasks);
    }

    public Task addTaskFromKVServer(Task task) throws IOException, InterruptedException {
        id++;
        task.setId(id);
        addToPrioritizedTasksList(task);
        tableTasks.put(id, task);
        save();
        return task;
    }

    public Epic addEpicFromKVServer(Epic epic) throws IOException, InterruptedException {
        id++;
        epic.setId(id);
        tableEpics.put(id, epic);
        save();
        return epic;
    }

    public SubTask addSubtaskFromKVServer(SubTask subTask) throws IOException, InterruptedException {
        id++;
        subTask.setId(id);
        addToPrioritizedTasksList(subTask);
        tableSubTasks.put(id, subTask);
        getEpicForSubTask(subTask.getEpicId()).addSubTaskToEpic(id);
        updateEpicStatus(subTask.getEpicId());
        save();
        return subTask;
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
}