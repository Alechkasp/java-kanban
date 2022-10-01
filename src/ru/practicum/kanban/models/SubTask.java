package ru.practicum.kanban.models;

import java.time.Instant;

public class SubTask extends Task {

    private final int epicId;

    public SubTask(TypeOfTask type, String name, String description, Status status, int epicId, Instant startTime,
                   long duration) {
        super(type, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return this.epicId;
    }

    @Override
    public String toString() {
       String result = String.join(",", Integer.toString(getId()), getType().toString(), getName(),
               getStatus().toString(), getDescription(), Long.toString(getDuration()),
               Long.toString(getStartTime().toEpochMilli()), Integer.toString(getEpicId()));
        return result;
    }
}