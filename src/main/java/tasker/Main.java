package tasker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskerRepository taskerRepository = new AppTaskerRepository();
        Gson gsonConverter = new Gson();
        Operations operations = new Operations(scanner, taskerRepository, gsonConverter);

        operations.readJsonFile();
        System.out.println("Tasker");

        while (true) {
            System.out.println("Enter a command (help - list of commands): ");
            String command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "add" -> operations.addTask();
                case "del" -> operations.deleteTask();
                case "upd" -> operations.updateTask();
                case "show" -> operations.showAllTasks();
                case "search" -> operations.searchTasks();
                case "sort" -> operations.sortTasks();
                case "help" -> operations.displayHelp();
                case "exit" -> {
                    operations.writeChangesToJson(taskerRepository.getAllTasks());
                    System.out.println("Exit...");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid command");
            }
        }
    }
}

