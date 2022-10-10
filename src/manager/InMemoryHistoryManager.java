package manager;

import extensions.HistoryManager;
import task.Task;
import util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    Node head;
    Node tail;

    public Map<Integer, Node> history = new HashMap<>();

    @Override
    public List<Integer> getHistory() {
        List<Integer> list = new ArrayList<>();
        for (Node value : history.values()) {
            if (value.prev == null) {
                while (!(list.size() == history.size())) {
                    list.add(value.task.getId());
                    value = value.next;
                }
            }
        }
        return list;
    }

    @Override
    public void add(Task task) {
        Node oldHead = head;
        Node oldTail = tail;
        Node node = new Node(oldHead, task, oldTail);
        if (!history.containsKey(task.getId())) {
            if (oldHead != null) {
                oldHead.next = node;
            }
        } else {
            linkLast(history.get(task.getId()));
            history.remove(task.getId());
            oldHead.next = node;
        }
        head = node;
        history.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        linkLast(history.get(id));
        history.remove(id);
    }


    public void linkLast(Node node) {
        if (!(node.next == null & node.prev == null)) {
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                node.next.prev = null;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }
        }
    }
    }