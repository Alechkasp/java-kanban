import ru.practicum.kanban.manager.HTTPTaskManager;
import ru.practicum.kanban.manager.Managers;
import ru.practicum.kanban.server.HttpTaskServer;
import ru.practicum.kanban.server.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Managers managers = new Managers();
        HTTPTaskManager httpTaskManager = managers.getDefaultHTTPTaskManager();
        httpTaskManager.getApiToken();
        httpTaskManager.save();
        httpTaskManager.load();
        //httpTaskServer.stop();
    }
}