package tasker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Operations operations = new Operations();

        System.out.println("Tasker");

        while (true) {
            System.out.println("Enter a command (help - list of commands): ");
            String command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "help" -> operations.displayHelp();
                case "exit" -> {
                    System.out.println("Exit...");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid command");
            }
        }
    }
}

