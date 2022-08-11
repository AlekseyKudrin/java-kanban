import manager.InMemoryTaskManager;
import manager.Managers;
import task.Epic;
import task.StatusTask;
import task.SubTask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = Managers.getDefault();
        //InMemoryTaskManager manager = new InMemoryTaskManager();
        //Ввод начальных задач
        Task taskOne = new Task("TaskOne", "testTaskOne", StatusTask.NEW);
        Task taskTwo = new Task("TaskTwo", "testTaskTwo", StatusTask.IN_PROGRESS);
        SubTask subTaskOne = new SubTask("one", "testSubTaskOne", StatusTask.DONE,6);
        SubTask subTaskTwo = new SubTask("two", "testSubTuskTwo", StatusTask.NEW,6);
        SubTask subTaskThree = new SubTask("three", "testSubTuskTwo", StatusTask.NEW,7);
        Epic epicOne = new Epic("EpicOne", "test1");
        Epic epicTwo = new Epic("EpicTwo", "test2");

        manager.addTask(taskOne);
        manager.addTask(taskTwo);
        manager.addSubTask(subTaskOne);
        manager.addSubTask(subTaskTwo);
        manager.addSubTask(subTaskThree);
        manager.addEpic(epicOne);
        manager.addEpic(epicTwo);

        //Обновление подзадачи
        SubTask upSubThree = new SubTask("two", "testSubTuskTwo", StatusTask.DONE,5,6);
        manager.updateSubTask(upSubThree);

        //Обновление глобальной задачи
        Epic upEpic = new Epic("EpicThree", "test1");
        manager.updateEpic(upEpic);

        //Обновление задачи
        Task upTask = new Task("TaskOne", "testTaskOne", StatusTask.IN_PROGRESS, 1);
        manager.updateTask(upTask);


        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(1);
        manager.getHistory();

        System.out.println(taskOne);

        /*manager.printAllTask();
        manager.printAllEpic();
        manager.printAllSubTask();

        manager.printTargetTask(2);
        manager.printTargetEpic(6);
        manager.printTargetSubTask(3);
        manager.printAllSubTaskEpic(7);

        manager.removeIdTask(1);
        manager.removeIdEpic(6);
        manager.removeIdSubTask(3);

        manager.removeAllTask();
        manager.removeAllEpic();
        manager.removeAllSabTask();*/
    }
}
