package ru.practicum.kanban.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.kanban.manager.TaskManager;
import ru.practicum.kanban.models.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;

public class JsonTaskManager {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder().
            registerTypeAdapter(Instant.class, new StartTimeAdapter()).create();
/*    private final Gson gson = new GsonBuilder().
            registerTypeAdapter(Instant.class, new StartTimeAdapter())
            .setPrettyPrinting()
            .serializeNulls().create();*/

    public JsonTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void getTasks(HttpExchange httpExchange) {
        String response = gson.toJson(taskManager.getTasks());

        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.sendResponseHeaders(200,0);
            os.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getEpics(HttpExchange httpExchange) {
        String response = gson.toJson(taskManager.getEpics());

        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.sendResponseHeaders(200,0);
            os.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getSubTasks(HttpExchange httpExchange) {
        String response = gson.toJson(taskManager.getSubTasks());

        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.sendResponseHeaders(200,0);
            os.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
