import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int id = 1;
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskList = new HashMap<>();

    void addTask(Task task) {
        task.setId(id);
        taskList.put(id++, task);
    }

    void addEpic(Epic epic) {
        epic.setId(id);
        epic.setStatus(assignEpicStatus(id));
        epicList.put(id++, epic);
        for (Integer key: subTaskList.keySet()) {
            if (subTaskList.get(key).getEpicId() == epic.getId()) {
                epic.epicTask.add(subTaskList.get(key));
            }
        }

    }

    void addSubTask(SubTask subTask) {
        if (epicList.containsKey(subTask.epicId)) {
            subTask.setId(id++);
            epicList.get(subTask.getEpicId()).epicTask.add(subTask);
        } else {
            subTask.setId(id);
            subTaskList.put(id++, subTask);
        }
    }

    void updateTask(Task task) {
        taskList.put(task.getId(), task);
    }

    void updateEpic(Epic epic) {
        epicList.put(epic.getId(), epic);
        epic.setStatus(assignEpicStatus(epic.getId()));
    }

    void updateSubTask(SubTask subTask) {
        subTaskList.put(subTask.getId(), subTask);
        if (epicList.containsKey(subTask.getEpicId())) {
            Epic epic = epicList.get(subTask.getEpicId());
            epic.setStatus(assignEpicStatus(epic.getId()));
        }
    }

    void printTargetTask(int id) {
        for (int key: taskList.keySet()) {
            if (id == taskList.get(key).getId()) {
                System.out.println("задача под id " + id
                        + "\n Назавание:" + taskList.get(key).getTitle()
                        + "\n Описание:" + taskList.get(key).getDescription()
                        + "\n id=" + taskList.get(key).getId()
                        + "\n Статус:" + taskList.get(key).getStatus());
                return;
            }
        }
        System.out.println("Такой задачи под данным id нет");
    }

    void printTargetEpic(int id) {
        for (int key: epicList.keySet()) {
            if (id == epicList.get(key).getId()) {
                System.out.println("Глобальная задача под id " + id
                        + "\n Назавание:" + epicList.get(key).getTitle()
                        + "\n Описание:" + epicList.get(key).getDescription()
                        + "\n id=" + epicList.get(key).getId()
                        + "\n Статус:" + epicList.get(key).getStatus()
                        + "\n Содержит в себе " + epicList.get(key).epicTask.size() +" подзадачу(и)");
                return;
            }
        }
        System.out.println("Такой глобальной задачи под данным id нет");
    }

    void printTargetSubTask(int id) {
        for (int key: subTaskList.keySet()) {
            if (id == subTaskList.get(key).getId()) {
                System.out.println("Подзадача под id " + id
                        + "\n Назавание:" + subTaskList.get(key).getTitle()
                        + "\n Описание:" + subTaskList.get(key).getDescription()
                        + "\n id=" + subTaskList.get(key).getId()
                        + "\n Статус:" + subTaskList.get(key).getStatus()
                        + "\n относится к глобальгой задаче id " + subTaskList.get(key).getEpicId());
                return;
            }
        }
        System.out.println("Такой подзадачи под данным id нет");
    }

    void printAllTask() {
        int i = 1;
        System.out.println("Список всех задач:");
        for (int key : taskList.keySet()) {
            System.out.println("задача №" + i
                    + "\n Назавание:" +taskList.get(key).getTitle()
                    + "\n Описание:" +taskList.get(key).getDescription()
                    + "\n id="+ taskList.get(key).getId()
                    + "\n Статус:"+ taskList.get(key).getStatus());
            i++;
        }
    }

    void printAllEpic() {
        int i = 1;
        System.out.println("Список всех глобальных задач:");
        for (int key : epicList.keySet()) {
            System.out.println("Глобальная задача №" + i
                    + "\n Назавание:" + epicList.get(key).getStatus()
                    + "\n Описание:" + epicList.get(key).getDescription()
                    + "\n id=" + epicList.get(key).getId()
                    + "\n Статус:" + epicList.get(key).getStatus()
                    + "\n Содержит в себе " + epicList.get(key).epicTask.size() +" подзадачу(и)");
            i++;
        }
    }

    void printAllSubTask() {
        int i = 1;
        System.out.println("Список всех подзадач:");
        for (int key : subTaskList.keySet()) {
            System.out.println("подзадача №" + i
                    + "\n Назавание:" + subTaskList.get(key).getTitle()
                    + "\n Описание:" + subTaskList.get(key).getDescription()
                    + "\n id=" + subTaskList.get(key).getId()
                    + "\n Статус:" + subTaskList.get(key).getStatus()
                    + "\n относится к глобальгой задаче id " + subTaskList.get(key).getEpicId());
            i++;
        }
    }

    void printAllSubTaskEpic(int id) {
        int i = 1;
        System.out.println("Список всех подзадач глобальной задачи:");
        for (SubTask subTask : epicList.get(id).epicTask) {
            System.out.println("подзадача №" + i
                    + "\n Назавание:" + subTask.getTitle()
                    + "\n Описание:" + subTask.getDescription()
                    + "\n id=" + subTask.getId()
                    + "\n Статус:" + subTask.getStatus()
                    + "\n относится к глобальгой задаче id " + subTask.getEpicId());
            i++;
        }
    }

    void removeIdTask(int id) {
        if (taskList.containsKey(id)) {
            for (int key : taskList.keySet()) {
                if (id == taskList.get(key).getId()) {
                    taskList.remove(id);
                    return;
                }
            }
        } else {
            System.out.println("Задачи с таким id нет");
        }
    }

    void removeIdEpic(int id) {
        ArrayList<Integer> numberSubtask = new ArrayList<>();
        if (epicList.containsKey(id)) {
            for (int keyEpic : epicList.keySet()) {
                if (id == epicList.get(keyEpic).getId()) {
                    epicList.remove(id);
                    break;
                }
            }
            for (int keySub : subTaskList.keySet()) {
                if (id == subTaskList.get(keySub).getEpicId()) {
                    numberSubtask.add(subTaskList.get(keySub).getId());
                }
            }
            for (int key : numberSubtask) {
                subTaskList.remove(key);
            }
        } else {
            System.out.println("Глобалтной задачи с таким id нет");
        }
    }

    void removeIdSubTask(int id) {
        if (subTaskList.containsKey(id)) {
            for (int keySub : subTaskList.keySet()) {
                if (id == subTaskList.get(keySub).getId()) {
                    subTaskList.remove(id);
                    break;
                }
            }
            for (int keyEpic : epicList.keySet()) {
                for (SubTask subTask : epicList.get(keyEpic).epicTask){
                    if (id == subTask.getEpicId())   {
                        epicList.get(keyEpic).epicTask.remove(subTask);
                        break;
                    }
                }
            }
        } else {
            System.out.println("Подзадачи с таким id нет");
        }

    }

    void removeAllTask() {
        taskList.clear();
    }

    void removeAllEpic() {
        epicList.clear();
        subTaskList.clear();
    }

    void removeAllSabTask() {
        subTaskList.clear();
        for(int key : epicList.keySet()) {
            epicList.get(key).epicTask.clear();
        }
    }

    private String assignEpicStatus(int id) {
        ArrayList<String> allStatus = new ArrayList<>();
        String status = "New";
        for (Integer key: subTaskList.keySet()) {
            if (id == subTaskList.get(key).getEpicId()) {
                allStatus.add(subTaskList.get(key).getStatus());
            }
        }
        if (allStatus.isEmpty()) {
            return status;
        } else {
            for(String tempStatus: allStatus) {
                status = tempStatus;
                if (status.equals("IN_PROGRESS")) {
                    return status;
                }
            }
        }
        return status;
    }
}
