import extensions.TaskManager;
import manager.Managers;
import task.Epic;
import util.StatusTask;
import task.SubTask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        //Ввод начальных задач
        Task taskOne = new Task("TaskOne", "testTaskOne", StatusTask.NEW);
        Task taskTwo = new Task("TaskTwo", "testTaskTwo", StatusTask.IN_PROGRESS);
        SubTask subTaskOne = new SubTask("one", "testSubTaskOne", StatusTask.DONE, 6);
        SubTask subTaskTwo = new SubTask("two", "testSubTuskTwo", StatusTask.NEW, 6);
        SubTask subTaskThree = new SubTask("three", "testSubTuskTwo", StatusTask.NEW, 6);
        Epic epicOne = new Epic("EpicOne", "test1");
        Epic epicTwo = new Epic("EpicTwo", "test2");

        manager.addTask(taskOne);
        manager.addTask(taskTwo);
        manager.addSubTask(subTaskOne);
        manager.addSubTask(subTaskTwo);
        manager.addSubTask(subTaskThree);
        manager.addEpic(epicOne);
        manager.addEpic(epicTwo);


        manager.getTask(2);
        manager.getTask(3);
        manager.getTask(4);
        manager.getTask(5);
        manager.getTask(4);
        manager.getTask(3);
        manager.getTask(6);

        manager.removeIdTask(2);
        manager.removeIdEpic(6);

        manager.getHistory();


    }
}
