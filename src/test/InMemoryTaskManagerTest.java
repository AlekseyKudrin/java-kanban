package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;
import util.StatusTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTaskManagerTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    void addTask() {
        Task task = new Task("task one test", "create task one", StatusTask.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));

        manager.addTask(task);

        assertEquals(1, manager.tasks.size());
    }

    @Test
    void addEpic() {
        Epic epic = new Epic("epic one test", "create epic one");

        manager.addEpic(epic);

        assertEquals(1, manager.epics.size());

    }

    @Test
    void addSubTask() {
        SubTask subTask = new SubTask("subTask one test", "create subTask one", StatusTask.NEW, 2,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));

        manager.addSubTask(subTask);

        assertEquals(1, manager.subTasks.size());
    }

    @Test
    void updateTask() {
        Task task = new Task("task one test", "create task one", StatusTask.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));
        Task taskUpdate = new Task("task one test", "create task one", StatusTask.DONE, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));

        manager.addTask(task);
        manager.updateTask(taskUpdate);

        assertEquals(taskUpdate, manager.tasks.get(1));
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("epic one test", "create epic one");
        Epic epicUpdate = new Epic("epic one test", "create epic one");

        manager.addEpic(epic);
        manager.updateEpic(epicUpdate);

        assertEquals(epicUpdate, manager.epics.get(1));

    }

    @Test
    void updateSubTask() {
        SubTask subTask = new SubTask("subTask one test", "create subTask one", StatusTask.NEW, 2,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));
        SubTask subTaskUpdate = new SubTask("subTask one test", "create subTask one", StatusTask.IN_PROGRESS, 1, 2,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(40));

        manager.addSubTask(subTask);
        manager.updateSubTask(subTaskUpdate);

        assertEquals(subTaskUpdate, manager.subTasks.get(1));
    }

    @Test
    void removeIdTask() {
        Task task = new Task("task one test", "create task one", StatusTask.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));

        manager.addTask(task);
        manager.removeIdTask(1);

        assertTrue(manager.tasks.isEmpty());

    }

    @Test
    void removeIdEpic() {
        Epic epic = new Epic("epic one test", "create epic one");

        manager.addEpic(epic);
        manager.removeIdEpic(1);

        assertTrue(manager.epics.isEmpty());
    }

    @Test
    void removeIdSubTask() {
        SubTask subTask = new SubTask("subTask one test", "create subTask one", StatusTask.NEW, 2,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));

        manager.addSubTask(subTask);
        manager.removeIdSubTask(1);

        assertTrue(manager.subTasks.isEmpty());
    }

    @Test
    void removeAllTask() {
        addTask();
        addEpic();
        addSubTask();

        manager.removeIdTask(1);
        manager.removeIdEpic(2);
        manager.removeIdSubTask(3);

        assertTrue(manager.tasks.isEmpty());
        assertTrue(manager.epics.isEmpty());
        assertTrue(manager.subTasks.isEmpty());
    }

    @Test
    void removeAllEpic() {
        addEpic();
        manager.addEpic(new Epic("epic two test", "create epic two"));

        manager.removeAllEpic();

        assertTrue(manager.epics.isEmpty());
    }

    @Test
    void removeAllSabTask() {
        addSubTask();
        manager.addSubTask(new SubTask("subTask two test", "create subTask two", StatusTask.NEW, 2,
                LocalDateTime.of(2000, 1, 1, 1, 10), Duration.ofMinutes(60)));

        manager.removeAllSabTask();

        assertTrue(manager.subTasks.isEmpty());
    }

    @Test
    void getTask() {
        addTask();
        addEpic();
        addSubTask();
        List<Integer> list = new ArrayList<>();

        list.add(1);
        list.add(2);
        list.add(3);
        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);

        assertEquals(list.get(0), manager.history.getHistory().get(0));
        assertEquals(list.get(1), manager.history.getHistory().get(1));
        assertEquals(list.get(2), manager.history.getHistory().get(2));
    }

    @Test
    void calculateCompletion() {
        addTask();
        LocalDateTime endTime = LocalDateTime.of(2000, 1, 1, 2, 0);

        assertEquals(endTime, manager.tasks.get(1).getEndTime());

    }

    @Test
    void assignEpicStatus() {
        addEpic();

        manager.assignEpicStatus(1);

        assertEquals(StatusTask.NEW, manager.epics.get(1).getStatus());

        manager.addSubTask(new SubTask("subTask one test", "create subTask one", StatusTask.NEW, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));

        assertEquals(StatusTask.NEW, manager.epics.get(1).getStatus());

        manager.updateSubTask(new SubTask("subTask one test", "create subTask one", StatusTask.IN_PROGRESS, 2, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(40)));

        assertEquals(StatusTask.IN_PROGRESS, manager.epics.get(1).getStatus());


    }

}