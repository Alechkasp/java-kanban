package ru.practicum.kanban.manager;

import java.io.IOException;

public class Managers {
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

/*    public TaskManager getDefault() throws IOException, InterruptedException {
        return new HTTPTaskManager("http://localhost:8079/");
    }*/

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}