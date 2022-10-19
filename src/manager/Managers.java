package manager;


import extensions.HistoryManager;
import extensions.TaskManager;
import http.HTTPTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getBacked() {
        return new FileBackedTasksManager();
    }

    public static HTTPTaskManager loadedHTTPTasksManager() {
        HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078");
        httpTaskManager.loadFromFile();
        //httpTaskManager.loadedFromFileTasksManager();
        return httpTaskManager;
    }
}
