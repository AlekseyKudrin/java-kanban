package manager;

public abstract class Managers implements TaskManager {

    public static InMemoryTaskManager getDefault(){
        return new InMemoryTaskManager();
    }

}
