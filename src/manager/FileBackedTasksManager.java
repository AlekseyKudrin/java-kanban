package manager;


import extensions.HistoryManager;
import task.Epic;
import task.SubTask;
import task.Task;
import util.ManagerSaveException;
import util.StatusTask;
import util.TypeTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final static String PATH = "resources\\data.csv";

    public static void main(String[] args) {
        FileBackedTasksManager backed = new FileBackedTasksManager();
        backed.loadFromFile();

        backed.addTask(new Task("TaskOne", "testTaskOne", StatusTask.NEW));
        backed.addTask(new Task("TaskTwo", "testTaskTwo", StatusTask.IN_PROGRESS));
        backed.addSubTask(new SubTask("one", "testSubTaskOne", StatusTask.DONE, 6));
        backed.addSubTask(new SubTask("two", "testSubTuskTwo", StatusTask.NEW, 6));
        backed.addSubTask(new SubTask("three", "testSubTuskTwo", StatusTask.NEW, 6));
        backed.addEpic(new Epic("EpicOne", "test1"));
        backed.addEpic(new Epic("EpicTwo", "test2"));

        backed.getTask(1);
        backed.getTask(2);
        backed.getTask(3);
        backed.getTask(4);
        backed.getTask(5);
        backed.getTask(6);
        backed.getTask(7);
        backed.getTask(8);

        backed.getHistory();
    }
    @Override
    public void addTask(Task task) {
        super.addTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getStackTrace();
        }
    }
    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getStackTrace();
        }
    }
    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getStackTrace();
        }
    }
    @Override
    public void getTask(int id) {
        super.getTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getStackTrace();
        }
    }
    public void save() throws ManagerSaveException {
        try {
            Path path = Path.of(PATH);
            final String head = "id,type,name,status,description,epic" + System.lineSeparator();
            String data = head +
                    this + System.lineSeparator()
                    +FileBackedTasksManager.historyToString(history);
            Files.writeString(path, data);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка, при записи файла произошел сбой!");
        }
    }
    static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (int value : manager.getHistory())
            sb.append(value).append(",");
        return sb.toString();
    }
    public void loadFromFile() {
        try {
            String[] lines = Files.readString(Path.of(PATH), StandardCharsets.UTF_8).split(System.lineSeparator());
            for (int i = 1; i < lines.length; i++) {
                fromString(lines[i]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer key : tasks.keySet()) {
            sb.append(tasks.get(key)).append(System.lineSeparator());
        }
        for (Integer key : epics.keySet()) {
            sb.append(epics.get(key)).append(System.lineSeparator());
        }
        for (Integer key : subTasks.keySet()) {
            sb.append(subTasks.get(key)).append(System.lineSeparator());
        }
        return sb.toString();

    }
    public void fromString(String line) {
        String[] parts = line.split(",");
        try {
            if(!parts[0].equals("")) {
                if (parts.length != 5 & parts.length != 6) {
                    for (String str : parts) {
                        getTask(Integer.parseInt(str));
                    }
                } else {
                    if (parts.length == 5) {
                        int id = Integer.parseInt(parts[0]);
                        TypeTask type = TypeTask.valueOf(parts[1]);
                        String name = parts[2];
                        StatusTask status = StatusTask.valueOf(parts[3]);
                        String description = parts[4];
                        if (type.equals(TypeTask.TASK)) {
                            Task task = new Task(name, description, status);
                            task.setId(id);
                            this.id = id;
                            super.addTask(task);
                        }
                        if (type.equals(TypeTask.EPIC)) {
                            Epic epic = new Epic(name, description);
                            epic.setId(id);
                            epic.setStatus(status);
                            this.id = id;
                            super.addEpic(epic);
                        }
                    }
                    if (parts.length == 6) {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[2];
                        StatusTask status = StatusTask.valueOf(parts[3]);
                        String description = parts[4];
                        int epicId = Integer.parseInt(parts[5]);
                        SubTask subTask = new SubTask(name, description, status, epicId);
                        subTask.setId(id);
                        this.id = id;
                        super.addSubTask(subTask);
                    }
                }
            }
        } catch (IllegalArgumentException exp) {
            for (String str : parts) {
                getTask(Integer.parseInt(str));
            }
        }
    }
}