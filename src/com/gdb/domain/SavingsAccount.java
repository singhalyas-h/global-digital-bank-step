package com.gdb.domain;

import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InvalidAmountException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class SavingsAccount extends AbstractAccount {
    private int tenureYears;
    private double minBalance;
    private double interestRate;

    public SavingsAccount(int accountNumber, String name, int age, double initialBalance, String accountType,
            int tenureYears) throws IllegalArgumentException {
        super(accountNumber, name, age, initialBalance, "Savings", tenureYears);
        this.tenureYears = tenureYears;
        this.minBalance = AccountRulesEngine.getSavingsMinBalance(tenureYears);
        this.interestRate = AccountRulesEngine.getSavingsInterestRate(tenureYears);
    }

    public SavingsAccount(int accountNumber, String name, int age, double initialBalance, String accountType)
            throws IllegalArgumentException {
        this(accountNumber, name, age, initialBalance, accountType, 0);
    }

    @Override
    protected void processDebit(double amount) throws MinimumBalanceViolationException {
        if ((super.getBalance() - amount) < this.minBalance) {
            throw new MinimumBalanceViolationException(
                    "Cannot withdraw. Minimum balance of \u20B9" + this.minBalance
                            + " required. Available after withdrawal: \u20B9" + (this.getBalance() - amount));
        }
    }

    @Override
    public boolean canWithdraw(double amount) {
        return amount > 0 && (getBalance() - amount) >= this.minBalance;
    }

    public void applyInterest() {
        double interest = this.getBalance() * (this.interestRate / 100);
        try {
            if (this.getBalance() < minBalance) {
                throw new InvalidAmountException("Balance is below the minimum required for interest application.");
            }
            this.deposit(interest, super.getPin());
        } catch (InactiveAccountException | InvalidAmountException e) {
            System.out.println("Cannot apply interest: " + e.getMessage());
        }
    }

    void getminBalance() {
        System.out.println("Minimum Balance for the Savings Account is: " + this.minBalance);
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public double getMinBalance() {
        return this.minBalance;
    }

    @Override
    public int getTenureYears() {
        return this.tenureYears;
    }
}
