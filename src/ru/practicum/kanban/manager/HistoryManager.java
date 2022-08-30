package ru.practicum.kanban.manager;

import ru.practicum.kanban.models.Task;

import java.util.List;

public interface HistoryManager {

    //добавить задачу в список просмотренных
    void add(Task task);

    //удалить задачу из списка просмотренных
    void remove(int id);

    //получить список последних просмотренных задач
    List<Task> getHistory();
}