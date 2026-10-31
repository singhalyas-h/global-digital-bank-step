package com.gdb.command;

import com.gdb.domain.Account;
import com.gdb.domain.Transaction;

public class WithdrawCommand implements TransactionCommand {
    private static final long serialVersionUID = 1L;

    private Account account;
    private double amount;
    private int pin;
    private Transaction transaction;

    public WithdrawCommand(Account account, double amount, int pin) {
        this.account = account;
        this.amount = amount;
        this.pin = pin;
    }

    @Override
    public void execute() throws Exception {
        this.transaction = account.withdrawWithTransaction(amount, pin);
    }

    @Override
    public Transaction getTransaction() {
        return this.transaction;
    }

    public Account getAccount() {
        return account;
    }

    public double getAmount() {
        return amount;
    }

    public int getPin() {
        return pin;
    }
}
