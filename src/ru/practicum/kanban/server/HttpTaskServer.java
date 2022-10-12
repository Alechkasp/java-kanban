package ru.practicum.kanban.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;

import com.sun.net.httpserver.HttpServer;
import ru.practicum.kanban.manager.Managers;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;

    public HttpTaskServer() throws IOException {
        Managers managers = new Managers();
        JsonTaskManager jsonTaskManager = new JsonTaskManager(managers.getDefaultFileBackedTasksManager()
                .loadFromFile((Path.of("resources/file.csv"))));
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
}