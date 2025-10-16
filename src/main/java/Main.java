

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "transactions.csv";
    private static List<Transaction> transactions = new ArrayList<>();
    // Using a single static Scanner for the whole application
    private static final Scanner scanner = new Scanner(System.in);
    private static final Ledger ledger = new Ledger();

    public static void main(String[] args) {
        System.out.println("Starting Transaction Ledger Application...");
        loadTransactions();

        // Starting the home screen loop
        displayHomeMenu();

        // Final save before exiting
        saveTransactions();
        System.out.println("Application exited. All data saved to " + FILE_NAME);
        scanner.close();
    }

    private static void saveTransactions() {
    }

    private static void loadTransactions() {
    }

    //  Home Screen

    public static void displayHomeMenu() {
        String input;
        boolean running = true;

        while (running) {
            System.out.println("\n*** Home Screen ***");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Enter your choice: ");

            input = scanner.nextLine().toUpperCase();

            switch (input) {
                case "D":
                    addTransaction(false); // Deposit
                    break;
                case "P":
                    addTransaction(true);  // Payment (Debit)
                    break;
                case "L":
                    ledger.display(); // Call the Ledger screen
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addTransaction(boolean isPayment) {
        String type = isPayment ? "Payment (Debit)" : "Deposit";
        System.out.println("\n*** Add " + type + " ***");

    }
}








