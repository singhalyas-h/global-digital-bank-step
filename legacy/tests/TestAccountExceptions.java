package com.gdb.tests;

import com.gdb.domain.Account;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InvalidAmountException;

public class TestAccountExceptions {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("ACCOUNT TEST WITH EXCEPTIONS");
        System.out.println("============================================================");

        System.out.println("\n>>> Test 1: Valid Account Creation");
        Account acc1 = null;
        try {
            acc1 = new Account(1001, "John Doe", 25, 1000.0, "Savings");
            System.out.println("SUCCESS: " + acc1);
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println("\n>>> Test 2: Invalid Age (under 18)");
        try {
            new Account(1002, "Young User", 16, 500.0, "Savings");
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println("\n>>> Test 3: Invalid Account Type");
        try {
            new Account(1003, "Invalid User", 25, 500.0, "Invalid");
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println("\n>>> Test 4: Minimum Balance on Creation");
        System.out.println("Creating Savings account with \u20B9300");
        try {
            new Account(1004, "Bob Wilson", 25, 300.0, "Savings");
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println("\n>>> Test 5: Valid Deposit and Withdrawal");
        Account acc5 = null;
        try {
            acc5 = new Account(1005, "Alice Brown", 30, 1000.0, "Current");
            System.out.println("Account: " + acc5);
            acc5.setPin(1234);
            System.out.println("Setting PIN 1234: SUCCESS");
            acc5.deposit(500.0);
            System.out.println("Depositing \u20B9500.0: SUCCESS");
            System.out.println("Balance after deposit: \u20B9" + acc5.getBalance());
            acc5.withdraw(200.0, 1234);
            System.out.println("Withdrawing \u20B9200.0: SUCCESS");
            System.out.println("Balance after withdrawal: \u20B9" + acc5.getBalance());
            System.out.println(acc5);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println("\n>>> Test 6: Invalid Deposit (Negative Amount)");
        System.out.println("Attempting to deposit \u20B9-100.0");
        try {
            if (acc5 != null) {
                acc5.deposit(-100.0);
            }
        } catch (InvalidAmountException | InactiveAccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println("\n>>> Test 7: Insufficient Balance");
        Account acc6 = null;
        try {
            acc6 = new Account(1006, "Charlie Green", 35, 500.0, "Savings");
            acc6.setPin(1234);
            System.out.println("Account: " + acc6);
            System.out.println("Attempting to withdraw \u20B91000.0");
            acc6.withdraw(1000.0, 1234);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println("\n>>> Test 8: Minimum Balance Violation");
        Account acc7 = null;
        try {
            acc7 = new Account(1007, "Diana Prince", 28, 1000.0, "Savings");
            acc7.setPin(1234);
            System.out.println("Account: " + acc7);
            System.out.println("Attempting to withdraw \u20B9600.0");
            acc7.withdraw(600.0, 1234);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println("\n>>> Test 9: Inactive Account Operations");
        Account acc8 = null;
        try {
            acc8 = new Account(1008, "Eve Wilson", 32, 2000.0, "Current");
            System.out.println("Account: " + acc8);
            acc8.closeAccount();
            System.out.println("Closing account: SUCCESS");
            System.out.println("Attempting to deposit \u20B9100.0 on closed account");
            try {
                acc8.deposit(100.0);
            } catch (InvalidAmountException | InactiveAccountException e) {
                System.out.println("EXCEPTION: " + e.getMessage());
            }
            acc8.reopenAccount();
            System.out.println("Reopening account: SUCCESS");
            acc8.deposit(100.0);
            System.out.println("Depositing \u20B9100.0 after reopen: SUCCESS");
            System.out.println("Balance after deposit: \u20B9" + acc8.getBalance());
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println("\n>>> Test 10: PIN Verification");
        Account acc9 = null;
        try {
            acc9 = new Account(1009, "Frank Miller", 40, 1500.0, "Savings");
            System.out.println("Account: " + acc9);
            acc9.setPin(1234);
            System.out.println("Setting PIN 1234: SUCCESS");
            acc9.withdraw(200.0, 1234);
            System.out.println("Withdrawing \u20B9200.0 with correct PIN: SUCCESS");
            System.out.println("Balance: \u20B9" + acc9.getBalance());

            System.out.println("Attempting to withdraw \u20B9100.0 with incorrect PIN (9999)");
            try {
                acc9.withdraw(100.0, 9999);
            } catch (AccountException e) {
                System.out.println("EXCEPTION: " + e.getMessage());
            }

            System.out.println("Attempting to withdraw \u20B9100.0 without PIN set");
            try {
                if (acc1 != null) {
                    acc1.withdraw(100.0, 1234);
                }
            } catch (AccountException e) {
                System.out.println("EXCEPTION: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println("\n>>> Test 11: All Accounts Summary");
        if (acc1 != null) System.out.println(acc1);
        if (acc5 != null) System.out.println(acc5);
        if (acc6 != null) System.out.println(acc6);
        if (acc7 != null) System.out.println(acc7);
        if (acc8 != null) System.out.println(acc8);
        if (acc9 != null) System.out.println(acc9);

        System.out.println("\n============================================================");
        System.out.println("TEST COMPLETED!");
        System.out.println("============================================================");
    }
}
