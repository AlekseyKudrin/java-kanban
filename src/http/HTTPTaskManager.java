package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import manager.FileBackedTasksManager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class HTTPTaskManager extends FileBackedTasksManager {

    private static final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).
            create();
    protected KVTaskClient kvTaskClient;

    protected String path;

    public HTTPTaskManager(String path) {
        this.path = path;
    }

    public void getToken() {
        kvTaskClient = new KVTaskClient(path);
        kvTaskClient.register();
    }

    public void saveTasks() throws IOException {
        if (kvTaskClient == null) {
            System.out.println("Требуется регистрация");
            return;
        }

        this.loadFromFile();
        kvTaskClient.put("/tasks", gson.toJson(tasks.values()));
        kvTaskClient.put("/epics", gson.toJson(epics.values()));
        kvTaskClient.put("/subtasks", gson.toJson(subTasks.values()));
        kvTaskClient.put("/history", gson.toJson(historyToString(history)));
    }

    public void loadTasks() {
        String json = kvTaskClient.load("/tasks");
        Type type = new TypeToken<ArrayList<Task>>(){}.getType();
        ArrayList<Task> tasksList = gson.fromJson(json, type);
        for (Task task : tasksList) {
            addTaskFromKVServer(task);
        }
        tasks.putAll((Map<? extends Integer, ? extends Task>) tasksList);

        json = kvTaskClient.load("/epics");
        type = new TypeToken<ArrayList<Epic>>(){}.getType();
        ArrayList<Epic> epicsList = gson.fromJson(json, type);
        for (Epic epic : epicsList) {
            addEpicFromKVServer(epic);
        }
        epics.putAll((Map<? extends Integer, ? extends Epic>) epicsList);

        json = kvTaskClient.load("/subtasks");
        type = new TypeToken<ArrayList<SubTask>>(){}.getType();
        ArrayList<SubTask> subtasksList = gson.fromJson(json, type);
        for (SubTask subtask : subtasksList) {
            addSubtaskFromKVServer(subtask);
        }
        subTasks.putAll((Map<? extends Integer, ? extends SubTask>) subtasksList);

        json = kvTaskClient.load("/history");
        String historyLine = json.substring(1, json.length() - 1);
        if (!historyLine.equals("\"\"")) {
            String[] historyLineContents = historyLine.split(",");
            for (String s : historyLineContents) {
                history.add(tasks.get(Integer.parseInt(s)));
            }
        }
        save();
    }

    public int addTaskFromKVServer(Task task) {
        task.setId(task.getId());
        tasks.put(task.getId(), task);
        save();
        return task.getId();
    }

    public int addEpicFromKVServer(Epic epic) {
        epic.setId(epic.getId());
        epics.put(epic.getId(), epic);
        save();
        return epic.getId();
    }

    public int addSubtaskFromKVServer(SubTask subtask) {
        subtask.setId(subtask.getId());
        subTasks.put(subtask.getId(), subtask);
        save();
        return subtask.getId();
    }

    @Override
    public void addTask(Task task) {
        task.setId(task.getId());
        tasks.put(task.getId(), task);
        save();
    }
    @Override
    public void addEpic(Epic epic) {
        epic.setId(epic.getId());
        epics.put(epic.getId(), epic);
        save();
    }
    @Override
    public void addSubTask(SubTask subtask) {
        subtask.setId(subtask.getId());
        subTasks.put(subtask.getId(), subtask);
        save();
    }
}
