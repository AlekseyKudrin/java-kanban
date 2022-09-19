package extensions;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

public interface TaskManager {

    void getTask(int id);

    //void getSubTask(int id);

    //void getEpic(int id);

    void getHistory();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void printTargetTask(int id);

    void printTargetEpic(int id);

    void printTargetSubTask(int id);

    void printAllTask();

    void printAllEpic();

    void printAllSubTask();

    void printAllSubTaskEpic(int id);

    void removeIdTask(int id);

    void removeIdEpic(int id);

    void removeIdSubTask(int id);

    void removeAllTask();

    void removeAllEpic();

    void removeAllSabTask();
}
