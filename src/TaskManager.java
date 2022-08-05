import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    //добавить задачу типа Task
    Task addTask(Task task);

    //добавить задачу типа Epic
    Epic addEpic(Epic epic);

    //добавить задачу SubTask
    SubTask addSubTask(SubTask subTask);

    //получить список все задач типа Task
    ArrayList<Task> getTasks();

    //получить список все задач типа Epic
    ArrayList<Epic> getEpics();

    //получить список все задач типа SubTask
    ArrayList<SubTask> getSubTasks();

    //удалить все задачи типа Task
    void deleteListTasks();

    //удалить все задачи типа Epic
    void deleteListEpics();

    //удалить все задачи типа SubTask
    void deleteListSubTask();

    //получение по идентификатору задачи типа Task
    Task getTask(int id);

    //получение по идентификатору задачи типа Epic
    Epic getEpic(int id);

    //получение по идентификатору задачи типа SubTask
    SubTask getSubTask(int id);

    //обновление задачи типа Task
    void updateTask(Task task);

    //обновление задачи типа Epic
    void updateEpic(Epic epic);

    //обновление задачи типа SubTask
    void updateSubTask(SubTask subTask);

    //удаление по идентификатору задачи типа Task
    void delTask(int id);

    //удаление по идентификатору задачи типа Epic
    void delEpic(int id);

    //удаление по идентификатору задачи типа SubTask
    void delSubTask(int id);

    //получение списка всех подзадач определённого эпика
    ArrayList<SubTask> getEpicSubTasks(int id);

    //получить список последних просмотренных задач
    List<Task> getHistory();
}