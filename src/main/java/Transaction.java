
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Transaction {
    private final String date;
    private final String description;
    private final String vendor;
    private final double amount;

    public Transaction(String date, String description, String vendor, double amount) {
        this.date = date;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    //Accessors for Filtering/Reporting
    public String getDate() { return date; }
    public String getVendor() { return vendor; }
    public double getAmount() { return amount; }

    //CSV I/O Methods
    public String toCSVString() {
        return date + "|" + description + "|" + vendor + "|" + amount;
    }

    public static Transaction fromCSVString(String csvLine) {
        String[] parts = csvLine.split("\\|");
        if (parts.length == 4) {
            try {
                return new Transaction(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
            } catch (NumberFormatException e) {
                System.err.println("Error parsing amount in CSV: " + csvLine);
            }
        }
        return null;
    }

    // Display Method
    @Override
    public String toString() {
        return String.format("| %-10s | %-25s | %-15s | $%,10.2f |", date, description, vendor, amount);
    }

  // LEDGER CLASS Nested

    public static class Ledger {

        // This method accepts the list and scanner from Main.java
        public void display(List<Transaction> transactions, Scanner scanner) {
            String input;

            while (true) {
                System.out.println("\n*** Ledger Screen ***");
                System.out.println("A) All");
                System.out.println("D) Deposits");
                System.out.println("P) Payments");
                System.out.println("R) Reports");
                System.out.println("H) Home - go back to the home page");
                System.out.print("Enter your choice: ");

                input = scanner.nextLine().toUpperCase();

                switch (input) {
                    case "A":
                        displayTransactions(transactions, "All Transactions");
                        break;
                    case "D":
                        displayDeposits(transactions);
                        break;
                    case "P":
                        displayPayments(transactions);
                        break;
                    case "R":
                        runReports(transactions, scanner);
                        break;
                    case "H":
                        return; // Exit the ledger menu
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }

        private void runReports(List<Transaction> transactions, Scanner scanner) {
        }

        //Ledger Display Filters (D, P)

        private void displayDeposits(List<Transaction> transactions) {
            List<Transaction> deposits = transactions.stream()
                    .filter(t -> t.getAmount() > 0)
                    .collect(Collectors.toList());
            displayTransactions(deposits, "Deposits");
        }

        private void displayPayments(List<Transaction> transactions) {
            List<Transaction> payments = transactions.stream()
                    .filter(t -> t.getAmount() < 0)
                    .collect(Collectors.toList());
            displayTransactions(payments, "Payments (Debits)");
        }

        // Main Display

        private void displayTransactions(List<Transaction> list, String title) {
            System.out.println("\n--- " + title + " ---");

            List<Transaction> sortedList = new ArrayList<>(list);
            Collections.reverse(sortedList); // Newest entries first

            if (sortedList.isEmpty()) {
                System.out.println("No transactions found for this view.");
                return;
            }

            // Display Header
            System.out.println("+------------+---------------------------+-----------------+--------------+");
            System.out.println("|    Date    |        Description        |      Vendor     |    Amount    |");
            System.out.println("+------------+---------------------------+-----------------+--------------+");

            for (Transaction t : sortedList) {
                System.out.println(t.toString());
            }
            System.out.println("+------------+---------------------------+-----------------+--------------+");
        }

        
        }
    }

