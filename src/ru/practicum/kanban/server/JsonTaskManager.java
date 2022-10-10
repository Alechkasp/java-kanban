package ru.practicum.kanban.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.kanban.manager.TaskManager;
import ru.practicum.kanban.models.Epic;
import ru.practicum.kanban.models.SubTask;
import ru.practicum.kanban.models.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class JsonTaskManager {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder().
            registerTypeAdapter(Instant.class, new StartTimeAdapter()).create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public JsonTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void getTasks(HttpExchange httpExchange) throws IOException {
        String response = "";
        int statusCode = 404;
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getRawQuery();

        //getTasks
        if (path.endsWith("/task") && method.equals("GET")) {
            statusCode = 200;
            response = gson.toJson(taskManager.getTasks());
        }

        //getTask(id)
        if ((query != null) && (method.equals("GET"))) {
            int id = Integer.parseInt(query.split("=")[1]);
            statusCode = 200;
            response = gson.toJson(taskManager.getTask(id));
        }

        //addTask
        if (path.endsWith("/task") && method.equals("POST")) {
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Task task = gson.fromJson(body, Task.class);
            statusCode = 200;
            response = gson.toJson(taskManager.addTask(task));
        }

        //delTask(id)
        if ((query != null) && (method.equals("DELETE"))) {
            int id = Integer.parseInt(query.split("=")[1]);
            statusCode = 200;
            taskManager.delTask(id);
            response = "Задача удалена!";
        }

        //getTasks + getEpics + getSubTasks
        if (path.endsWith("/tasks/") && method.equals("GET")) {
            statusCode = 200;
            response = gson.toJson(taskManager.getTasks()) + gson.toJson(taskManager.getEpics())
                    + gson.toJson(taskManager.getSubTasks());
        }

        //getHistory
        if (path.endsWith("/history") && method.equals("GET")) {
            statusCode = 200;
            response = gson.toJson(taskManager.getHistory());
        }

        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=" + StandardCharsets.UTF_8);
            httpExchange.sendResponseHeaders(statusCode, 0);
            os.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getEpics(HttpExchange httpExchange) throws IOException {
        String response = "";
        int statusCode = 404;
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getRawQuery();

        //getEpics
        if (path.endsWith("/epic") && method.equals("GET")) {
            statusCode = 200;
            response = gson.toJson(taskManager.getEpics());
        }

        //getEpic(id)
        if ((query != null) && (method.equals("GET"))) {
            int id = Integer.parseInt(query.split("=")[1]);
            statusCode = 200;
            response = gson.toJson(taskManager.getEpic(id));
        }

        //addEpic
        if (path.endsWith("/epic") && method.equals("POST")) {
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Epic epic = gson.fromJson(body, Epic.class);
            statusCode = 200;
            response = gson.toJson(taskManager.addEpic(epic));
            System.out.println("response " + response);
        }

        //delEpic(id)
        if ((query != null) && (method.equals("DELETE"))) {
            int id = Integer.parseInt(query.split("=")[1]);
            statusCode = 200;
            taskManager.delEpic(id);
            response = "Эпик удален!";
        }

        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=" + StandardCharsets.UTF_8);
            httpExchange.sendResponseHeaders(statusCode, 0);
            os.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getSubTasks(HttpExchange httpExchange) throws IOException {
        String response = "";
        int statusCode = 404;
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getRawQuery();

        //getSubTasks
        if (path.endsWith("/subtask") && method.equals("GET")) {
            statusCode = 200;
            response = gson.toJson(taskManager.getSubTasks());
        }

        //getSubTask(id)
        if ((query != null) && (method.equals("GET"))) {
            int id = Integer.parseInt(query.split("=")[1]);
            statusCode = 200;
            response = gson.toJson(taskManager.getSubTask(id));
        }

        //addSubTask
        if (path.endsWith("/subtask") && method.equals("POST")) {
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            SubTask subTask = gson.fromJson(body, SubTask.class);
            statusCode = 200;
            response = gson.toJson(taskManager.addSubTask(subTask));
        }

        //delSubTask(id)
        if ((query != null) && (method.equals("DELETE"))) {
            int id = Integer.parseInt(query.split("=")[1]);
            statusCode = 200;
            taskManager.delSubTask(id);
            response = "Сабтаск удален!";
        }

        //getEpicSubTasks
        if (path.endsWith("/epic/") && (query != null) && method.equals("GET")) {
            int id = Integer.parseInt(query.split("=")[1]);
            statusCode = 200;
            response = gson.toJson(taskManager.getEpic(id).getSubTasks());
        }

        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=" + StandardCharsets.UTF_8);
            httpExchange.sendResponseHeaders(statusCode, 0);
            os.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
