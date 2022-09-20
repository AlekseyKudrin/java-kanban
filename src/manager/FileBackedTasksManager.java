package manager;


import extensions.HistoryManager;
import task.Epic;
import task.SubTask;
import task.Task;
import util.ErrorException;
import util.StatusTask;
import util.TypeTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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
        if (!parts[0].equals("")) {
            if (parts.length != 5 && parts.length != 6) {
                for (String str : parts) {
                    getTask(Integer.parseInt(str));
                }
            } else {
                try {
                    int id = Integer.parseInt(parts[0]);
                    TypeTask type = TypeTask.valueOf(parts[1]);
                    String name = parts[2];
                    StatusTask status = StatusTask.valueOf(parts[3]);
                    String description = parts[4];
                    if (TypeTask.TASK.equals(type)) {
                        Task task = new Task(name, description, status);
                        task.setId(id);
                        this.id = id;
                        super.addTask(task);
                    }
                    if (TypeTask.EPIC.equals(type)) {
                        Epic epic = new Epic(name, description);
                        epic.setId(id);
                        epic.setStatus(status);
                        this.id = id;
                        super.addEpic(epic);
                    }
                    if (TypeTask.SUBTASK.equals(type)) {
                        int epicId = Integer.parseInt(parts[5]);
                        SubTask subTask = new SubTask(name, description, status, epicId);
                        subTask.setId(id);
                        this.id = id;
                        super.addSubTask(subTask);
                    }
                } catch (IllegalArgumentException exp) {
                    for (String str : parts) {
                        getTask(Integer.parseInt(str));
                    }
                }
            }
        }
    }
}