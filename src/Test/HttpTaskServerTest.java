package Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import http.HTTPTaskManager;
import http.HttpTaskServer;
import http.KVServer;
import http.LocalDateAdapter;
import manager.FileBackedTasksManager;
import manager.Managers;

import org.junit.jupiter.api.*;
import task.Epic;
import task.SubTask;
import task.Task;
import util.StatusTask;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static util.StatusTask.NEW;


public class HttpTaskServerTest {

    HttpTaskServer httpTaskServer;
    HTTPTaskManager httpTaskManager;
    HTTPTaskManager loadedTaskManager;
    HttpClient httpClient = HttpClient.newHttpClient();
    KVServer kvServer;
    String path = "http://localhost:8080";
    private static final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).
            create();


    Task taskOne = new Task("task one", "test task one",  NEW, LocalDateTime.of(2022, 10, 10, 10, 30,0), Duration.ofMinutes(30));

    Task taskTwo = new Task("task two", "test task two", NEW, LocalDateTime.of(2022, 10, 12, 11, 0, 15), Duration.ofMinutes(60));

    Epic epicOne = new Epic("epic one",  "test epic one");

    SubTask subTaskOne = new SubTask("subTask one test", "create subTask one", StatusTask.NEW, 2,
            LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));
    SubTask subTaskTwo = new SubTask("subTask two test", "create subTask two", StatusTask.NEW, 2,
            LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));
    SubTask subTaskThree = new SubTask("subTask three test", "create subTask three", StatusTask.NEW, 2,
            LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60));



    public HttpTaskServerTest() {
        loadedTaskManager = Managers.loadedHTTPTasksManager();
    }
    @BeforeAll
    static void constructFileForeTests() {
        FileBackedTasksManager fileManager = Managers.getBacked();
        FileBackedTasksManager.setIdCounter(1);

        fileManager.addTask(new Task("task one", "test task one",  NEW, LocalDateTime.of(2022, 10, 10, 10, 30,0), Duration.ofMinutes(30)));

        fileManager.addTask(new Task("task two", "test task two", NEW, LocalDateTime.of(2022, 10, 12, 11, 0, 15), Duration.ofMinutes(60)));

        fileManager.addEpic(new Epic("epic one",  "test epic one"));

        fileManager.addSubTask(new SubTask("subTask one test", "create subTask one", StatusTask.NEW, 2,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));
        fileManager.addSubTask(new SubTask("subTask two test", "create subTask two", StatusTask.NEW, 2,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));
        fileManager.addSubTask(new SubTask("subTask three test", "create subTask three", StatusTask.NEW, 2,
                LocalDateTime.of(2000, 1, 1, 1, 0), Duration.ofMinutes(60)));


        fileManager.getTask(1);
        fileManager.getTask(3);
    }

    @BeforeEach
    void starServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        httpTaskManager = Managers.loadedHTTPTasksManager();
        httpTaskManager.getToken();
        httpTaskManager.saveTasks();
    }

    @AfterEach
    void stopStart() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    void getAllTasksAndEpicsAndSubtasks() throws IOException {
        httpTaskManager = loadedTaskManager;
        httpTaskManager.saveTasks();
        URI url = URI.create(path + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            List<Task> tasksList = gson.fromJson(json, type);
            List<Task> expectedList = new ArrayList<>(httpTaskManager.tasks.values());
            assertEquals(expectedList.size(), tasksList.size());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getTasksTest() {
        URI url = URI.create(path + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            List<Task> tasksList = gson.fromJson(json, type);
            List<Task> expectedList = new ArrayList<>(httpTaskManager.tasks.values());
            for (int i = 0; i < tasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTitle(), tasksList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getEpicsTest() {
        URI url = URI.create(path + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<ArrayList<Epic>>(){}.getType();
            List<Epic> tasksList = gson.fromJson(json, type);
            List<Epic> expectedList = new ArrayList<>(httpTaskManager.epics.values());
            for (int i = 0; i < tasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTitle(), tasksList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }


    @Test
    void getSubtasksTest() {
        URI url = URI.create(path + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<ArrayList<SubTask>>(){}.getType();
            List<SubTask> tasksList = gson.fromJson(json, type);
            List<SubTask> expectedList = new ArrayList<>(httpTaskManager.subTasks.values());
            for (int i = 0; i < tasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTitle(), tasksList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getTaskByIdTest() {
        URI url = URI.create(path + "/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            String json = response.body();
            System.out.println(response.body());
            Task task = gson.fromJson(json, Task.class);
            Task expectedTask = httpTaskManager.tasks.get(1);

            assertEquals(expectedTask.getTitle(), task.getTitle());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getSubtaskByIdTest() {
        URI url = URI.create(path + "/tasks/subtask?id=5");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            String json = response.body();
            SubTask subtask = gson.fromJson(json, SubTask.class);
            SubTask expectedSubtask = httpTaskManager.subTasks.get(5);

            assertEquals(expectedSubtask.getTitle(), subtask.getTitle());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getHistoryTest() {
        URI url = URI.create(path + "/tasks/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            String json = response.body();
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            List<Task> historyList = gson.fromJson(json, type);
            List<Integer> expectedHistoryList = new ArrayList<>(httpTaskManager.history.getHistory());
            for (int i = 0; i < historyList.size(); i++) {
                assertEquals(expectedHistoryList.get(i), historyList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getPrioritizedListTest() {
        URI url = URI.create(path + "/tasks/priority");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            String json = response.body();
            Type type = new TypeToken<List<Task>>(){}.getType();
            List<Task> prioritizedList = gson.fromJson(json, type);
            List<Task> expectedPrioritizedList = new ArrayList<>(httpTaskManager.tasks.values());
            for (int i = 0; i < prioritizedList.size(); i++) {
                assertEquals(expectedPrioritizedList.get(i).getTitle(), prioritizedList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }



    @Test
    void deleteTaskByIdFromServerTest() {
        HTTPTaskManager rollback = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/task?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            HTTPTaskManager taskManager = Managers.loadedHTTPTasksManager();
            assertNull(taskManager.tasks.get(2));
            httpTaskManager = rollback;
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void deleteEpicByIdAndAllSubtasksByThisEpicFromServerTest() {
        HTTPTaskManager rollback = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/epic?id=3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            httpTaskManager = Managers.loadedHTTPTasksManager();
            assertNull(httpTaskManager.epics.get(3));
            httpTaskManager = rollback;
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void deleteSubtaskByIdFromServerTest() {
        HTTPTaskManager rollback = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/subtask?id=6");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            httpTaskManager = Managers.loadedHTTPTasksManager();
            assertNull(httpTaskManager.subTasks.get(6));
            httpTaskManager = rollback;
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
}

















