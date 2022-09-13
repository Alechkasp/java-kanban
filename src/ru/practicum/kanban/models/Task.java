package ru.practicum.kanban.models;

import java.util.Objects;

public class Task {
    private final TypeOfTask type;
    private final String name;
    private int id;
    private final String description;
    private Status status;

    public Task(TypeOfTask type, String name, String description, Status status) {
        this.type = type;
        this.name = name;
        this.id = 0;
        this.description = description;
        this.status = status;
    }

    public TypeOfTask getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, description, status);
    }


    @Override
    public String toString() {
        String result = String.join(",", Integer.toString(id), type.toString(), name, status.toString(), description);
        return result;
    }
}