import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksIds;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subTasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasks() {
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

    @Override
    public String toString() {
        return "Epic{" +
                "subTasksIds=" + subTasksIds +
                '}';
    }
}