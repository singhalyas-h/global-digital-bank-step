package com.gdb.domain;

import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class CurrentAccount extends AbstractAccount {
    private double overDraftLimit = 25000;

    public CurrentAccount(int accountNumber, String name, int age, double initialBalance, String accountType)
            throws IllegalArgumentException {
        super(accountNumber, name, age, initialBalance, "Current");
    }

    @Override
    protected void processDebit(double amount) throws MinimumBalanceViolationException, InsufficientBalanceException {
        if(amount > (super.getBalance() + this.getOverDraftLimit())){
            throw new InsufficientBalanceException("Cannot withdraw. Withdrawal amount exceeds the balance and OverDraft Limit of "+this.getOverDraftLimit()+"\n");
        }
    }

    @Override
    public boolean canWithdraw(double amount) {
        return amount > 0 && amount <= (super.getBalance() + this.overDraftLimit);
    }

    public double getOverDraftLimit() {
        return this.overDraftLimit;
    }

    void setOverDraftLimit(double newLimit) {
        if (newLimit >= 0) {
            this.overDraftLimit = newLimit;
        } else {
            System.out.println("Overdraft limit cannot be negative.");
        }
    }
}
