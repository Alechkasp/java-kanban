import ru.practicum.kanban.manager.HTTPTaskManager;
import ru.practicum.kanban.manager.Managers;
import ru.practicum.kanban.server.HttpTaskServer;
import ru.practicum.kanban.server.KVServer;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();

        HTTPTaskManager httpTaskManager = Managers.getDefaultHTTPTaskManager();

        HttpTaskServer httpTaskServer = new HttpTaskServer(httpTaskManager.loadFromFile(Path.of("resources/file.csv")));
        httpTaskServer.start();

        httpTaskManager.getApiToken();
        httpTaskManager.load();
    }
}