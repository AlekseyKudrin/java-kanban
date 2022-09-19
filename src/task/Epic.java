package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    public List<Integer> subTaskIds;

    public Epic(String title, String description) {
        super(title, description);
        subTaskIds = new ArrayList<>();
    }

    @Override
    public String toString() {
        return id + "," +"EPIC"+  "," + title +  "," + status + "," + description+ ",";
    }
}