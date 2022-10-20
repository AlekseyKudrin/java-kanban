package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import task.Task;
import util.StatusTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager manager = new InMemoryHistoryManager();

    @Test
    void add() {
        Task task = new Task("task one test", "create task one", StatusTask.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));
        manager.add(task);
        final List<Integer> history = manager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void remove() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task taskOne = new Task("task one test", "create task one", StatusTask.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));
        Task taskTwo = new Task("task two test", "create task two", StatusTask.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));
        Task taskThree = new Task("task three test", "create task three", StatusTask.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));

        taskManager.addTask(taskOne);
        taskManager.addTask(taskTwo);
        taskManager.addTask(taskThree);
        manager.add(taskOne);
        manager.add(taskTwo);
        manager.add(taskThree);

        manager.remove(2);

        assertEquals(2, manager.history.size(), "История не пустая.");
    }
}