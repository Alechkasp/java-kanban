package ru.practicum.kanban.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import ru.practicum.kanban.manager.FileBackedTasksManager;
import ru.practicum.kanban.manager.HTTPTaskManager;
import ru.practicum.kanban.manager.Managers;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;

    public HttpTaskServer(FileBackedTasksManager fileBackedTasksManager) throws IOException {
        JsonTaskManager jsonTaskManager = new JsonTaskManager(fileBackedTasksManager);
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/", jsonTaskManager::getTasks);
        server.createContext("/tasks/epic", jsonTaskManager::getEpics);
        server.createContext("/tasks/subtask", jsonTaskManager::getSubTasks);
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

        HttpTaskServer httpTaskServer = new HttpTaskServer(httpTaskManager);
        httpTaskServer.start();

        httpTaskManager.getApiToken();
        //httpTaskManager.save();
        httpTaskManager.load();
    }
}