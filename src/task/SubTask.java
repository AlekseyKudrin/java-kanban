package task;

import util.StatusTask;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String title, String description, StatusTask status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;

    }

    public SubTask(String title, String description, StatusTask status, int id, int epicId) {
        super(title, description, status, id);
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
        return id + "," +"SUBTASK"+  "," + title +  "," + status + "," + description + ","+ epicId;
    }
}