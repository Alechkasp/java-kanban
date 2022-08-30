package ru.practicum.kanban.models;

public class SubTask extends Task {

    private final int epicId;

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return this.epicId;
    }

    @Override
    public String toString() {
        return "SubTask{id=" + getId() +
                " epicId=" + epicId +
                '}';
    }
}