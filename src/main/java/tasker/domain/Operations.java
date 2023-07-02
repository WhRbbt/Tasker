package tasker.domain;

import tasker.domain.data.Gson;
import tasker.domain.data.SortType;
import tasker.domain.data.Task;
import tasker.domain.data.TaskStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Operations {

    private final Scanner scanner;
    private final TaskerRepository taskerRepository;
    private final Gson gsonConverter;
    private final DateTimeFormatter dateFormatter;

    public Operations(Scanner scanner, TaskerRepository taskerRepository, Gson gsonConverter) {
        this.scanner = scanner;
        this.taskerRepository = taskerRepository;
        this.gsonConverter = gsonConverter;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
        taskerRepository.addTask(createTaskFromUserInput());
        System.out.println("Task created");
    }

    private Task createTaskFromUserInput() {
        System.out.println("Enter the task name:");
        String name = scanner.nextLine();
        System.out.println("Enter description:");
        String description = scanner.nextLine();
        LocalDate deadline = null;
        while (deadline == null) {
            System.out.println("Enter the deadline (dd-MM-yyyy):");
            String deadlineStr = scanner.nextLine();
            try {
                deadline = LocalDate.parse(deadlineStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the deadline in the format dd-MM-yyyy.");
            }
        }
        System.out.println("Select the status ((1) - Waiting, (2) - In Progress, (3) - Completed):");
        String statusStr = scanner.nextLine();
        TaskStatus status = parseTaskStatus(statusStr);
        System.out.println("Enter tags (tag1, tag2):");
        String tagsStr = scanner.nextLine();
        List<String> tags = Arrays.asList(tagsStr.split("\\s*,\\s*"));

        return new Task(name, description, deadline, status, tags);
    }


    private TaskStatus parseTaskStatus(String statusStr) {
        int statusNumber;
        try {
            statusNumber = Integer.parseInt(statusStr);
        } catch (NumberFormatException e) {
            return TaskStatus.WAITING;
        }

        return switch (statusNumber) {
            case 1 -> TaskStatus.WAITING;
            case 2 -> TaskStatus.IN_PROGRESS;
            case 3 -> TaskStatus.COMPLETED;
            default -> null;
        };
    }

    public void updateTask() {
        System.out.println("Enter ID:");
        int taskId = scanner.nextInt();
        scanner.nextLine();

        int adjustedTaskId = taskId - 1;

        if (adjustedTaskId >= 0 && adjustedTaskId < taskerRepository.getAllTasks().size()) {
            Task currentTask = taskerRepository.getAllTasks().get(adjustedTaskId);
            System.out.println("Task details:");
            printTaskDetails(currentTask);

            taskerRepository.updateTask(adjustedTaskId, createTaskFromUserInput());
            System.out.println("Task updated");
        } else {
            System.out.println("Invalid ID");
        }
    }

    private void printTaskDetails(Task task) {
        System.out.println("Name: " + task.name() +
                "\nDescription: " + task.description() +
                "\nDeadline: " + task.deadline() +
                "\nStatus: " + task.status() +
                "\nTags: " + String.join(", ", task.tags()) + "\n");
    }

    private void printTaskDetailsWithId(Task task, int taskId) {
        System.out.println("ID: " + taskId +
                "\nName: " + task.name() +
                "\nDescription: " + task.description() +
                "\nDeadline: " + task.deadline() +
                "\nStatus: " + task.status() +
                "\nTags: " + String.join(", ", task.tags()) + "\n");
    }

    public void showAllTasks() {
        List<Task> tasks = taskerRepository.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found");
        } else {
            System.out.println("All tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                int taskId = i + 1;
                printTaskDetailsWithId(task, taskId);
            }
        }
    }

    public void deleteTask() {
        System.out.println("Enter ID:");
        int taskId = Integer.parseInt(scanner.nextLine());
        int adjustedTaskId = taskId - 1;
        if (adjustedTaskId >= 0 && adjustedTaskId < taskerRepository.getAllTasks().size()) {
            taskerRepository.deleteTask(adjustedTaskId);
            System.out.println("Task deleted");
        } else {
            System.out.println("Invalid ID");
        }
    }

    public void searchTasks() {
        System.out.println("Enter the search query:");
        String query = scanner.nextLine();
        List<Task> results = taskerRepository.searchTasks(query);
        if (results.isEmpty()) {
            System.out.println("No tasks found");
        } else {
            System.out.println("Tasks:");
            for (Task task : results) {
                printTaskDetails(task);
            }
        }
    }

    public void sortTasks() {
        System.out.println("Select the sort type:");
        System.out.println("1. By name\n2. By deadline\n3. By status");
        int choice = Integer.parseInt(scanner.nextLine());

        SortType sortType;
        switch (choice) {
            case 1 -> sortType = SortType.NAME;
            case 2 -> sortType = SortType.DATE;
            case 3 -> sortType = SortType.STATUS;
            default -> {
                System.out.println("Invalid choice. Sorting by name");
                sortType = SortType.NAME;
            }
        }

        List<Task> sortedTasks = taskerRepository.sortTasks(sortType);
        if (sortedTasks.isEmpty()) {
            System.out.println("No tasks found");
        } else {
            System.out.println("Sorted tasks:");
            for (Task task : sortedTasks) {
                printTaskDetails(task);
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


