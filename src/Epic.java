import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<SubTask> epicTask;

    public Epic(String title, String description) {
        super(title, description);
        epicTask = new ArrayList<>();
    }
}