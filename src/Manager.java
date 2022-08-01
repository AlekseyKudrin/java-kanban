import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    private int id = 1;
    private static final String NEW = "NEW";
    private static final String IN_PROGRESS = "IN_PROGRESS";
    private static final String DONE = "DONE";
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();

    void addTask(Task task) {
        task.setId(id);
        tasks.put(id++, task);
    }

    void addEpic(Epic epic) {
        epic.setId(id);
        epic.setStatus(assignEpicStatus(id));
        epics.put(id++, epic);
        for (Integer key: subTasks.keySet()) {
            if (subTasks.get(key).getEpicId() == epic.getId()) {
                epic.subTaskIds.add(subTasks.get(key).id);
            }
        }

    }

    void addSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.epicId)) {
            subTask.setId(id++);
            epics.get(subTask.getEpicId()).subTaskIds.add(subTask.id);
        } else {
            subTask.setId(id);
            subTasks.put(id++, subTask);
        }
    }

    void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        epic.setStatus(assignEpicStatus(epic.getId()));
    }

    void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        if (epics.containsKey(subTask.getEpicId())) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.setStatus(assignEpicStatus(epic.getId()));
        }
    }

    void printTargetTask(int id) {
        for (int key: tasks.keySet()) {
            if (id == tasks.get(key).getId()) {
                System.out.println("задача под id " + id);
                System.out.println(tasks.get(key));
                return;
            }
        }
        System.out.println("Такой задачи под данным id нет");
    }

    void printTargetEpic(int id) {
        for (int key: epics.keySet()) {
            if (id == epics.get(key).getId()) {
                System.out.println("Глобальная задача под id " + id);
                System.out.println(epics.get(key));
                return;
            }
        }
        System.out.println("Такой глобальной задачи под данным id нет");
    }

    void printTargetSubTask(int id) {
        for (int key: subTasks.keySet()) {
            if (id == subTasks.get(key).getId()) {
                System.out.println("Подзадача под id " + id);
                System.out.println(subTasks.get(key)        );
                return;
            }
        }
        System.out.println("Такой подзадачи под данным id нет");
    }

    void printAllTask() {
        int i = 1;
        System.out.println("Список всех задач:");
        for (int key : tasks.keySet()) {
            System.out.println("задача №" + i
                    + "\n Назавание:" + tasks.get(key).getTitle()
                    + "\n Описание:" + tasks.get(key).getDescription()
                    + "\n id="+ tasks.get(key).getId()
                    + "\n Статус:"+ tasks.get(key).getStatus());
            i++;
        }
    }

    void printAllEpic() {
        int i = 1;
        System.out.println("Список всех глобальных задач:");
        for (int key : epics.keySet()) {
            System.out.println("Глобальная задача №" + i
                    + "\n Назавание:" + epics.get(key).getStatus()
                    + "\n Описание:" + epics.get(key).getDescription()
                    + "\n id=" + epics.get(key).getId()
                    + "\n Статус:" + epics.get(key).getStatus()
                    + "\n Содержит в себе " + epics.get(key).subTaskIds.size() +" подзадачу(и)");
            i++;
        }
    }

    void printAllSubTask() {
        int i = 1;
        System.out.println("Список всех подзадач:");
        for (int key : subTasks.keySet()) {
            System.out.println("подзадача №" + i
                    + "\n Назавание:" + subTasks.get(key).getTitle()
                    + "\n Описание:" + subTasks.get(key).getDescription()
                    + "\n id=" + subTasks.get(key).getId()
                    + "\n Статус:" + subTasks.get(key).getStatus()
                    + "\n относится к глобальгой задаче id " + subTasks.get(key).getEpicId());
            i++;
        }
    }

    void printAllSubTaskEpic(int id) {
        int i = 1;
        System.out.println("Список всех подзадач глобальной задачи:");
        for (int subTaskId : epics.get(id).subTaskIds) {
            for (int key : subTasks.keySet()) {
                if (subTaskId == subTasks.get(key).id)
                    System.out.println(subTasks.get(key));
                i++;
            }
        }
    }

    void removeIdTask(int id) {
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

    void removeIdEpic(int id) {
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

    void removeIdSubTask(int id) {
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

    void removeAllTask() {
        tasks.clear();
    }

    void removeAllEpic() {
        epics.clear();
        subTasks.clear();
    }

    void removeAllSabTask() {
        subTasks.clear();
        for(int key : epics.keySet()) {
            epics.get(key).subTaskIds.clear();
        }
    }

    private String assignEpicStatus(int id) {
        ArrayList<String> allStatus = new ArrayList<>();
        String status = NEW;
        for (Integer key: subTasks.keySet()) {
            if (id == subTasks.get(key).getEpicId()) {
                allStatus.add(subTasks.get(key).getStatus());
            }
        }
        if (allStatus.isEmpty()) {
            return status;
        } else {
            for(String tempStatus: allStatus) {
                status = tempStatus;
                if (status.equals(IN_PROGRESS)||status.equals(DONE)) {
                    return IN_PROGRESS;
                }
            }
        }
        return status;
    }
}
