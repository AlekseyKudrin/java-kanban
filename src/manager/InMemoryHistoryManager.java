package manager;

import extensions.HistoryManager;
import task.Task;
import util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;

    private Map<Integer, Node> history = new HashMap<>();

    @Override
    public List<Integer> getHistory() {
        Node headTemp = head;
        List<Integer> list = new ArrayList<>();
        if (head != null) {
            while (headTemp.getNext() != null) {
                list.add(headTemp.getTask().getId());
                headTemp = headTemp.getNext();
            }
            list.add(headTemp.getTask().getId());
        }
        return list;
    }

    @Override
    public void add(Task task) {
        Node node = new Node(tail, task, null);
        if (history.containsKey(task.getId())) {
            linkLast(history.get(task.getId()));
            history.remove(task.getId());
            tail.setNext(node);
        } else {
            if (head == null) {
                head = node;
            } else {
                tail.setNext(node);
            }
        }
        tail = node;
        history.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            linkLast(history.get(id));
            history.remove(id);
        }
    }


    public void linkLast(Node node) {
        if (node.getPrev() == null & node.getNext() == null) {
            head = null;
            return;
        }
        if (node.getPrev() == null) {
            head = node.getNext();
            node.getNext().setPrev(null);
            tail.setNext(node);
        } else {
            node.getPrev().setNext(node.getNext());
        }
        if (node.getNext() == null) {
            node.getPrev().setNext(null);
            tail = node;
        } else {
            node.getNext().setPrev(node.getPrev());
        }
        if (head == tail) {
            head.setNext(null);
            tail = null;
        }
    }
}