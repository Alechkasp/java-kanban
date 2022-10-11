package ru.practicum.kanban.manager;

import ru.practicum.kanban.client.KVTaskClient;
import java.io.IOException;

public class HTTPTaskManager extends FileBackedTasksManager {
    private String urlStorage;
    private String lineSeparator = System.lineSeparator();

    public HTTPTaskManager(String urlStorage) throws IOException, InterruptedException {
        this.urlStorage = urlStorage;
        KVTaskClient kvTaskClient = new KVTaskClient(urlStorage);
    }


}


