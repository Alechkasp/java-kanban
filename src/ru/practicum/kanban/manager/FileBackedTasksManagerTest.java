package ru.practicum.kanban.manager;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager());
    }
}