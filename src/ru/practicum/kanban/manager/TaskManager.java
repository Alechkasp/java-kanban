package ru.practicum.kanban.manager;

import ru.practicum.kanban.models.Epic;
import ru.practicum.kanban.models.SubTask;
import ru.practicum.kanban.models.Task;

import java.util.List;

public interface TaskManager {

    //добавить задачу типа Task
    Task addTask(Task task);

    //добавить задачу типа Epic
    Epic addEpic(Epic epic);

    //добавить задачу SubTask
    SubTask addSubTask(SubTask subTask);

    //получить список все задач типа Task
    List<Task> getTasks();

    //получить список все задач типа Epic
    List<Epic> getEpics();

    //получить список все задач типа SubTask
    List<SubTask> getSubTasks();

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
    List<SubTask> getEpicSubTasks(int id);

    //получить список последних просмотренных задач
    List<Task> getHistory();
}