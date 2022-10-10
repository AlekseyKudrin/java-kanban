package task;

import util.StatusTask;
import util.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected StatusTask status;

    protected Duration duration;

    protected LocalDateTime startTime;

    protected LocalDateTime endTime;

    // конструктор Epic
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    //конструктор Task и subTask
    public Task(String title, String description, StatusTask status, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

   //конструктор для замены task и subTask
    public Task(String title, String description, StatusTask status, int id, LocalDateTime newTime, Duration newDuration) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = newTime;
        this.duration = newDuration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return id + "," +"TASK"+  "," + title +  "," + status + "," + description + ","
                + startTime + "," + duration + "," + endTime;
    }
}
