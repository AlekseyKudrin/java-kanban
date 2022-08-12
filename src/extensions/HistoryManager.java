package extensions;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    List<Task> history = new ArrayList<>();

    void add(Task task);

    List getHistory();
}
