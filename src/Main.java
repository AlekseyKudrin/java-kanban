import extensions.TaskManager;
import manager.FileBackedTasksManager;
import manager.Managers;
import task.Epic;
import util.StatusTask;
import task.SubTask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        FileBackedTasksManager manager = Managers.getBacked();
        manager.loadFromFile();

        //Ввод начальных задач
        manager.addTask(new Task("TaskOne", "testTaskOne", StatusTask.NEW));
        manager.addTask(new Task("TaskTwo", "testTaskTwo", StatusTask.IN_PROGRESS));
        manager.addSubTask(new SubTask("one", "testSubTaskOne", StatusTask.DONE, 6));
        manager.addSubTask(new SubTask("two", "testSubTuskTwo", StatusTask.NEW, 6));
        manager.addSubTask(new SubTask("three", "testSubTuskTwo", StatusTask.NEW, 6));
        manager.addEpic(new Epic("EpicOne", "test1"));
        manager.addEpic(new Epic("EpicTwo", "test2"));

        manager.getTask(2);
        manager.getTask(3);
        manager.getTask(4);
        manager.getTask(5);
        manager.getTask(4);
        manager.getTask(3);
        manager.getTask(6);

        manager.getHistory();


    }
}
