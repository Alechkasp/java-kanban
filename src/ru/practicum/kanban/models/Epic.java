package ru.practicum.kanban.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subTasksIds;
    private Instant endTime;

    public Epic(TypeOfTask type, String name, String description, Status status, Instant startTime, long duration) {
        super(type, name, description, status, startTime, duration);
        subTasksIds = new ArrayList<>();
        endTime = Instant.ofEpochMilli(0);
    }

    public List<Integer> getSubTasks() {
        return subTasksIds;
    }

    //добавить SubTask в Epic
    public void addSubTaskToEpic(int subTaskId) {
        this.subTasksIds.add(subTaskId);
    }

    //удалить SubTask из Epic
    public void delSubTaskFromEpic(int subTaskId) {
        int id = 0;
        for (int i = 0; i < subTasksIds.size(); i++) {
            if (subTaskId == subTasksIds.get(i)) {
                id = i;
            }
        }
        subTasksIds.remove(id);
    }

    //если удалили все SubTask
    public void clearSubTasksIds() {
        subTasksIds.clear();
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        String result = String.join(",", Integer.toString(getId()), getType().toString(), getName(),
                getStatus().toString(), getDescription(), Long.toString(getDuration()),
        Long.toString(getStartTime().toEpochMilli()), Long.toString(getEndTime().toEpochMilli()));
        return result;
    }
}