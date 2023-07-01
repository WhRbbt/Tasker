package tasker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Operations {

    private final Scanner scanner;
    private final TaskerRepository taskerRepository;
    private final Gson gsonConverter;

    public Operations(Scanner scanner, TaskerRepository taskerRepository, Gson gsonConverter) {
        this.scanner = scanner;
        this.taskerRepository = taskerRepository;
        this.gsonConverter = gsonConverter;
    }

    public void displayHelp() {
        System.out.println(" ");
        System.out.println("Available commands:");
        System.out.println("add - Add a task");
        System.out.println("del - Delete a task");
        System.out.println("upd - Update a task");
        System.out.println("show - Show all tasks");
        System.out.println("search - Search tasks");
        System.out.println("sort - Sort tasks");
        System.out.println("help - Show available commands");
        System.out.println("exit - Exit the program");
        System.out.println(" ");
    }

    public void addTask() {
        System.out.println("Enter task name:");
        String name = scanner.nextLine();
        System.out.println("Enter description:");
        String description = scanner.nextLine();
        System.out.println("Enter task deadline (yyyy-MM-dd):");
        String deadlineStr = scanner.nextLine();
        LocalDate deadline = LocalDate.parse(deadlineStr);
        System.out.println("Enter task status (waiting, in progress, completed):");
        String statusStr = scanner.nextLine();
        TaskStatus status = switch (statusStr.toLowerCase()) {
            case "waiting" -> TaskStatus.WAITING;
            case "in progress" -> TaskStatus.IN_PROGRESS;
            case "completed" -> TaskStatus.COMPLETED;
            default -> TaskStatus.WAITING;
        };
        System.out.println("Enter task tags:");
        String tagsStr = scanner.nextLine();
        List<String> tags = Arrays.asList(tagsStr.split("\\s*,\\s*"));

        Task newTask = new Task(name, description, deadline, status, tags);
        taskerRepository.addTask(newTask);
        System.out.println("Task added");
    }

    public void deleteTask() {
        System.out.println("Enter task ID:");
        int taskId = scanner.nextInt();
        scanner.nextLine();
        int adjustedTaskId = taskId - 1;
        if (adjustedTaskId >= 0 && adjustedTaskId < taskerRepository.getAllTasks().size()) {
            taskerRepository.deleteTask(adjustedTaskId);
            System.out.println("Task deleted");
        } else {
            System.out.println("Invalid ID.");
        }
    }

    public void showAllTasks() {
        List<Task> tasks = taskerRepository.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            System.out.println("All tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                int taskId = i + 1;
                System.out.println("ID: " + taskId);
                System.out.println("Name: " + task.name());
                System.out.println("Description: " + task.description());
                System.out.println("Deadline: " + task.deadline());
                System.out.println("Status: " + task.status());
                System.out.println("Tags: " + String.join(", ", task.tags()));
                System.out.println();
            }
        }
    }

    public void writeChangesToJson(List<Task> tasks) {
        gsonConverter.writeDataToFile(tasks);
        System.out.println("Changes written to JSON file");
    }

    public void readJsonFile() {
        List<Task> tasks = gsonConverter.readDataFromFile();
        System.out.println("Data read from JSON file");

        taskerRepository.setAllTasks(tasks);
        System.out.println("Tasks saved to repository");
    }
}


