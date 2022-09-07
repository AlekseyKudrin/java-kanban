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

    Map<Integer, Node> history = new HashMap<>();

    @Override
    public List<Integer> getHistory() {
        List<Integer> list = new ArrayList<>();
        for (Node value : history.values()) {
            if (value.getPrev() == null) {
                while (!(list.size() == history.size())) {
                    list.add(value.getTask().getId());
                    value = value.getNext();
                }
            }
        }
        return list;
    }
        @Override
    public void add(Task task) {
        Node oldHead = head;
        Node node = new Node(oldHead, task, null);
        if (!history.containsKey(task.getId())) {
            if (oldHead != null) {
                oldHead.setNext(node);
            }
        } else {
            linkLast(history.get(task.getId()));
            history.remove(task.getId());
            oldHead.setNext(node);
        }
        head = node;
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
        if (!(node.getNext() == null & node.getPrev() == null)) {
            if (node.getPrev() == null) {
                node.getNext().setPrev(null);
            } else {
                node.getPrev().setNext(node.getNext());
            }
            if (node.getNext() != null) {
                node.getNext().setPrev(node.getPrev());
            }
        }
    }
}