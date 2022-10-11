import ru.practicum.kanban.client.KVTaskClient;
import ru.practicum.kanban.manager.FileBackedTasksManager;
import ru.practicum.kanban.manager.TaskManager;
import ru.practicum.kanban.server.HttpTaskServer;
import ru.practicum.kanban.server.KVServer;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
/*        TaskManager taskManager = FileBackedTasksManager.loadFromFile((Path.of("resources/file.csv")));
        new HttpTaskServer(taskManager).start();*/
        new KVServer().start();

    }
}