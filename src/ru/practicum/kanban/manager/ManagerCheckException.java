package ru.practicum.kanban.manager;

public class ManagerCheckException extends RuntimeException {
    public ManagerCheckException(final String message) {
        super(message);
    }
}
