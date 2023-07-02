package tasker;

import java.util.ArrayList;
import java.util.Comparator;
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
    public void updateTask(int taskId, Task updatedTask) {
        if (taskId >= 0 && taskId < tasks.size()) {
            tasks.set(taskId, updatedTask);
        } else {
            System.out.println("Invalid ID");
        }
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
    public List<Task> searchTasks(String query) {
        List<Task> results = new ArrayList<>();
        for (Task task : tasks) {
            if (task.name().toLowerCase().contains(query.toLowerCase()) ||
                    task.tags().stream().anyMatch(tag -> tag.toLowerCase().contains(query.toLowerCase()))) {
                results.add(task);
            }
        }
        return results;
    }

    @Override
    public List<Task> sortTasks(SortType sortType) {
        List<Task> sortedTasks = new ArrayList<>(tasks);
        switch (sortType) {
            case NAME -> sortedTasks.sort((t1, t2) -> t1.name().compareToIgnoreCase(t2.name()));
            case DATE -> sortedTasks.sort(Comparator.comparing(Task::deadline));
            case STATUS -> sortedTasks.sort(Comparator.comparing(Task::status));
        }
        return sortedTasks;
    }

    @Override
    public void setAllTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
