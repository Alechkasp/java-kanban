import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    int id = 0;
    protected HashMap<Integer, Task> tableTasks = new HashMap<>();
    protected HashMap<Integer, Epic> tableEpics = new HashMap<>();
    protected HashMap<Integer, SubTask> tableSubTasks = new HashMap<>();
    protected List<Task> history = new ArrayList<>();


    //добавить задачу типа Task
    @Override
    public Task addTask(Task task) {
        id++;
        task.setId(id);
        tableTasks.put(id, task);
        return task;
    }

    //добавить задачу типа Epic
    @Override
    public Epic addEpic(Epic epic) {
        id++;
        epic.setId(id);
        tableEpics.put(id, epic);
        return epic;
    }

    //добавить задачу SubTask
    @Override
    public SubTask addSubTask(SubTask subTask) {
        id++;
        subTask.setId(id);
        tableSubTasks.put(id, subTask);
        getEpic(subTask.getEpicId()).addSubTaskToEpic(id);
        updateEpicStatus(subTask.getEpicId());
        return subTask;
    }

    //получить список все задач типа Task
    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Integer task : tableTasks.keySet()) {
            tasks.add(tableTasks.get(task));
        }
        return tasks;
    }

    //получить список все задач типа Epic
    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (Integer epic : tableEpics.keySet()) {
            epics.add(tableEpics.get(epic));
        }
        return epics;
    }

    //получить список все задач типа SubTask
    @Override
    public ArrayList<SubTask> getSubTasks() {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Integer subTask : tableSubTasks.keySet()) {
            subTasks.add(tableSubTasks.get(subTask));
        }
        return subTasks;
    }

    //удалить все задачи типа Task
    @Override
    public void deleteListTasks() {
        tableTasks.clear();
    }

    //удалить все задачи типа Epic
    @Override
    public void deleteListEpics() {
        tableEpics.clear();
        tableSubTasks.clear();
    }

    //удалить все задачи типа SubTask
    @Override
    public void deleteListSubTask() {
        tableSubTasks.clear();
    }

    //получение по идентификатору задачи типа Task
    @Override
    public Task getTask(int id) {
        if (history.size() < 10) {
            history.add(tableTasks.get(id));
        }
        if (history.size() == 10) {
            history.remove(0);
            history.add(tableTasks.get(id));
        }
        return tableTasks.get(id); // было return (Task) tableTasks.get(id);
    }

    //получение по идентификатору задачи типа Epic
    @Override
    public Epic getEpic(int id) {
        if (history.size() < 10) {
            history.add(tableEpics.get(id));
        }
        if (history.size() == 10) {
            history.remove(0);
            history.add(tableEpics.get(id));
        }
        return tableEpics.get(id);
    }

    //получение по идентификатору задачи типа SubTask
    @Override
    public SubTask getSubTask(int id) {
        if (history.size() < 10) {
            history.add(tableSubTasks.get(id));
        }
        if (history.size() == 10) {
            history.remove(0);
            history.add(tableSubTasks.get(id));
        }
        return tableSubTasks.get(id);
    }

    //обновление задачи типа Task
    @Override
    public void updateTask(Task task) {
        if (tableTasks.containsKey(task.getId())) {
            tableTasks.put(task.getId(), task);
        }
    }

    //обновление задачи типа Epic
    @Override
    public void updateEpic(Epic epic) {
        tableEpics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    //обновление задачи типа SubTask
    @Override
    public void updateSubTask(SubTask subTask) {
        if (tableSubTasks.containsKey(subTask.getId())) {
            if (tableEpics.containsKey(subTask.getEpicId())) {
                tableSubTasks.put(subTask.getId(), subTask);
                updateEpicStatus(tableEpics.get(subTask.getEpicId()).getId());
            }
        }
    }

    //удаление по идентификатору задачи типа Task
    @Override
    public void delTask(int id) {
        tableTasks.remove(id);
    }

    //удаление по идентификатору задачи типа Epic
    @Override
    public void delEpic(int id) {
        for (Integer subTask : getEpic(id).getSubTasks()) {
            if (tableSubTasks.containsKey(subTask)) {
                tableSubTasks.remove(subTask);
            }
        }
        tableEpics.remove(id);
    }

    //удаление по идентификатору задачи типа SubTask
    @Override
    public void delSubTask(int id) {
        getEpic(tableSubTasks.get(id).getEpicId()).delSubTaskFromEpic(id);
        updateEpic(getEpic(tableSubTasks.get(id).getEpicId()));
        tableSubTasks.remove(id);
    }

    //получение списка всех подзадач определённого эпика
    @Override
    public ArrayList<SubTask> getEpicSubTasks(int id) {
        ArrayList<SubTask> subTaskFromEpic = new ArrayList<>();
        for (Integer subTask : tableSubTasks.keySet()) {
            for (int i = 0; i < getEpic(id).getSubTasks().size(); i++) {
                if (subTask.equals(getEpic(id).getSubTasks().get(i))) {
                    subTaskFromEpic.add(tableSubTasks.get(subTask));
                }
            }
        }
        return subTaskFromEpic;
    }

    private void updateEpicStatus(int epicId) {
        int countNew = 0;
        int countDone = 0;

        for (Integer subTask : tableSubTasks.keySet()) {
            if (getEpic(epicId).getSubTasks().contains(subTask)) {
                if (tableSubTasks.get(subTask).getStatus().equals(Status.NEW)) {
                    countNew++;
                }
            }
        }

        for (Integer subTask : tableSubTasks.keySet()) {
            if (getEpic(epicId).getSubTasks().contains(subTask)) {
                if (tableSubTasks.get(subTask).getStatus().equals(Status.DONE)) {
                    countDone++;
                }
            }
        }

        //если у эпика нет подзадач
        boolean withoutSubTask = getEpic(epicId).getSubTasks().isEmpty();
        //если подзадачи у эпика есть и все имеют статус "NEW"
        boolean withSubTaskStatusNew = countNew == getEpic(epicId).getSubTasks().size();
        //если все подзадачи у эпика имеют статус "DONE"
        boolean withSubTaskStatusDone = countDone == getEpic(epicId).getSubTasks().size();

        if (withoutSubTask || withSubTaskStatusNew) {
            getEpic(epicId).setStatus(Status.NEW);
        } else if (withSubTaskStatusDone) {
            getEpic(epicId).setStatus(Status.DONE);
        } else {
            getEpic(epicId).setStatus(Status.IN_PROGRESS);
        }
    }

    //отображение последних просмотренных задач
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
