import extensions.TaskManager;
//import manager.FileBackedTasksManager;
import manager.FileBackedTasksManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import task.Epic;
import util.StatusTask;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        FileBackedTasksManager manager = Managers.getBacked();

        manager.addTask(new Task("task one", "test task one", StatusTask.NEW
                , LocalDateTime.of(2022, 9, 5, 10, 1)
                , Duration.ofMinutes(30)));

        manager.addEpic(new Epic("epic one", "test epic one"));

        manager.addSubTask(new SubTask("subTask one", "test subTask one", StatusTask.NEW, 2
                , LocalDateTime.of(2022, 9, 1, 5,10)
                , Duration.ofMinutes(60)));
        manager.addSubTask(new SubTask("subTask two", "test subTask two", StatusTask.NEW, 2
                , LocalDateTime.of(2022, 9, 3, 8,20)
                , Duration.ofMinutes(30)));
    }
}
