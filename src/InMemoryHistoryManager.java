import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    protected List<Task> history = new ArrayList<>();

    //добавить задачу в список просмотренных
    @Override
    public void add(Task task) {
        if (history.size() < 10) {
            history.add(task);
        }
        if (history.size() == 10) {
            history.remove(0);
            history.add(task);
        }
    }

    //получить список последних просмотренных задач
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
