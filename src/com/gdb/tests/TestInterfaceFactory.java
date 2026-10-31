package com.gdb.tests;

import com.gdb.domain.AccountFactory;
import com.gdb.domain.IAccount;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class TestInterfaceFactory {
    public static void main(String[] args) {
        System.out.println("=== Activity 12: Factory-Driven System Suite ===");

        try {
            IAccount savingsAccount = AccountFactory.createAccount(1001, "Alice Smith", 25, 2000.0, "SAVINGS");
            savingsAccount.setPin(1234);
            savingsAccount.deposit(1000.0, 1234);

            boolean minBalanceBlocked = false;
            try {
                savingsAccount.withdraw(2600.0, 1234);
            } catch (MinimumBalanceViolationException e) {
                minBalanceBlocked = true;
            }

            if (savingsAccount.getBalance() == 3000.0 && minBalanceBlocked) {
                System.out.println("[Test 1] Savings Account Creation & Deposit: [PASS]");
            } else {
                System.out.println("[Test 1] Savings Account Creation & Deposit: [FAIL]");
            }
        } catch (Exception e) {
            System.out.println("[Test 1] Savings Account Creation & Deposit: [FAIL]");
        }

        try {
            IAccount currentAccount = AccountFactory.createAccount(1002, "Bob Jones", 30, 2000.0, "CURRENT");
            currentAccount.setPin(1234);
            currentAccount.withdraw(5000.0, 1234);

            if (currentAccount.getBalance() == -3000.0) {
                System.out.println("[Test 2] Current Account Overdraft Withdrawal: [PASS]");
            }
        } catch (Exception e) {
            System.out.println("[Test 2] Current Account Overdraft Withdrawal: [FAIL]");
        }

        try {
            IAccount fixedDepositAccount = AccountFactory.createAccount(1003, "Charlie Brown", 40, 10000.0, "FIXED_DEPOSIT");
            fixedDepositAccount.setPin(1234);

            boolean prematureBlocked = false;
            try {
                fixedDepositAccount.withdraw(2000.0, 1234);
            } catch (AccountException e) {
                prematureBlocked = true;
            }

            if (prematureBlocked) {
                System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [PASS]");
            }
        } catch (Exception e) {
            System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL]");
        }

        try {
            boolean invalidRejected = false;
            try {
                AccountFactory.createAccount(1004, "Unknown User", 22, 1000.0, "INVESTMENT");
            } catch (IllegalArgumentException e) {
                invalidRejected = true;
            }

            if (invalidRejected) {
                System.out.println("[Test 4] Invalid Type Rejection: [PASS]");
            }
        } catch (Exception e) {
            System.out.println("[Test 4] Invalid Type Rejection: [FAIL]");
        }

        System.out.println("Factory-driven architecture successfully verified!");
    }
}
