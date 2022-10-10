package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    public Set<SubTask> subTask;

    public Epic(String title, String description) {
        super(title, description);
        subTask = new TreeSet<>((o1, o2) -> {
            if (o1.getStartTime() == null && o2.getStartTime() == null) return o1.getId() - o2.getId();
            if (o1.getStartTime() == null) return 1;
            if (o2.getStartTime() == null) return -1;
            if (o1.getStartTime().isAfter(o2.getStartTime())) return 1;
            if (o1.getStartTime().isBefore(o2.getStartTime())) return -1;
            if (o1.getStartTime().isEqual(o2.getStartTime())) return o1.getId() - o2.getId();
            return 0;
        });
    }

    @Override
    public String toString() {
        return id + "," +"EPIC"+  "," + title +  "," + status + "," + description+ ","
                + startTime + "," + duration + "," + endTime;
    }
}