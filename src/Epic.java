import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasks = new ArrayList<>();

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    //добавить SubTask в Epic
    public void addSubTaskToEpic(int id) {
        subTasks.add(id);
    }

   //удалить SubTask из Epic
    public void delSubTaskFromEpic(int id) {
        subTasks.remove(id);
    }

    //если удалили все задачи типа SubTask
    public void delSubTask() {
        subTasks.clear();
    }

    //вывод SubTask, принадлежащих определенному Epic
    public void printEpicSubTasks() {
        System.out.println(subTasks);
    }
}
