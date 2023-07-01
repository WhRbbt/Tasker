package tasker;

import java.util.List;

public interface TaskerRepository {

    void addTask(Task task);

    void deleteTask(int taskId);

    List<Task> getAllTasks();
}
