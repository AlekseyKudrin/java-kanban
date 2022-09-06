package util;

import task.Task;

public class Node {

    public Node next;

    public Node prev;
    public Task task;

    public Node(Node prev, Task task, Node next) {
        this.prev = prev;
        this.task = task;
        this.next = next;

    }
}
