public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        //Ввод начальных задач
        Task taskOne = new Task("TaskOne", "testTaskOne", "NEW");
        Task taskTwo = new Task("TaskTwo", "testTaskTwo", "IN_PROGRESS");
        SubTask subTaskOne = new SubTask("one", "testSubTaskOne", "IN_PROGRESS",6);
        SubTask subTaskTwo = new SubTask("two", "testSubTuskTwo", "NEW",6);
        SubTask subTaskThree = new SubTask("three", "testSubTuskTwo", "NEW",7);
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
        SubTask upSubThree = new SubTask("two", "testSubTuskTwo", "DONE",5,6);
        manager.updateSubTask(upSubThree);

        //Обновление глобальной задачи
        Epic upEpic = new Epic("EpicThree", "test1");
        manager.updateEpic(upEpic);

        //Обновление задачи
        Task upTask = new Task("TaskOne", "testTaskOne", "IN_PROGRESS", 1);
        manager.updateTask(upTask);

        manager.printAllTask();
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
        manager.removeAllSabTask();
    }
}
