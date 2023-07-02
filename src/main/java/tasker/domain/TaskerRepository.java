package tasker.domain;

import tasker.domain.data.SortType;
import tasker.domain.data.Task;

import java.util.List;

public interface TaskerRepository {

    void addTask(Task task);

    void updateTask(int taskId, Task updatedTask);

    void deleteTask(int taskId);

    List<Task> getAllTasks();

    List<Task> searchTasks(String query);

    List<Task> sortTasks(SortType sortType);

    void setAllTasks(List<Task> tasks);
}
