package ru.practicum.kanban.manager;

public class Managers {
    public TaskManager getDefaultInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public FileBackedTasksManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager();
    }

    public  HTTPTaskManager getDefaultHTTPTaskManager() {
        return new HTTPTaskManager("http://localhost:8079");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}