package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class FixedDepositAccount extends AbstractAccount {
    private int tenureMonths = 12;
    private double interestRate = 6.5;

    public FixedDepositAccount(int accountNumber, String name, int age, double initialBalance, String accountType)
            throws IllegalArgumentException {
        super(accountNumber, name, age, initialBalance, "FIXED_DEPOSIT");
    }

    @Override
    protected void processDebit(double amount) throws AccountException, MinimumBalanceViolationException, InsufficientBalanceException {
        throw new AccountException("Pre-mature account cannot withdraw\n");
    }

    @Override
    public boolean canWithdraw(double amount) {
        return false;
    }

    double calculateMaturityAmount() {
        double principal = this.getBalance();
        double rate = this.interestRate / 100;
        double time = (double) this.tenureMonths / 12;
        return principal * Math.pow((1 + rate), time);

    }
    public double getTenureMonths() {
        return this.tenureMonths;
    }

    public double getInterestRate() {
        return this.interestRate;
    }
}
