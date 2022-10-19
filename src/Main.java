import http.KVServer;
import manager.FileBackedTasksManager;
import manager.Managers;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
       FileBackedTasksManager manager = Managers.getBacked();

/*        manager.addTask(new Task("task one", "test task one", StatusTask.NEW
                , LocalDateTime.of(2022, 9, 5, 10, 1)
                , Duration.ofMinutes(30)));

        manager.addEpic(new Epic("epic one", "test epic one"));

        manager.addSubTask(new SubTask("subTask one", "test subTask one", StatusTask.NEW, 2
                , LocalDateTime.of(2022, 9, 1, 5,10)
                , Duration.ofMinutes(60)));
        manager.addSubTask(new SubTask("subTask two", "test subTask two", StatusTask.NEW, 2
                , LocalDateTime.of(2022, 9, 3, 8,20)
                , Duration.ofMinutes(30)));*/
        KVServer server = new KVServer();
        server.start();

    }
}
