
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

    //Accessors for filtering and reporting
    public String getDate() { return date; }
    public String getVendor() { return vendor; }
    public double getAmount() { return amount; }

    // CSV I/O Methods
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

    //Display method
    @Override
    public String toString() {
        return String.format("| %-10s | %-25s | %-15s | $%,10.2f |", date, description, vendor, amount);
    }


    // Ledger

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

        // Ledger display filters

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

        // Main display

        private void displayTransactions(List<Transaction> list, String title) {
            System.out.println("\n--- " + title + " ---");

            List<Transaction> sortedList = new ArrayList<>(list);
            Collections.reverse(sortedList); // Newest entries first

            if (sortedList.isEmpty()) {
                System.out.println("No transactions found for this view.");
                return;
            }

            //Display Header
            System.out.println("+------------+---------------------------+-----------------+--------------+");
            System.out.println("|    Date    |        Description        |      Vendor     |    Amount    |");
            System.out.println("+------------+---------------------------+-----------------+--------------+");

            for (Transaction t : sortedList) {
                System.out.println(t.toString());
            }
            System.out.println("+------------+---------------------------+-----------------+--------------+");
        }

        //Reports

        private void runReports(List<Transaction> transactions, Scanner scanner) {
            String input;

            while (true) {
                System.out.println("\n*** Reports Screen ***");
                System.out.println("1) Month To Date");
                System.out.println("2) Previous Month");
                System.out.println("3) Year To Date");
                System.out.println("4) Previous Year");
                System.out.println("5) Search by Vendor");
                System.out.println("0) Back - go back to the Ledger page");
                System.out.print("Enter your choice: ");

                input = scanner.nextLine();
                List<Transaction> filteredList = new ArrayList<>();

                switch (input) {
                    case "1":
                        filteredList = filterByDate(transactions, Period.MONTH_TO_DATE);
                        displayTransactions(filteredList, "Report: Month To Date");
                        break;
                    case "2":
                        filteredList = filterByDate(transactions, Period.PREVIOUS_MONTH);
                        displayTransactions(filteredList, "Report: Previous Month");
                        break;
                    case "3":
                        filteredList = filterByDate(transactions, Period.YEAR_TO_DATE);
                        displayTransactions(filteredList, "Report: Year To Date");
                        break;
                    case "4":
                        filteredList = filterByDate(transactions, Period.PREVIOUS_YEAR);
                        displayTransactions(filteredList, "Report: Previous Year");
                        break;
                    case "5":
                        System.out.print("Enter vendor name to search: ");
                        String vendorSearch = scanner.nextLine().trim();
                        filteredList = filterByVendor(transactions, vendorSearch);
                        displayTransactions(filteredList, "Report: Search by Vendor - " + vendorSearch);
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }

        // Private helper methods for  filtering dates

        private enum Period { MONTH_TO_DATE, PREVIOUS_MONTH, YEAR_TO_DATE, PREVIOUS_YEAR }

        private List<Transaction> filterByDate(List<Transaction> transactions, Period period) {
            LocalDate today = LocalDate.now();
            LocalDate startDate;
            LocalDate endDate = today;

            //date calculation
            switch (period) {
                case MONTH_TO_DATE:
                    startDate = today.withDayOfMonth(1);
                    break;
                case PREVIOUS_MONTH:
                    LocalDate prevMonth = today.minusMonths(1);
                    startDate = prevMonth.withDayOfMonth(1);
                    endDate = prevMonth.with(TemporalAdjusters.lastDayOfMonth());
                    break;
                case YEAR_TO_DATE:
                    startDate = today.withDayOfYear(1);
                    break;
                case PREVIOUS_YEAR:
                    LocalDate prevYear = today.minusYears(1);
                    startDate = prevYear.withDayOfYear(1);
                    endDate = prevYear.with(TemporalAdjusters.lastDayOfYear());
                    break;
                default:
                    return new ArrayList<>();
            }

            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            final LocalDate finalStartDate = startDate;
            final LocalDate finalEndDate = endDate;

            return transactions.stream()
                    .filter(t -> {
                        try {
                            LocalDate transactionDate = LocalDate.parse(t.getDate(), formatter);
                            return !transactionDate.isBefore(finalStartDate) && !transactionDate.isAfter(finalEndDate);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        }

        private List<Transaction> filterByVendor(List<Transaction> transactions, String vendor) {
            return transactions.stream()
                    .filter(t -> t.getVendor().equalsIgnoreCase(vendor))
                    .collect(Collectors.toList());
        }
    }
}





