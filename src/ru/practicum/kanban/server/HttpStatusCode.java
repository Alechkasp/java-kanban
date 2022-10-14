package ru.practicum.kanban.server;

public enum HttpStatusCode {

    //2xx: Success
    OK(200),
    CREATED(201),

    //4xx: Client Error
    BAD_REQUEST(400),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405);

    private final int value;

    HttpStatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}