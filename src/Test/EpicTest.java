package Test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.StatusTask.*;

class EpicTest {

    private static InMemoryTaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void emptyListSubTask() {
        manager.addEpic(new Epic("epic one", "create epic one"));

        assertEquals(0, manager.epics.get(1).subTask.size());
    }

    @Test
    public void shouldCheckAllSubTaskNew() {
        manager.addEpic(new Epic("epic one", "create epic one"));
        manager.addSubTask(new SubTask("subTask one test", "create subTask one", NEW, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));
        manager.addSubTask(new SubTask("subTask two test", "create subTask two", NEW, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));
        manager.addSubTask(new SubTask("subTask three test", "create subTask three", NEW, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));

        assertEquals(NEW, manager.epics.get(1).getStatus());
    }

    @Test
    public void shouldCheckAllSubTaskDone() {
        manager.addEpic(new Epic("epic one", "create epic one"));
        manager.addSubTask(new SubTask("subTask one test", "create subTask one", DONE, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));
        manager.addSubTask(new SubTask("subTask two test", "create subTask two", DONE, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));
        manager.addSubTask(new SubTask("subTask three test", "create subTask three", DONE, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));

        assertEquals(DONE, manager.epics.get(1).getStatus());
    }

    @Test
    public void shouldCheckAllSubTaskDoneNEW() {
        manager.addEpic(new Epic("epic one", "create epic one"));
        manager.addSubTask(new SubTask("subTask one test", "create subTask one", DONE, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));
        manager.addSubTask(new SubTask("subTask two test", "create subTask two", DONE, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));
        manager.addSubTask(new SubTask("subTask three test", "create subTask three", NEW, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));

        assertEquals(IN_PROGRESS, manager.epics.get(1).getStatus());
    }

    @Test
    public void shouldCheckAllSubTaskInProgress() {
        manager.addEpic(new Epic("epic one", "create epic one"));
        manager.addSubTask(new SubTask("subTask one test", "create subTask one", IN_PROGRESS, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));
        manager.addSubTask(new SubTask("subTask two test", "create subTask two", IN_PROGRESS, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));
        manager.addSubTask(new SubTask("subTask three test", "create subTask three", IN_PROGRESS, 1,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));

        assertEquals(IN_PROGRESS, manager.epics.get(1).getStatus());
    }
}