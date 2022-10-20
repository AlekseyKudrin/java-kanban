package test;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import util.ErrorException;
import util.StatusTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest {

    @Test
    void save() {
        FileBackedTasksManager manager = new FileBackedTasksManager();
        Epic epic = new Epic("epic one test", "create epic one");
        SubTask subTaskOne = new SubTask("subTask one test", "create subTask one", StatusTask.NEW, 2,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));
        SubTask subTaskTwo = new SubTask("subTask two test", "create subTask two", StatusTask.NEW, 2,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));
        SubTask subTaskThree = new SubTask("subTask three test", "create subTask three", StatusTask.NEW, 2,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));

        manager.addEpic(epic);
        manager.addSubTask(subTaskOne);
        manager.addSubTask(subTaskTwo);
        manager.addSubTask(subTaskThree);

        String[] lines;
        final String PATH = "resources/data.csv";
        try {
            lines = Files.readString(Path.of(PATH)).split("\n");
        } catch (IOException e) {
            throw new ErrorException("Не удалось считать файл");
        }
        assertEquals(6, lines.length);

        assertEquals("2,SUBTASK,subTask one test,NEW,create subTask one,2,2000-01-01T01:00,PT1H,2000-01-01T02:00\r",
                lines[2]);
        assertEquals("3,SUBTASK,subTask two test,NEW,create subTask two,2,2000-01-01T01:00,PT1H,2000-01-01T02:00\r",
                lines[3]);
        assertEquals("4,SUBTASK,subTask three test,NEW,create subTask three,2,2000-01-01T01:00,PT1H,2000-01-01T02:00\r",
                lines[4]);
    }

    @Test
    void loadFromFile() {
        FileBackedTasksManager manager = new FileBackedTasksManager();
        save();

        manager.loadFromFile();

        assertEquals(1, manager.epics.size());
        assertEquals(3, manager.subTasks.size());
    }
}