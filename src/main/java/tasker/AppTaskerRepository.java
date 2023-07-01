package tasker;

import java.util.ArrayList;
import java.util.List;

public class AppTaskerRepository implements TaskerRepository {

    private List<Task> tasks;

    public AppTaskerRepository() {
        tasks = new ArrayList<>();
    }

    @Override
    public void addTask(Task task) {
        tasks.add(task);
    }

    @Override
    public void deleteTask(int taskId) {
        if (taskId >= 0 && taskId < tasks.size()) {
            tasks.remove(taskId);
        } else {
            System.out.println("Invalid ID");
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return tasks;
    }

    @Override
    public void setAllTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
