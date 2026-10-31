package com.gdb.domain;

import com.gdb.exceptions.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Account implements IAccount, Serializable {
    private static final long serialVersionUID = 1L;

    private static final double MIN_BALANCE_SAVINGS = 500.0;
    private static final double MIN_BALANCE_CURRENT = 1000.0;
    private static final int MIN_AGE = 18;
    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;

    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin;

    protected int tenureYears = 0;
    private double dailyTransferTotal = 0.0;
    private LocalDateTime lastTransferDate = LocalDateTime.now();

    public Account(int accountNumber, String name, int age, double initialBalance, String accountType, int tenureYears)
            throws IllegalArgumentException {
        if (age < MIN_AGE) {
            throw new IllegalArgumentException("Customer must be at least 18 years old. Provided: " + age);
        }
        if (!"Savings".equalsIgnoreCase(accountType) && !"Current".equalsIgnoreCase(accountType) && !"FIXED_DEPOSIT".equalsIgnoreCase(accountType) && !"SALARY".equalsIgnoreCase(accountType)) {
            throw new IllegalArgumentException("Account type must be 'Savings' or 'Current'. Provided: " + accountType);
        }
        double minBalance = "Savings".equalsIgnoreCase(accountType) ? MIN_BALANCE_SAVINGS : ("Current".equalsIgnoreCase(accountType) ? MIN_BALANCE_CURRENT : 0.0);
        if (initialBalance < minBalance) {
            throw new IllegalArgumentException(accountType + " account requires minimum balance of \u20B9" + minBalance + ". Provided: \u20B9" + initialBalance);
        }

        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = null;
        this.tenureYears = tenureYears;
        this.dailyTransferTotal = 0.0;
        this.lastTransferDate = LocalDateTime.now();
    }

    public Account(int accountNumber, String name, int age, double initialBalance, String accountType)
            throws IllegalArgumentException {
        this(accountNumber, name, age, initialBalance, accountType, 0);
    }

    public void deposit(double amount) throws InvalidAmountException, InactiveAccountException {
        deposit(amount, this.pin);
    }

    public void deposit(double amount, Integer pin) throws InvalidAmountException, InactiveAccountException {
        validateActive();
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive. Provided: \u20B9" + amount);
        }
        this.balance += amount;
    }

    public Transaction depositWithTransaction(double amount) throws InvalidAmountException, InactiveAccountException {
        deposit(amount);
        return new Transaction(
            Transaction.generateId(),
            LocalDateTime.now(),
            this.accountNumber,
            TransactionType.DEPOSIT,
            amount,
            this.balance,
            "SUCCESS",
            "Deposit of Rs. " + amount,
            0,
            0
        );
    }

    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException,
            MinimumBalanceViolationException, InactiveAccountException, InvalidPinException, AccountException {
        if (getPin() == null) {
            throw new InvalidPinException("PIN not set for this account");
        }
        withdraw(amount, getPin());
    }

    public void withdraw(double amount, int pin) throws InvalidAmountException, InsufficientBalanceException,
            MinimumBalanceViolationException, InactiveAccountException, InvalidPinException, AccountException {
        validateActive();
        if (getPin() == null) {
            throw new InvalidPinException("PIN not set for this account");
        }
        if (!verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Withdraw amount must be positive. Provided: \u20B9" + amount);
        }
        processDebit(amount);
        this.balance -= amount;
    }

    public Transaction withdrawWithTransaction(double amount, int pin) throws InvalidAmountException,
            InsufficientBalanceException, MinimumBalanceViolationException, InactiveAccountException,
            InvalidPinException, AccountException {
        withdraw(amount, pin);
        return new Transaction(
            Transaction.generateId(),
            LocalDateTime.now(),
            this.accountNumber,
            TransactionType.WITHDRAW,
            amount,
            this.balance,
            "SUCCESS",
            "Withdrawal of Rs. " + amount,
            0,
            0
        );
    }

    protected void processDebit(double amount)
            throws MinimumBalanceViolationException, InsufficientBalanceException, AccountException {
        if (amount > this.balance) {
            throw new InsufficientBalanceException("Insufficient balance. Available: \u20B9" + this.balance + ", Requested: \u20B9" + amount);
        }
        if ((this.balance - amount) < getMinimumBalance()) {
            throw new MinimumBalanceViolationException("Cannot withdraw. Minimum balance of \u20B9" + getMinimumBalance() + " required. Available after withdrawal: \u20B9" + (this.balance - amount));
        }
    }

    public void closeAccount() throws IllegalStateException {
        if ("Inactive".equals(this.status)) {
            throw new IllegalStateException("Account is already closed");
        }
        this.status = "Inactive";
    }

    public void reopenAccount() throws IllegalStateException {
        if ("Active".equals(this.status)) {
            throw new IllegalStateException("Account is already active");
        }
        this.status = "Active";
    }

    public void setPin(int pin) throws IllegalArgumentException {
        if (pin < MIN_PIN || pin > MAX_PIN) {
            throw new IllegalArgumentException("PIN must be a 4-digit number (1000-9999). Provided: " + pin);
        }
        this.pin = pin;
    }

    public boolean verifyPin(int pin) {
        return this.pin != null && this.pin == pin;
    }

    public boolean hasPin() {
        return this.pin != null;
    }

    double getMinimumBalance() {
        if ("Savings".equalsIgnoreCase(this.accountType)) {
            return MIN_BALANCE_SAVINGS;
        } else if ("Current".equalsIgnoreCase(this.accountType)) {
            return MIN_BALANCE_CURRENT;
        }
        return 0.0;
    }

    public void validateActive() throws InactiveAccountException {
        if (!"Active".equals(this.status)) {
            throw new InactiveAccountException("Account is inactive. Please reopen the account or contact support.");
        }
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getStatus() {
        return status;
    }

    public Integer getPin() {
        return pin;
    }

    public int getTenureYears() {
        return this.tenureYears;
    }

    public void setTenureYears(int tenureYears) {
        this.tenureYears = tenureYears;
    }

    public double getDailyTransferTotal() {
        return dailyTransferTotal;
    }

    public LocalDateTime getLastTransferDate() {
        return lastTransferDate;
    }

    public void resetDailyTransferIfNeeded() {
        if (lastTransferDate == null || !lastTransferDate.toLocalDate().equals(LocalDate.now())) {
            this.dailyTransferTotal = 0.0;
            this.lastTransferDate = LocalDateTime.now();
        }
    }

    public double getDailyTransferLimit() {
        return AccountRulesEngine.getInstance().getDailyTransferLimit(getAccountType(), getTenureYears());
    }

    public double getRemainingDailyTransferLimit() {
        resetDailyTransferIfNeeded();
        return Math.max(0.0, getDailyTransferLimit() - dailyTransferTotal);
    }

    public boolean canTransfer(double amount) {
        resetDailyTransferIfNeeded();
        return (dailyTransferTotal + amount) <= getDailyTransferLimit();
    }

    public void updateDailyTransferTotal(double amount) {
        resetDailyTransferIfNeeded();
        this.dailyTransferTotal += amount;
        this.lastTransferDate = LocalDateTime.now();
    }

    public boolean canWithdraw(double amount) {
        return amount > 0 && (this.balance - amount) >= getMinimumBalance();
    }

    @Override
    public String toString() {
        return "Account #" + accountNumber + " | " + name + " (" + age + " yrs, Tenure: " + getTenureYears() + " yrs) | " + accountType + " | Rs. " + balance + " | " + status;
    }
}
