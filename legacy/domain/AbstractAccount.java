package com.gdb.domain;

public abstract class AbstractAccount extends Account {
    public AbstractAccount(int accountNumber, String name, int age, double initialBalance, String accountType, int tenureYears)
            throws IllegalArgumentException {
        super(accountNumber, name, age, initialBalance, accountType, tenureYears);
    }

    public AbstractAccount(int accountNumber, String name, int age, double initialBalance, String accountType)
            throws IllegalArgumentException {
        super(accountNumber, name, age, initialBalance, accountType, 0);
    }
}
