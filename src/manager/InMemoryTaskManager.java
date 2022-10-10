package manager;

import extensions.HistoryManager;
import extensions.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import util.StatusTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 1;
    public Map<Integer, Task> tasks = new HashMap<>();
    public Map<Integer, Epic> epics = new HashMap<>();
    public Map<Integer, SubTask> subTasks = new HashMap<>();

    public HistoryManager history = Managers.getDefaultHistory();

    @Override
    public void addTask(Task task) {
        task.setId(id);
        calculateCompletion(task);
        tasks.put(id++, task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(id);
        epic.setStatus(assignEpicStatus(id));
        calculateCompletion(epic);
        epics.put(id++, epic);
        for (Integer key : subTasks.keySet()) {
            if (subTasks.get(key).getEpicId() == epic.getId()) {
                epic.subTask.add(subTasks.get(key));
            }
        }
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(id);
            epics.get(subTask.getEpicId()).subTask.add(subTask);
            calculateCompletion(subTask);
            ArrayList <SubTask> list = new ArrayList<>(epics.get(subTask.getEpicId()).subTask);
            epics.get(subTask.getEpicId()).setStartTime(list.get(0).getStartTime());
            epics.get(subTask.getEpicId()).setEndTime(list.get(list.size()-1).getEndTime());
            subTasks.put(id++, subTask);
        } else {
            subTask.setId(id);
            calculateCompletion(subTask);
            subTasks.put(id++, subTask);
        }
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        for ( Epic value : epics.values()) {
            if (value.getDescription().equals(epic.getDescription()) || value.getTitle().equals(epic.getTitle())) {
                epic.setId(value.getId());
                calculateCompletion(epic);
                epics.put(epic.getId(), epic);
            }
        }
        calculateCompletion(epic);
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
        for (int key : tasks.keySet()) {
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
        for (int key : epics.keySet()) {
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
        for (int key : subTasks.keySet()) {
            if (id == subTasks.get(key).getId()) {
                System.out.println("Подзадача под id " + id);
                System.out.println(subTasks.get(key));
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
            System.out.println(epics.get(key) + "\n Содержит в себе " + epics.get(key).subTask.size() + " подзадачу(и)");
            i++;
        }
    }

    @Override
    public void printAllSubTask() {
        int i = 1;
        System.out.println("Список всех подзадач:");
        for (int key : subTasks.keySet()) {
            System.out.println("подзадача №" + i);
            System.out.println(subTasks.get(key) + "\n относится к глобальгой задаче id " + subTasks.get(key).getEpicId());
            i++;
        }
    }

    @Override
    public void printAllSubTaskEpic(int id) {
        int i = 1;
        System.out.println("Список всех подзадач глобальной задачи:");
        for (SubTask subTask : epics.get(id).subTask) {
            for (int key : subTasks.keySet()) {
                if (subTask.getId() == subTasks.get(key).getId()) {
                    System.out.println("подзадача №" + i);
                    System.out.println(subTasks.get(key));
                    i++;
                }
            }
        }
    }

    @Override
    public void removeIdTask(int id) {
        if (tasks.containsKey(id)) {
            for (int key : tasks.keySet()) {
                if (id == tasks.get(key).getId()) {
                    tasks.remove(id);
                    history.remove(id);
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
                    history.remove(id);
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
                history.remove(key);

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
                for (SubTask subTask : epics.get(keyEpic).subTask) {
                    if (id == subTasks.get(subTask).getEpicId()) {
                        epics.get(keyEpic).subTask.remove(subTask);
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
        for (int key : epics.keySet()) {
            epics.get(key).subTask.clear();
        }
    }

    @Override
    public void getTask(int id) {
        if (tasks.containsKey(id)) {
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                if (entry.getKey() == id) {
                    history.add(entry.getValue());
                    return;
                }
            }
        } else if (subTasks.containsKey(id)) {
            for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
                if (entry.getKey() == id) {
                    history.add(entry.getValue());
                    return;
                }
            }
        } else if (epics.containsKey(id)) {
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                if (entry.getKey() == id) {
                    history.add(entry.getValue());
                    return;
                }
            }
        } else {
            System.out.println("Задачи с таким id нет");
        }
    }

    @Override
    public void calculateCompletion(Task task) {
        if (task.getClass() == Task.class) {
            task.setEndTime(task.getStartTime().plus(task.getDuration()));
        } else if (task.getClass() == SubTask.class) {
            task.setEndTime(task.getStartTime().plus(task.getDuration()));
            Duration duration = Duration.ofMinutes(0);
            if (epics.containsKey(((SubTask) task).getEpicId())) {
                for (SubTask subTask : epics.get(((SubTask) task).getEpicId()).subTask) {
                    duration = duration.plus(subTask.getDuration());
                }
                epics.get(((SubTask) task).getEpicId()).setDuration(duration);
            }
        } else {
            Epic epic = (Epic)task;
            if (epic.subTask.size() == 0) {
                epic.setDuration(Duration.ofMinutes(0));
                epic.setStartTime(LocalDateTime.now());
                epic.setEndTime(epic.getStartTime().plus(epic.getDuration()));
            }
        }
    }
    @Override


    public void getHistory() {
        System.out.println(history.getHistory());
    }

    public StatusTask assignEpicStatus(int id) {
        ArrayList<StatusTask> allStatus = new ArrayList<>();
        StatusTask status = StatusTask.NEW;
        for (Integer key : subTasks.keySet()) {
            if (id == subTasks.get(key).getEpicId()) {
                allStatus.add(subTasks.get(key).getStatus());
            }
        }
        if (allStatus.isEmpty()) {
            return status;
        } else {
            for (StatusTask tempStatus : allStatus) {
                status = tempStatus;
                if (status.equals(StatusTask.IN_PROGRESS) || status.equals(StatusTask.DONE)) {
                    return StatusTask.IN_PROGRESS;
                }
            }
        }
        return status;
    }
}
