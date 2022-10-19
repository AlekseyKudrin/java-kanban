package manager;


import extensions.HistoryManager;
import task.Epic;
import task.SubTask;
import task.Task;
import util.ErrorException;
import util.StatusTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final static String PATH = "resources\\data.csv";

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void getTask(int id) {
        super.getTask(id);
        save();
    }

    public void save() {
        try {
            Path path = Path.of(PATH);
            final String head = "id,type,name,status,description,epic" + System.lineSeparator();
            String data = head +
                    this + System.lineSeparator()
                    + FileBackedTasksManager.historyToString(history);
            Files.writeString(path, data);
        } catch (IOException e) {
            throw new ErrorException("Ошибка, при записи файла произошел сбой!");
        }
    }

    protected static String historyToString(HistoryManager manager) {
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
        if (!parts[0].equals("")) {
            if (parts.length != 8 && parts.length != 9) {
                for (String str : parts) {
                    getTask(Integer.parseInt(str));
                }
            } else {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[2];
                    String description = parts[4];
                    if ("TASK".equals(parts[1])) {
                        StatusTask status = StatusTask.valueOf(parts[3]);
                        LocalDateTime startTime = LocalDateTime.parse(parts[5]);
                        Duration duration = Duration.parse(parts[6]);
                        Task task = new Task(name, description, status, startTime, duration);
                        task.setId(id);
                        this.id = id;
                        super.addTask(task);
                    }
                    if ("EPIC".equals(parts[1])) {
                        StatusTask status = StatusTask.valueOf(parts[3]);
                        Epic epic = new Epic(name, description);
                        epic.setId(id);
                        epic.setStatus(status);
                        this.id = id;
                        super.addEpic(epic);
                    }
                    if ("SUBTASK".equals(parts[1])) {
                        StatusTask status = StatusTask.valueOf(parts[3]);
                        int epicId = Integer.parseInt(parts[5]);
                        LocalDateTime startTime = LocalDateTime.parse(parts[6]);
                        Duration duration = Duration.parse(parts[7]);
                        SubTask subTask = new SubTask(name, description, status, epicId, startTime, duration);
                        subTask.setId(id);
                        this.id = id;
                        super.addSubTask(subTask);
                    }
                }
            }
        }
    }