package ru.practicum.kanban.models;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Task {
    private final TypeOfTask type;
    private final String name;
    private int id;
    private final String description;
    private Status status;
    private Instant startTime;
    private long duration;

    public Task(TypeOfTask type, String name, String description, Status status, Instant startTime, long duration) {
        this.type = type;
        this.name = name;
        this.id = 0;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
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

    public long getDuration() {
        return duration;
    }

/*    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getStartTime() {
        return startTime.toEpochMilli();
    }

    public long getEndTime() {
        long SECONDS_IN_MINUTE = 60L;
        return startTime.plusSeconds(duration * SECONDS_IN_MINUTE).toEpochMilli();
    }*/

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        long SECONDS_IN_MINUTE = 60L;
        return startTime.plusSeconds(duration * SECONDS_IN_MINUTE);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && type == task.type
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, id, description, status);
    }

    @Override
    public String toString() {
/*       String result = String.join(",", Integer.toString(id), type.toString(), name, status.toString(),
                description, Long.toString(startTime.toEpochMilli()), Long.toString(getEndTime()));*/

/*        String result = String.join(",", Integer.toString(id), type.toString(), name, status.toString(),
                description, startTime.toEpochMilli(), duration);*/
        return id + "," +
                type + "," +
                name + "," +
                status + "," +
                description + "," +
                startTime.toEpochMilli() + "," +
                duration;
        //return result;
    }
}