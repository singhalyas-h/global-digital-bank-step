package com.gdb.domain;

import com.gdb.exceptions.*;

public interface IAccount {
    void deposit(double amount) throws InvalidAmountException, InactiveAccountException;
    void deposit(double amount, Integer pin) throws InvalidAmountException, InactiveAccountException;
    void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException, MinimumBalanceViolationException, InactiveAccountException, InvalidPinException, AccountException;
    void withdraw(double amount, int pin) throws InvalidAmountException, InsufficientBalanceException, MinimumBalanceViolationException, InactiveAccountException, InvalidPinException, AccountException;
    Transaction depositWithTransaction(double amount) throws InvalidAmountException, InactiveAccountException;
    Transaction withdrawWithTransaction(double amount, int pin) throws InvalidAmountException, InsufficientBalanceException, MinimumBalanceViolationException, InactiveAccountException, InvalidPinException, AccountException;
    double getBalance();
    int getAccountNumber();
    String getName();
    int getAge();
    String getAccountType();
    String getStatus();
    void setPin(int pin) throws IllegalArgumentException;
    boolean verifyPin(int pin);
    boolean hasPin();
    void closeAccount() throws IllegalStateException;
    void reopenAccount() throws IllegalStateException;
    boolean canWithdraw(double amount);
    int getTenureYears();
}
