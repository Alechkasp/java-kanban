import java.util.List;

public interface HistoryManager {

    //добавить задачу в список просмотренных
    void add(Task task);

    //получить список последних просмотренных задач
    List<Task> getHistory();
}
