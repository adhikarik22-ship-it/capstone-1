
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "transactions.csv";
    private static final List<Transaction> transactions = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    //Ledger inside the Transaction class
    private static final Transaction.Ledger ledger = new Transaction.Ledger();

    public static void main(String[] args) {
        System.out.println("Starting Transaction Ledger Application...");
        loadTransactions();

        displayHomeMenu();

        saveTransactions();
        System.out.println("Application exited. All data saved to " + FILE_NAME);
        scanner.close();
    }

    //Home Screen

    public static void displayHomeMenu() {
        String input;
        while (true) { // Loop continues until 'X' (Exit) is chosen
            System.out.println("\n*** Home Screen ***");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Enter your choice: ");

            input = scanner.nextLine().toUpperCase();

            switch (input) {
                case "D":
                    addTransaction(false);
                    break;
                case "P":
                    addTransaction(true);
                    break;
                case "L":
                    // Call the nested Ledger class
                    ledger.display(transactions, scanner);
                    break;
                case "X":
                    return; // Exit the loop and main method
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addTransaction(boolean isPayment) {
        //transaction input logic
        String type = isPayment ? "Payment (Debit)" : "Deposit";
        System.out.println("\n*** Add " + type + " ***");

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Please enter a number.");
            return;
        }

        if (isPayment) {
            amount = -Math.abs(amount);
        }

        Transaction newTransaction = new Transaction(date, description, vendor, amount);
        transactions.add(newTransaction);
        saveTransactions();
        System.out.println(type + " added and saved successfully!");
    }

    //File I/O Methods

    private static void loadTransactions() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Transaction t = Transaction.fromCSVString(line);
                if (t != null) {
                    transactions.add(t);
                }
            }
            System.out.println("Loaded " + transactions.size() + " transactions.");
        } catch (FileNotFoundException e) {
            System.out.println("No transaction file found. Starting a new ledger.");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static void saveTransactions() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Transaction t : transactions) {
                bw.write(t.toCSVString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());

        }
    }
}



