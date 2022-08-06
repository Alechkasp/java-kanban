package ru.practicum.kanban.manager;

import ru.practicum.kanban.models.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    protected LinkedList<Task> history = new LinkedList<>();
    public static final int NUMBER_OF_MOVIES_VIEWED = 10;

    //добавить задачу в список просмотренных
    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() >= NUMBER_OF_MOVIES_VIEWED) {
                history.remove(0);
                history.add(task);
            } else {
                history.add(task);
            }
        }
    }

    //получить список последних просмотренных задач
    @Override
    public List<Task> getHistory() {
        return history;
    }
}