package Http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import manager.Managers;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class HttpTaskServer {
    private static final int PORT = 5555;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).
            create();

    static HttpServer httpServer;


    public HttpTaskServer() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
    }

    public void start() {

        System.out.println("Запускаем сервер на порту " + PORT);

        httpServer.start();
    }

    public void stop() {
        System.out.println("Сервер остановлен");
        httpServer.stop(0);
    }

    public static class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            FileBackedTasksManager fileManager = Managers.getBacked();
            fileManager.loadFromFile();

            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            String response = "";
            int rCode = 404;

            switch (method) {

                case "GET":
                    if (Pattern.matches("^/tasks/task$", path) && exchange.getRequestURI().getQuery() != null) {
                        int id = parserId(exchange.getRequestURI().getQuery());
                        response = gson.toJson(fileManager.tasks.get(id));
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/task$", path)) {
                        response = gson.toJson(fileManager.tasks.values());
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/subtask/epic$", path) && exchange.getRequestURI().getQuery() != null) {
                        response = response = gson.toJson(fileManager.subTasks.values());
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/epic$", path) && exchange.getRequestURI().getQuery() != null) {
                        response = response = gson.toJson(fileManager.epics.values());
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        response = gson.toJson(fileManager.epics.values());
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/subtask$", path) && exchange.getRequestURI().getQuery() != null) {
                        response = response = gson.toJson(fileManager.subTasks.values());
                        rCode = 200;
                    } else if(Pattern.matches("^/tasks/subtask$", path)) {
                        response = gson.toJson(fileManager.subTasks.values());
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/history$", path)) {
                        response = gson.toJson(fileManager.history.getHistory());
                        rCode = 200;
                    }

                    break;

                case "POST":
                    if (Pattern.matches("^/tasks/task$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(taskBody, Task.class);
                        fileManager.addTask(task);
                        rCode = 201;
                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(taskBody, Epic.class);
                        fileManager.addEpic(epic);
                        rCode = 201;
                    } else if (Pattern.matches("^/tasks/subtask$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        SubTask subtask = gson.fromJson(taskBody, SubTask.class);
                        fileManager.addSubTask(subtask);
                        rCode = 201;
                    }
                    break;
                case "DELETE":

                    if (Pattern.matches("^/tasks/task$", path) && exchange.getRequestURI().getQuery() != null) {
                        int id = parserId(exchange.getRequestURI().getQuery());;
                        fileManager.removeIdTask(id);
                        response = gson.toJson("Задача c id:" + id + " удалена!");
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/task$", path)) {
                        fileManager.removeAllTask();
                        response = gson.toJson("Все задачи удалены");
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/epic$", path) && exchange.getRequestURI().getQuery() != null) {
                        int id = parserId(exchange.getRequestURI().getQuery());
                        fileManager.removeIdEpic(id);
                        response = gson.toJson("Комплексная задача c id:" + id + " удалена!");
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        fileManager.removeAllEpic();
                        response = gson.toJson("Все комплексные задачи и подзадачи удалены");
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/subtask$", path) && exchange.getRequestURI().getQuery() != null) {
                        int id = parserId(exchange.getRequestURI().getQuery());
                        fileManager.removeIdSubTask(id); //
                        response = gson.toJson("Подзадача c id:" + id + " удалена!");
                        rCode = 200;

                    }
                    break;

                case "PUT":
                    if (Pattern.matches("^/tasks/task$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(taskBody, Task.class);
                        fileManager.updateTask(task);
                        rCode = 202;
                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(taskBody, Epic.class);
                        fileManager.updateEpic(epic);
                        rCode = 202;
                    } else if (Pattern.matches("^/tasks/subtask$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        SubTask subtask = gson.fromJson(taskBody, SubTask.class);
                        fileManager.updateSubTask(subtask);
                        rCode = 202;
                    } else {
                        rCode = 404;
                        response = gson.toJson("Такой команды нет");
                    }
                    break;
            }

            exchange.sendResponseHeaders(rCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }


    private static int parserId(String query) {
        String[] queryArray = query.split("=");
        int id = Integer.parseInt(queryArray[1]);
        return id;
    }
}
