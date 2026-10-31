package com.gdb.command;

import com.gdb.domain.Account;
import com.gdb.domain.Transaction;

public class DepositCommand implements TransactionCommand {
    private static final long serialVersionUID = 1L;

    private Account account;
    private double amount;
    private Transaction transaction;

    public DepositCommand(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() throws Exception {
        this.transaction = account.depositWithTransaction(amount);
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
}
