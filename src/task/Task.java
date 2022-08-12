package task;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected StatusTask status;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task(String title, String description, StatusTask status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, String description, StatusTask status, int id) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
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

    @Override
    public String toString() {
        return "Назавание:" + getTitle()
                + "\n Описание:" + getDescription()
                + "\n id=" + getId()
                + "\n Статус:" + getStatus();
    }
}
