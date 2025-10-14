public class Transaction {
    private final String date;
    private final   String description;
    private final String vendor;
    private final double amount;

    public Transaction(String date, String description, String vendor, double amount){
        this.date = date;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public static Transaction fromCSVString(String line) {
        return null;
    }

    // Filtering
    public String getDate() {return date;}
    public String getDescription() {return description;}
    public String getVendor() {return vendor;}
    public double getAmount() {return amount;}

    //CSV I/O method
    public String toCSVString() {
        return date + "|" + description + "|" + vendor + "|" + amount;}

    }


