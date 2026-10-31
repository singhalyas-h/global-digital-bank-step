package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class SalaryAccount extends AbstractAccount {
    private String employerName;
    private int inactiveMonths;

    public SalaryAccount(int accountNumber, String name, int age, double initialBalance, String accountType)
            throws IllegalArgumentException {
        super(accountNumber, name, age, initialBalance, "SALARY");
    }

    @Override
    protected void processDebit(double amount) throws MinimumBalanceViolationException, InsufficientBalanceException, AccountException {
        if(super.getBalance() < amount){
            throw new InsufficientBalanceException("Cannot withdraw. Not enough balance to withdraw.\n");
        }
    }

    @Override
    public boolean canWithdraw(double amount) {
        return amount > 0 && super.getBalance() >= amount;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    void setInactiveMonths(int inactiveMonths) {
        this.inactiveMonths = inactiveMonths;
    }

    public String getEmployerName() {
        return this.employerName;
    }

    public int getInactiveMonths(){
        return this.inactiveMonths;
    }
}
