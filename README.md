# Capstone-1

<img width="712" height="598" alt="Screenshot 2025-10-14 at 9 08 00 AM" src="https://github.com/user-attachments/assets/3ca42f45-89c3-4390-a56f-db16737ed8bf" />

# Home Screen ption
files created: Main,Transaction,Ledger,Home.
D - Deposit:
prompt user for description, vendor, and amount. Save positive transaction to transaction.csv.

P - Payment:
Prompt user for description, vendor, and amount. Save negative transaction to transaction.csv 

L - Ledger:
Navigating to the ledger screen for viewing and reporting

E - Exit:
Save all current file to csv and terminate the application


# Ledger screen option
A - All:
show all recorded transaction(sorted by newest first)

D - Deposits:
display transactions that only has positive amount

P - payment:
display transactions that only has negative amount

R - Reports:
Navigates to the Reports menu for date-based and vendor searches

H - Home:
returns to Homescreen



# files and what they should contain:
main.java: main method
homescreen menu logic
input handeling
core file I/O (loading and saving)

transaction.java: Data model.
Defines the structure of a single transaction object. 
Includes methods for converting to/from the CSV string format.

ledger.java: View/Controler
Manages the Ledger Screen menu. 
Contains all the filtering and reporting logic. (like: Deposits, Payments, Month To Date, Search by Vendor).


# Reports (The 'R' option)
The Reports menu provides pre-defined filters using the Java LocalDate API for accurate date range calculations:

Month To Date

Previous Month

Year To Date

Previous Year

Search by Vendor

Back (to Ledger Screen)









