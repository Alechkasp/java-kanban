import java.util.ArrayList;
import java.util.HashMap;

public class TasksManager {
    int id = 0;
    protected HashMap<Integer, Task> tableTasks = new HashMap<>();
    protected HashMap<Integer, Epic> tableEpics = new HashMap<>();
    protected HashMap<Integer, SubTask> tableSubTasks = new HashMap<>();

    //добавить задачу типа Task
    public Task addTask(Task task) {
        id++;
        task.setId(id);
        tableTasks.put(id, task);
        return task;
    }

    //добавить задачу типа Epic
    public Epic addEpic(Epic epic) {
        id++;
        epic.setId(id);
        tableEpics.put(id, epic);
        return epic;
    }

    //добавить задачу SubTask
    public SubTask addSubTask(SubTask subTask) {
        id++;
        subTask.setId(id);
        tableSubTasks.put(id, subTask);
        getEpic(subTask.getEpicId()).addSubTaskToEpic(id);
        updateEpicStatus(subTask.getEpicId());
        return subTask;
    }

    //получить список все задач типа Task
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Integer task : tableTasks.keySet()) {
            tasks.add(tableTasks.get(task));
        }
        return tasks;
    }

    //получить список все задач типа Epic
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (Integer epic : tableEpics.keySet()) {
            epics.add(tableEpics.get(epic));
        }
        return epics;
    }

    //получить список все задач типа SubTask
    public ArrayList<SubTask> getSubTasks() {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Integer subTask : tableSubTasks.keySet()) {
            subTasks.add(tableSubTasks.get(subTask));
        }
        return subTasks;
    }

    //удалить все задачи типа Task
    public void deleteListTasks() {
        tableTasks.clear();
    }

    //удалить все задачи типа Epic
    public void deleteListEpics() {
        tableEpics.clear();
        tableSubTasks.clear();
    }

    //удалить все задачи типа SubTask
    public void deleteListSubTask() {
        tableSubTasks.clear();
    }

    //получение по идентификатору задачи типа Task
    public Task getTask(int id) {
        return (Task) tableTasks.get(id);
    }

    //получение по идентификатору задачи типа Epic
    public Epic getEpic(int id) {
        return tableEpics.get(id);
    }

    //получение по идентификатору задачи типа SubTask
    public SubTask getSubTask(int id) {
        return tableSubTasks.get(id);
    }

    //обновление задачи типа Task
    public void updateTask(Task task) {
        if (tableTasks.containsKey(task.getId())) {
            tableTasks.put(task.getId(), task);
        }
    }

    //обновление задачи типа Epic
    public void updateEpic(Epic epic) {
        tableEpics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    //обновление задачи типа SubTask
    public void updateSubTask(SubTask subTask) {
        if (tableSubTasks.containsKey(subTask.getId())) {
            if (tableEpics.containsKey(subTask.getEpicId())) {
                tableSubTasks.put(subTask.getId(), subTask);
                updateEpicStatus(tableEpics.get(subTask.getEpicId()).getId());
            }
        }
    }

    //удаление по идентификатору задачи типа Task
    public void delTask(int id) {
        tableTasks.remove(id);
    }

    //удаление по идентификатору задачи типа Epic
    public void delEpic(int id) {
        for (Integer subTask : getEpic(id).getSubTasks()) {
            if (tableSubTasks.containsKey(subTask)) {
                tableSubTasks.remove(subTask);
            }
        }
        tableEpics.remove(id);
    }

    //удаление по идентификатору задачи типа SubTask
    public void delSubTask(int id) {
        getEpic(tableSubTasks.get(id).getEpicId()).delSubTaskFromEpic(id);
        updateEpic(getEpic(tableSubTasks.get(id).getEpicId()));
        tableSubTasks.remove(id);
    }

    //получение списка всех подзадач определённого эпика
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
                if (tableSubTasks.get(subTask).getStatus().equals("NEW")) {
                    countNew++;
                }
            }
        }

        for (Integer subTask : tableSubTasks.keySet()) {
            if (getEpic(epicId).getSubTasks().contains(subTask)) {
                if (tableSubTasks.get(subTask).getStatus().equals("DONE")) {
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
            getEpic(epicId).setStatus("NEW");
        } else if (withSubTaskStatusDone) {
            getEpic(epicId).setStatus("DONE");
        } else {
            getEpic(epicId).setStatus("IN_PROGRESS");
        }
    }
}