package task;

import util.StatusTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String title, String description, StatusTask status, int epicId, LocalDateTime startTime, Duration duration) {
        super(title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, StatusTask status, int id, int epicId, LocalDateTime newTime, Duration newDuration) {
        super(title, description, status, id, newTime, newDuration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return id + "," +"SUBTASK"+  "," + title +  "," + status + "," + description + ","+ epicId + ","
                + startTime + "," + duration + "," + endTime;
    }
}