package ru.practicum.kanban.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.kanban.models.*;
import ru.practicum.kanban.server.HttpStatusCode;
import ru.practicum.kanban.server.HttpTaskServer;
import ru.practicum.kanban.server.KVServer;
import ru.practicum.kanban.server.StartTimeAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HTTPTaskManagerTest {
    private HTTPTaskManager httpTaskManager;
    private HttpTaskServer httpTaskServer;
    private Task taskOne;
    private Epic epicOne;
    private Epic epicTwo;
    private SubTask subTaskOne;
    private final Gson gson = new GsonBuilder().
            registerTypeAdapter(Instant.class, new StartTimeAdapter()).create();

    @BeforeAll
    static void startKVServer() {
        try {
            new KVServer().start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        httpTaskManager = Managers.getDefaultHTTPTaskManager();
        httpTaskServer = new HttpTaskServer(httpTaskManager);

        httpTaskServer.start();
        httpTaskManager.getApiToken();

        taskOne = new Task(TypeOfTask.TASK, "Новая задача 1", "Описание первой задачи", Status.NEW,
                Instant.ofEpochMilli(1662016210000L), 2);
        httpTaskManager.addTask(taskOne);
        Task taskTwo = new Task(TypeOfTask.TASK, "Новая задача 2", "Описание второй задачи", Status.NEW,
                Instant.ofEpochMilli(1662019810000L), 10);
        httpTaskManager.addTask(taskTwo);

        epicOne = new Epic(TypeOfTask.EPIC, "Новый эпик 1", "Описание первого эпика", Status.NEW);
        httpTaskManager.addEpic(epicOne);
        epicTwo = new Epic(TypeOfTask.EPIC, "Новый эпик 2", "Описание второго эпика", Status.NEW);
        httpTaskManager.addEpic(epicTwo);

        subTaskOne = new SubTask(TypeOfTask.SUBTASK, "Новый сабтаск 1", "Описание первого сабтаска",
                Status.DONE, epicOne.getId(), Instant.ofEpochMilli(1662023410000L), 10);
        httpTaskManager.addSubTask(subTaskOne);
        SubTask subTaskTwo = new SubTask(TypeOfTask.SUBTASK, "Новый сабтаск 2", "Описание второго сабтаска",
                Status.IN_PROGRESS, epicOne.getId(), Instant.ofEpochMilli(1662027010000L), 15);
        httpTaskManager.addSubTask(subTaskTwo);
        SubTask subTaskThree = new SubTask(TypeOfTask.SUBTASK, "Новый сабтаск 3", "Описание третьего сабтаска",
                Status.NEW, epicTwo.getId(), Instant.ofEpochMilli(1664627100000L), 15);
        httpTaskManager.addSubTask(subTaskThree);
    }

    @AfterEach
    void stop() {
        httpTaskServer.stop();
    }

    @Test
    void addTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        Task task = new Task(TypeOfTask.TASK, "Новая задача", "Описание задачи", Status.NEW,
                Instant.ofEpochMilli(1662905418000L), 10);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task))).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatusCode.CREATED.getValue(), response.statusCode());

        assertEquals(3, httpTaskManager.getTasks().size(), "Размер списка неверный");
    }

    @Test
    void addEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        Epic epic = new Epic(TypeOfTask.EPIC, "Новый эпик", "Описание эпика", Status.NEW);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic))).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatusCode.CREATED.getValue(), response.statusCode());

        assertEquals(3, httpTaskManager.getEpics().size(), "Размер списка неверный");
    }

    @Test
    void addSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        SubTask subTask = new SubTask(TypeOfTask.SUBTASK, "Новый сабтаск", "Описание сабтаска",
                Status.IN_PROGRESS, epicTwo.getId(), Instant.ofEpochMilli(1664628600000L), 10);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask))).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatusCode.CREATED.getValue(), response.statusCode());

        assertEquals(4, httpTaskManager.getSubTasks().size(), "Размер списка неверный");
        assertEquals(epicTwo.getStatus(), Status.IN_PROGRESS);
        assertEquals(epicTwo.getStartTime(), Instant.ofEpochMilli(1664627100000L));
        assertEquals(epicTwo.getDuration(), 35);
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatusCode.OK.getValue(), response.statusCode());

        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task task = gson.fromJson(response.body(), taskType);

        assertNotNull(task, "Таск не вернулся");
        assertEquals(taskOne, task);
    }

    @Test
    void getEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatusCode.OK.getValue(), response.statusCode());

        Type epicType = new TypeToken<Epic>() {
        }.getType();
        Epic epic = gson.fromJson(response.body(), epicType);

        assertNotNull(epic, "Эпик не вернулся");
        assertEquals(epicOne, epic);
    }

    @Test
    void getSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatusCode.OK.getValue(), response.statusCode());

        Type subTaskType = new TypeToken<SubTask>() {
        }.getType();
        SubTask subTask = gson.fromJson(response.body(), subTaskType);

        assertNotNull(subTask, "Сабтаск не вернулся");
        assertEquals(subTaskOne, subTask);
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatusCode.OK.getValue(), response.statusCode());

        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> listTask = gson.fromJson(response.body(), taskType);

        assertNotNull(listTask, "Таски не вернулись");
        assertEquals(2, listTask.size());
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatusCode.OK.getValue(), response.statusCode());

        Type epicType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> listEpic = gson.fromJson(response.body(), epicType);

        assertNotNull(listEpic, "Эпики не вернулись");
        assertEquals(2, listEpic.size());
    }

    @Test
    void getSubTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatusCode.OK.getValue(), response.statusCode());

        Type subTaskType = new TypeToken<List<SubTask>>() {
        }.getType();
        List<SubTask> listSubTask = gson.fromJson(response.body(), subTaskType);

        assertNotNull(listSubTask, "Сабтаски не вернулись");
        assertEquals(3, listSubTask.size());
    }

    @Test
    void delTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatusCode.OK.getValue(), response.statusCode());

        assertEquals(httpTaskManager.getTasks().size(), 1);
    }

    @Test
    void delEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatusCode.OK.getValue(), response.statusCode());

        assertEquals(httpTaskManager.getEpics().size(), 1);
    }

    @Test
    void delSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpStatusCode.OK.getValue(), response.statusCode());

        assertEquals(httpTaskManager.getSubTasks().size(), 2);
    }
}