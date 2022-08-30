package ru.practicum.kanban.manager;

import ru.practicum.kanban.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    protected Node historyHead;
    protected Node historyTail;
    private int historySize = 0;
    protected Map<Integer, Node> historyMap = new HashMap<>();
    public static final int NUMBER_OF_MOVIES_VIEWED = 10;

    //добавить задачу в список просмотренных
    @Override
    public void add(Task task) {
        if (task != null) {
            if (historySize >= NUMBER_OF_MOVIES_VIEWED) {
                historyMap.remove(historyHead.data.getId());
                removeNode(historyHead);
            }
            if (historyMap.containsKey(task.getId())) {
                removeNode(historyMap.get(task.getId()));
                historyMap.remove(task.getId());
            }
            linkLast(task);
        }
    }

    //удалить задачу из списка просмотренных
    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
        }
    }

    //получить список последних просмотренных задач
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    //удалить узел связного списка
    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            historyHead = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            historyTail = node.prev;
        }
        historyMap.remove(node.data.getId());
        historySize--;
    }

    //добавить задачу в конец списка
    public void linkLast(Task task) {
        Node newNode = null;
        if (historyHead == null) {
            newNode = new Node(task, null, null);
            historyHead = newNode;
        } else {
            newNode = new Node(task, null, historyTail);
            historyTail.next = newNode;
        }
        historyTail = newNode;
        historyMap.put(task.getId(), newNode);
        historySize++;
    }

    //получить список всех задач
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Node n = historyHead; n != null; n = n.next) {
            tasks.add(n.data);
        }
        return tasks;
    }

    class Node {
        public Task data;
        public Node next;
        public Node prev;

        public Node(Task data, Node next, Node prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}