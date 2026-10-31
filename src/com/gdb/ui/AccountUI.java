package com.gdb.ui;

import com.gdb.domain.Account;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.exceptions.AccountException;
import com.gdb.service.AccountService;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class AccountUI {
    private final AccountService service;
    private final Scanner scanner;
    private final PrintStream out;

    public AccountUI(AccountService service) {
        this(service, System.in, System.out);
    }

    public AccountUI(AccountService service, InputStream in, PrintStream out) {
        this.service = service;
        this.scanner = new Scanner(in);
        this.out = out;
    }

    public void start() {
        boolean running = true;
        out.println("============================================================");
        out.println("          GLOBAL DIGITAL BANK - CONSOLE PORTAL              ");
        out.println("============================================================");

        while (running) {
            displayMenu();
            out.print("Enter your choice (1-8, 0 to Exit): ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String input = scanner.nextLine().trim();
            out.println();

            switch (input) {
                case "1" -> handleOpenAccount();
                case "2" -> handleDeposit();
                case "3" -> handleWithdraw();
                case "4" -> handleTransfer();
                case "5" -> handleViewAccount();
                case "6" -> handleListAccounts();
                case "7" -> handleTransactionHistory();
                case "8" -> handleCloseAccount();
                case "0" -> {
                    out.println("Thank you for using Global Digital Bank. Goodbye!");
                    running = false;
                }
                default -> out.println("Invalid choice. Please select an option between 0 and 8.");
            }
            out.println();
        }
    }

    public void displayMenu() {
        out.println("------------------------------------------------------------");
        out.println("                      MAIN MENU                             ");
        out.println("------------------------------------------------------------");
        out.println("  1. Open New Account");
        out.println("  2. Deposit Funds");
        out.println("  3. Withdraw Funds");
        out.println("  4. Transfer Funds");
        out.println("  5. View Account Details");
        out.println("  6. List All Accounts");
        out.println("  7. View Transaction History");
        out.println("  8. Close Account");
        out.println("  0. Exit Application");
        out.println("------------------------------------------------------------");
    }

    public void handleOpenAccount() {
        try {
            out.print("Enter Account Type (SAVINGS, CURRENT, SALARY, FIXED_DEPOSIT): ");
            String type = scanner.nextLine().trim().toUpperCase();

            out.print("Enter Customer Name: ");
            String name = scanner.nextLine().trim();

            out.print("Enter Customer Age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());

            out.print("Enter Initial Balance (Rs.): ");
            double balance = Double.parseDouble(scanner.nextLine().trim());

            out.print("Enter Tenure in Years (0 for standard): ");
            int tenure = Integer.parseInt(scanner.nextLine().trim());

            out.print("Set 4-digit Security PIN (1000-9999): ");
            int pin = Integer.parseInt(scanner.nextLine().trim());

            IAccount account = service.openAccount(type, name, age, balance, tenure);
            if (account instanceof Account acc) {
                acc.setPin(pin);
            }
            out.println("SUCCESS: Account created successfully!");
            out.println("  Details: " + account);
        } catch (NumberFormatException e) {
            out.println("ERROR: Invalid numeric input provided.");
        } catch (Exception e) {
            out.println("ERROR: " + e.getMessage());
        }
    }

    public void handleDeposit() {
        try {
            out.print("Enter Account Number: ");
            int accNo = Integer.parseInt(scanner.nextLine().trim());

            out.print("Enter Deposit Amount (Rs.): ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            Transaction txn = service.deposit(accNo, amount);
            out.println("SUCCESS: Deposit completed!");
            out.println("  " + txn);
        } catch (NumberFormatException e) {
            out.println("ERROR: Invalid numeric format.");
        } catch (Exception e) {
            out.println("ERROR: " + e.getMessage());
        }
    }

    public void handleWithdraw() {
        try {
            out.print("Enter Account Number: ");
            int accNo = Integer.parseInt(scanner.nextLine().trim());

            out.print("Enter Withdrawal Amount (Rs.): ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            out.print("Enter 4-digit PIN: ");
            int pin = Integer.parseInt(scanner.nextLine().trim());

            Transaction txn = service.withdraw(accNo, amount, pin);
            out.println("SUCCESS: Withdrawal completed!");
            out.println("  " + txn);
        } catch (NumberFormatException e) {
            out.println("ERROR: Invalid numeric format.");
        } catch (Exception e) {
            out.println("ERROR: " + e.getMessage());
        }
    }

    public void handleTransfer() {
        try {
            out.print("Enter Source Account Number: ");
            int fromAcc = Integer.parseInt(scanner.nextLine().trim());

            out.print("Enter Destination Account Number: ");
            int toAcc = Integer.parseInt(scanner.nextLine().trim());

            out.print("Enter Transfer Amount (Rs.): ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            out.print("Enter Source Account 4-digit PIN: ");
            int pin = Integer.parseInt(scanner.nextLine().trim());

            Transaction txn = service.transfer(fromAcc, toAcc, amount, pin);
            out.println("SUCCESS: Transfer completed!");
            out.println("  " + txn);
        } catch (NumberFormatException e) {
            out.println("ERROR: Invalid numeric format.");
        } catch (Exception e) {
            out.println("ERROR: " + e.getMessage());
        }
    }

    public void handleViewAccount() {
        try {
            out.print("Enter Account Number: ");
            int accNo = Integer.parseInt(scanner.nextLine().trim());

            IAccount account = service.getAccount(accNo);
            if (account == null) {
                out.println("ERROR: Account #" + accNo + " not found.");
            } else {
                out.println("Account Details:");
                out.println("  " + account);
            }
        } catch (NumberFormatException e) {
            out.println("ERROR: Invalid account number.");
        }
    }

    public void handleListAccounts() {
        Collection<IAccount> accounts = service.getAllAccounts();
        if (accounts.isEmpty()) {
            out.println("No accounts currently registered in the system.");
            return;
        }
        out.println("Registered Accounts (" + accounts.size() + " total):");
        for (IAccount acc : accounts) {
            out.println("  - " + acc);
        }
    }

    public void handleTransactionHistory() {
        List<Transaction> history = service.getTransactionHistory();
        if (history.isEmpty()) {
            out.println("No transactions recorded yet.");
            return;
        }
        out.println("Transaction History (" + history.size() + " records):");
        for (int i = 0; i < history.size(); i++) {
            out.println("  [" + (i + 1) + "] " + history.get(i));
        }
    }

    public void handleCloseAccount() {
        try {
            out.print("Enter Account Number to Close: ");
            int accNo = Integer.parseInt(scanner.nextLine().trim());

            out.print("Enter 4-digit PIN: ");
            int pin = Integer.parseInt(scanner.nextLine().trim());

            service.closeAccount(accNo, pin);
            out.println("SUCCESS: Account #" + accNo + " closed successfully.");
        } catch (NumberFormatException e) {
            out.println("ERROR: Invalid numeric format.");
        } catch (AccountException e) {
            out.println("ERROR: " + e.getMessage());
        }
    }

    public AccountService getService() {
        return service;
    }
}
