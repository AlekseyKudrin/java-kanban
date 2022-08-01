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
        return "Назавание:" + getTitle()
                + "\n Описание:" + getDescription()
                + "\n id=" + getId()
                + "\n Статус:" + getStatus()
                + "\n Содержит в себе " + subTaskIds.size() +" подзадачу(и)";
    }
}