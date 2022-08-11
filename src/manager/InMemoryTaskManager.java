package manager;

import task.Epic;
import task.StatusTask;
import task.SubTask;
import task.Task;
import extensions.BrowsingHistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager extends Managers implements BrowsingHistory {
    private int id = 1;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();

    private List<Task> history = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        task.setId(id);
        tasks.put(id++, task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(id);
        epic.setStatus(assignEpicStatus(id));
        epics.put(id++, epic);
        for (Integer key: subTasks.keySet()) {
            if (subTasks.get(key).getEpicId() == epic.getId()) {
                epic.subTaskIds.add(subTasks.get(key).getId());
            }
        }

    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(id++);
            epics.get(subTask.getEpicId()).subTaskIds.add(subTask.getId());
        } else {
            subTask.setId(id);
            subTasks.put(id++, subTask);
        }
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        epic.setStatus(assignEpicStatus(epic.getId()));
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        if (epics.containsKey(subTask.getEpicId())) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.setStatus(assignEpicStatus(epic.getId()));
        }
    }

    @Override
    public void printTargetTask(int id) {
        for (int key: tasks.keySet()) {
            if (id == tasks.get(key).getId()) {
                System.out.println("задача под id " + id);
                System.out.println(tasks.get(key));
                return;
            }
        }
        System.out.println("Такой задачи под данным id нет");
    }

    @Override
    public void printTargetEpic(int id) {
        for (int key: epics.keySet()) {
            if (id == epics.get(key).getId()) {
                System.out.println("Глобальная задача под id " + id);
                System.out.println(epics.get(key));
                return;
            }
        }
        System.out.println("Такой глобальной задачи под данным id нет");
    }

    @Override
    public void printTargetSubTask(int id) {
        for (int key: subTasks.keySet()) {
            if (id == subTasks.get(key).getId()) {
                System.out.println("Подзадача под id " + id);
                System.out.println(subTasks.get(key)        );
                return;
            }
        }
        System.out.println("Такой подзадачи под данным id нет");
    }

    @Override
    public void printAllTask() {
        int i = 1; //счетчик задач
        System.out.println("Список всех задач:");
        for (int key : tasks.keySet()) {
            System.out.println("задача №" + i);
            System.out.println(tasks.get(key));
            i++;
        }
    }

    @Override
    public void printAllEpic() {
        int i = 1;
        System.out.println("Список всех глобальных задач:");
        for (int key : epics.keySet()) {
            System.out.println("Глобальная задача №" + i);
            System.out.println(epics.get(key)
                    + "\n Содержит в себе " + epics.get(key).subTaskIds.size() +" подзадачу(и)");
            i++;
        }
    }

    @Override
    public void printAllSubTask() {
        int i = 1;
        System.out.println("Список всех подзадач:");
        for (int key : subTasks.keySet()) {
            System.out.println("подзадача №" + i);
            System.out.println(subTasks.get(key)
                    + "\n относится к глобальгой задаче id " + subTasks.get(key).getEpicId());
            i++;
        }
    }

    @Override
    public void printAllSubTaskEpic(int id) {
        int i = 1;
        System.out.println("Список всех подзадач глобальной задачи:");
        for (int subTaskId : epics.get(id).subTaskIds) {
            for (int key : subTasks.keySet()) {
                if (subTaskId == subTasks.get(key).getId())
                    System.out.println("подзадача №" + i);
                System.out.println(subTasks.get(key));
                i++;
            }
        }
    }

    @Override
    public void removeIdTask(int id) {
        if (tasks.containsKey(id)) {
            for (int key : tasks.keySet()) {
                if (id == tasks.get(key).getId()) {
                    tasks.remove(id);
                    return;
                }
            }
        } else {
            System.out.println("Задачи с таким id нет");
        }
    }

    @Override
    public void removeIdEpic(int id) {
        ArrayList<Integer> numberSubtask = new ArrayList<>();
        if (epics.containsKey(id)) {
            for (int keyEpic : epics.keySet()) {
                if (id == epics.get(keyEpic).getId()) {
                    epics.remove(id);
                    break;
                }
            }
            for (int keySub : subTasks.keySet()) {
                if (id == subTasks.get(keySub).getEpicId()) {
                    numberSubtask.add(subTasks.get(keySub).getId());
                }
            }
            for (int key : numberSubtask) {
                subTasks.remove(key);
            }
        } else {
            System.out.println("Глобалтной задачи с таким id нет");
        }
    }

    @Override
    public void removeIdSubTask(int id) {
        if (subTasks.containsKey(id)) {
            for (int keySub : subTasks.keySet()) {
                if (id == subTasks.get(keySub).getId()) {
                    subTasks.remove(id);
                    break;
                }
            }
            for (int keyEpic : epics.keySet()) {
                for (int subTaskId : epics.get(keyEpic).subTaskIds){
                    if (id == subTasks.get(subTaskId).getEpicId())   {
                        epics.get(keyEpic).subTaskIds.remove(subTaskId);
                        break;
                    }
                }
            }
        } else {
            System.out.println("Подзадачи с таким id нет");
        }

    }

    @Override
    public void removeAllTask() {
        tasks.clear();
    }

    @Override
    public void removeAllEpic() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeAllSabTask() {
        subTasks.clear();
        for(int key : epics.keySet()) {
            epics.get(key).subTaskIds.clear();
        }
    }

    @Override
    public void getHistory() {
        for (Task task : history) {
            System.out.print(task.getId()+" ");
        }

    }
    @Override
    public void getTask(int id) {
        if (tasks.containsKey(id)) {
            for (Integer key : tasks.keySet()) {
                if (tasks.get(key).getId() == id) {
                    if (history.size() == 10) {
                        history.remove(0);
                        history.add(tasks.get(key));
                    } else {
                        history.add(tasks.get(key));
                        System.out.println(tasks.get(key));
                        return;
                    }
                } else {
                    System.out.println("Задачи с таким id нет");
                    if (history.size() == 10) {
                        history.remove(0);
                        history.add(tasks.get(key));
                    } else {
                        history.add(tasks.get(key));
                    }
                }
            }
        }
    }

    @Override
    public void getSubTask(int id) {
        if (subTasks.containsKey(id)) {
            for (Integer key : subTasks.keySet()) {
                if (subTasks.get(key).getId() == id) {
                    if (history.size() == 10) {
                        history.remove(0);
                        history.add(subTasks.get(key));
                    } else {
                        history.add(subTasks.get(key));
                        System.out.println(subTasks.get(key));
                        return;
                    }
                } else {
                    System.out.println("Задачи с таким id нет");
                    if (history.size() == 10) {
                        history.remove(0);
                        history.add(subTasks.get(key));
                    } else {
                        history.add(subTasks.get(key));
                    }
                }
            }
        }
    }

    @Override
    public void getEpic(int id) {
        if (epics.containsKey(id)) {
            for (Integer key : epics.keySet()) {
                if (epics.get(key).getId() == id) {
                    if (history.size() == 10) {
                        history.remove(0);
                        history.add(epics.get(key));
                    } else {
                        history.add(epics.get(key));
                        System.out.println(epics.get(key));
                        return;
                    }
                } else {
                    System.out.println("Задачи с таким id нет");
                    if (history.size() == 10) {
                        history.remove(0);
                        history.add(epics.get(key));
                    } else {
                        history.add(epics.get(key));
                    }
                }
            }
        }
    }

    private StatusTask assignEpicStatus(int id) {
        ArrayList<StatusTask> allStatus = new ArrayList<>();
        StatusTask status = StatusTask.NEW;
        for (Integer key: subTasks.keySet()) {
            if (id == subTasks.get(key).getEpicId()) {
                allStatus.add(subTasks.get(key).getStatus());
            }
        }
        if (allStatus.isEmpty()) {
            return status;
        } else {
            for(StatusTask tempStatus: allStatus) {
                status = tempStatus;
                if (status.equals(StatusTask.IN_PROGRESS)||status.equals(StatusTask.DONE)) {
                    return StatusTask.IN_PROGRESS;
                }
            }
        }
        return status;
    }
}
