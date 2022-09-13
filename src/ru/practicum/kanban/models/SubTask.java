package ru.practicum.kanban.models;

public class SubTask extends Task {

    private final int epicId;

    public SubTask(TypeOfTask type, String name, String description, Status status, int epicId) {
        super(type, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return this.epicId;
    }

    @Override
    public String toString() {
        String result = String.join(",", Integer.toString(getId()), getType().toString(), getName(),
                getStatus().toString(), getDescription(), Integer.toString(getEpicId()));
        return result;
    }
}