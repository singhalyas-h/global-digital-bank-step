package com.gdb.command;

import com.gdb.domain.Account;
import com.gdb.domain.Transaction;
import com.gdb.service.TransferService;

public class TransferCommand implements TransactionCommand {
    private static final long serialVersionUID = 1L;

    private Account fromAccount;
    private Account toAccount;
    private double amount;
    private int pin;
    private Transaction transaction;

    public TransferCommand(Account fromAccount, Account toAccount, double amount, int pin) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.pin = pin;
    }

    @Override
    public void execute() throws Exception {
        this.transaction = TransferService.transferWithTransaction(fromAccount, toAccount, amount, pin);
    }

    @Override
    public Transaction getTransaction() {
        return this.transaction;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public int getPin() {
        return pin;
    }
}
