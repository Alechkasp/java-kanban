import java.util.HashMap;

public class TasksManager {
    int id = 0;
    HashMap<Integer, Object> tableTasks = new HashMap<>();
    HashMap<Integer, Epic> tableEpics = new HashMap<>();
    HashMap<Integer, SubTask> tableSubTasks = new HashMap<>();

    //добавить задачу типа Task
    Task addTask(Task task) {
        id++;
        task.setId(id);
        tableTasks.put(id, task);
        return task;
    }

    //добавить задачу типа Epic
    Epic addEpic(Epic epic) {
        id++;
        epic.setId(id);
        tableEpics.put(id, epic);
        return epic;
    }

    //добавить задачу SubTask
    SubTask addSubTask(SubTask subTask) {
        id++;
        subTask.setId(id);
        tableSubTasks.put(id, subTask);
        subTask.getEpic().addSubTaskToEpic(id);
        return subTask;
    }

    //получить список все задач типа Task
    void getListTasks() {
        System.out.println(tableTasks.values());
    }

    //получить список все задач типа Epic
    void getListEpics() {
        for (Integer epic : tableEpics.keySet()) {
            updateEpic(getEpic(epic));
        }
        System.out.println(tableEpics.values());
    }

    //получить список все задач типа SubTask
    void getListSubTasks() {
        System.out.println(tableSubTasks.values());
    }

    //удалить все задачи типа Task
    void deleteListTasks() {
        tableTasks.clear();
    }

    //удалить все задачи типа Epic
    void deleteListEpics() {
        tableEpics.clear();
        tableSubTasks.clear();
    }

    //удалить все задачи типа SubTask
    void deleteListSubTask() {
        tableSubTasks.clear();
        for (Integer epic : tableEpics.keySet()) {
            getEpic(epic).delSubTask();
            updateEpic(getEpic(epic));
        }
    }

    //получение по идентификатору задачи типа Task
    Task getTask(int id) {
        return (Task) tableTasks.get(id);
    }

    //получение по идентификатору задачи типа Epic
    Epic getEpic(int id) {
        return tableEpics.get(id);
    }

    //получение по идентификатору задачи типа SubTask
    SubTask getSubTask(int id) {
        return tableSubTasks.get(id);
    }

    //обновление задачи типа Task
    void updateTask(Task task) {
        tableTasks.put(task.getId(), task);
    }

    //обновление задачи типа Epic
    void updateEpic(Epic epic) {
        int countNew = 0;
        int countDone = 0;

        for (Integer subTask : tableSubTasks.keySet()) {
            if (getEpic(epic.getId()).getSubTasks().contains(subTask)) {
                if (tableSubTasks.get(subTask).getStatus().equals("NEW")) {
                    countNew++;
                }
            }
        }

        for (Integer subTask : tableSubTasks.keySet()) {
            if (getEpic(epic.getId()).getSubTasks().contains(subTask)) {
                if (tableSubTasks.get(subTask).getStatus().equals("DONE")) {
                    countDone++;
                }
            }
        }

        //если у эпика нет подзадач
        boolean withoutSubTask = getEpic(epic.getId()).getSubTasks().isEmpty();
        //если подзадачи у эпика есть и все имеют статус "NEW"
        boolean withSubTaskStatusNew = countNew == getEpic(epic.getId()).getSubTasks().size();
        //если все подзадачи у эпика имеют статус "DONE"
        boolean withSubTaskStatusDone = countDone == getEpic(epic.getId()).getSubTasks().size();

        if (withoutSubTask || withSubTaskStatusNew) {
            epic.setStatus("NEW");
            tableEpics.put(epic.getId(), epic);
        } else if (withSubTaskStatusDone) {
            epic.setStatus("DONE");
            tableEpics.put(epic.getId(), epic);
        } else {
            epic.setStatus("IN_PROGRESS");
            tableEpics.put(epic.getId(), epic);
        }
    }

    //обновление задачи типа SubTask
    void updateSubTask(SubTask subTask) {
        tableSubTasks.put(subTask.getId(), subTask);
        for (Integer epic : tableEpics.keySet()) {
            updateEpic(getEpic(epic));
        }
    }

    //удаление по идентификатору задачи типа Task
    void delTask(int id) {
        tableTasks.remove(id);
    }

    //удаление по идентификатору задачи типа Epic
    void delEpic(int id) {
        for (Integer subTask : getEpic(id).getSubTasks()) {
            if (tableSubTasks.containsKey(subTask)) {
                tableSubTasks.remove(subTask);
            }
        }
        getEpic(id).getSubTasks().clear();
        tableEpics.remove(id);
    }

    //удаление по идентификатору задачи типа SubTask
    void delSubTask(int id) {
        int epicId = 0;
        for (Integer epic : tableEpics.keySet()) {
            if (getEpic(epic).getSubTasks().contains(id)) {
                epicId = epic;
            }
        }

        for (int i = 0; i < getEpic(epicId).getSubTasks().size(); i++) {
            if (getEpic(epicId).getSubTasks().get(i) == id) {
                getEpic(epicId).delSubTaskFromEpic(i);
            }
        }
        updateEpic(getEpic(epicId));
        tableSubTasks.remove(id);
    }

    //получение списка всех подзадач определённого эпика
    void getEpicSubTasks(int id) {
        getEpic(id).printEpicSubTasks();
    }
}