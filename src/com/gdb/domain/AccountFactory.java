package com.gdb.domain;

public class AccountFactory {
    public static IAccount createAccount(int accountNumber, String name, int age, double initialBalance,
            String accountType, int tenureYears) {
        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        return switch (accountType.toUpperCase()) {
            case "SAVINGS" -> new SavingsAccount(accountNumber, name, age, initialBalance, "Savings", tenureYears);
            case "CURRENT" -> new CurrentAccount(accountNumber, name, age, initialBalance, "Current");
            case "SALARY" -> new SalaryAccount(accountNumber, name, age, initialBalance, "Salary");
            case "FIXED_DEPOSIT" -> new FixedDepositAccount(accountNumber, name, age, initialBalance, "FIXED_DEPOSIT");
            default -> throw new IllegalArgumentException("Unexpected value: " + accountType);
        };
    }

    public static IAccount createAccount(int accountNumber, String name, int age, double initialBalance,
            String accountType) {
        return createAccount(accountNumber, name, age, initialBalance, accountType, 0);
    }
}
