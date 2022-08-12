package manager;

import extensions.HistoryManager;
import task.Task;

import java.util.List;

public class InMemoryHistoryManager extends InMemoryTaskManager implements HistoryManager {

    @Override
    public List getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            history.remove(0);
            history.add(task);
        } else {
            history.add(task);
        }
    }

    public void getTask(int id) {
        if (tasks.containsKey(id)) {
            for (Integer key : tasks.keySet()) {
                if (tasks.get(key).getId() == id) {
                    add(tasks.get(id));
                    System.out.println(tasks.get(id));
                    return;
                } else {
                    System.out.println("Задачи с таким id нет");
                    add(tasks.get(id));
                }
            }
        }
    }

    public void getSubTask(int id) {
        if (subTasks.containsKey(id)) {
            for (Integer key : subTasks.keySet()) {
                if (subTasks.get(key).getId() == id) {
                    if (subTasks.get(key).getId() == id) {
                        add(subTasks.get(id));
                        System.out.println(subTasks.get(id));
                        return;
                    } else {
                        System.out.println("Задачи с таким id нет");
                        add(subTasks.get(id));
                    }
                }
            }
        }
    }

    public void getEpic(int id) {
        if (epics.containsKey(id)) {
            for (Integer key : epics.keySet()) {
                if (epics.get(key).getId() == id) {
                    if (epics.get(key).getId() == id) {
                        add(epics.get(id));
                        System.out.println(epics.get(id));
                        return;
                    } else {
                        System.out.println("Задачи с таким id нет");
                        add(epics.get(id));
                    }
                }
            }
        }
    }
}
