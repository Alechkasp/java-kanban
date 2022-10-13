package ru.practicum.kanban.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.practicum.kanban.manager.FileBackedTasksManager;
import ru.practicum.kanban.manager.HTTPTaskManager;
import ru.practicum.kanban.manager.Managers;
import ru.practicum.kanban.models.Epic;
import ru.practicum.kanban.models.SubTask;
import ru.practicum.kanban.models.Task;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer server;
    private final FileBackedTasksManager fileBackedTasksManager;
    private final Gson gson = new GsonBuilder().
            registerTypeAdapter(Instant.class, new StartTimeAdapter()).create();

    public HttpTaskServer(FileBackedTasksManager fileBackedTasksManager) throws IOException {
        this.fileBackedTasksManager = fileBackedTasksManager;
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TasksHandler());
    }

    class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks запроса от клиента");
            String response = "";
            int statusCode = 404;
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String query = httpExchange.getRequestURI().getRawQuery();

            switch (method) {
                case "GET":
                    //getTasks
                    if (Pattern.matches("^/tasks/task$", path)) {
                        statusCode = 200;
                        response = gson.toJson(fileBackedTasksManager.getTasks());
                    }

                    //getEpics
                    if (Pattern.matches("^/tasks/epic$", path)) {
                        statusCode = 200;
                        response = gson.toJson(fileBackedTasksManager.getEpics());
                    }

                    //getSubTasks
                    if (Pattern.matches("^/tasks/subtask$", path)) {
                        statusCode = 200;
                        response = gson.toJson(fileBackedTasksManager.getSubTasks());
                    }

                    //getTask(id)
                    if (Pattern.matches("^/tasks/task/$", path) && (query != null)) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        statusCode = 200;
                        response = gson.toJson(fileBackedTasksManager.getTask(id));
                    }

                    //getEpic(id)
                    if (Pattern.matches("^/tasks/epic/", path) && (query != null)) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        statusCode = 200;
                        response = gson.toJson(fileBackedTasksManager.getEpic(id));
                    }

                    //getSubTask(id)
                    if (Pattern.matches("^/tasks/subtask/", path) && (query != null)) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        statusCode = 200;
                        response = gson.toJson(fileBackedTasksManager.getSubTask(id));
                    }

                    //getTasks + getEpics + getSubTasks
                    if (Pattern.matches("^/tasks$", path)) {
                        statusCode = 200;
                        response = gson.toJson(fileBackedTasksManager.getTasks())
                                + gson.toJson(fileBackedTasksManager.getEpics())
                                + gson.toJson(fileBackedTasksManager.getSubTasks());
                    }

                    //getHistory
                    if (Pattern.matches("^/tasks/history$", path)) {
                        statusCode = 200;
                        response = gson.toJson(fileBackedTasksManager.getHistory());
                    }

                    //getEpicSubTasks
                    if (Pattern.matches("^/tasks/subtask/epic/", path) && (query != null)) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        statusCode = 200;
                        response = gson.toJson(fileBackedTasksManager.getEpic(id).getSubTasks());
                    }

                    break;

                case "POST":
                    //addTask
                    if (Pattern.matches("^/tasks/task", path)) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(body, Task.class);
                        statusCode = 201;
                        fileBackedTasksManager.addTask(task);
                        response = "Задача создана!";
                    }

                    //addEpic
                    if (Pattern.matches("^/tasks/epic", path)) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(body, Epic.class);
                        statusCode = 201;
                        fileBackedTasksManager.addEpic(epic);
                        response = "Эпик создан!";
                    }

                    //addSubTask
                    if (Pattern.matches("^/tasks/subtask", path)) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        SubTask subTask = gson.fromJson(body, SubTask.class);
                        statusCode = 201;
                        fileBackedTasksManager.addSubTask(subTask);
                        response = "Сабтаск создан!";
                    }

                    break;

                case "DELETE":
                    //delTask(id)
                    if (Pattern.matches("^/tasks/task/", path) && (query != null)) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        statusCode = 200;
                        fileBackedTasksManager.delTask(id);
                        response = "Задача удалена!";
                    }

                    //delEpic(id)
                    if (Pattern.matches("^/tasks/epic/", path) && (query != null)) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        statusCode = 200;
                        fileBackedTasksManager.delEpic(id);
                        response = "Эпик удален!";
                    }

                    //delSubTask(id)
                    if (Pattern.matches("^/tasks/subtask/", path) && (query != null)) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        statusCode = 200;
                        fileBackedTasksManager.delSubTask(id);
                        response = "Сабтаск удален!";
                    }

                    break;

                default:
                    System.out.println("Такого метода нет");
            }

            httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=" + StandardCharsets.UTF_8);
            httpExchange.sendResponseHeaders(statusCode, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        HTTPTaskManager httpTaskManager = Managers.getDefaultHTTPTaskManager();

        HttpTaskServer httpTaskServer = new HttpTaskServer(httpTaskManager.loadFromFile(Path.of("resources/file.csv")));
        httpTaskServer.start();

        httpTaskManager.getApiToken();
        //httpTaskManager.save();
        httpTaskManager.load();
    }
}