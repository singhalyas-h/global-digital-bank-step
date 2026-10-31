package com.gdb.service;

import com.gdb.command.*;
import com.gdb.domain.*;
import com.gdb.exceptions.*;
import com.gdb.logging.TransactionLogger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AccountService {
    private final Map<Integer, IAccount> accounts;
    private final TransactionLogger logger;
    private final TransferService transferService;
    private int nextAccountNumber = 1001;

    public AccountService(TransactionLogger logger, TransferService transferService) {
        this.accounts = new ConcurrentHashMap<>();
        this.logger = logger;
        this.transferService = transferService;
    }

    public AccountService(TransactionLogger logger) {
        this(logger, new TransferService());
    }

    public IAccount openAccount(String type, String name, int age, double initialBalance) {
        return openAccount(type, name, age, initialBalance, 0);
    }

    public IAccount openAccount(String type, String name, int age, double initialBalance, int tenureYears) {
        int accNo = nextAccountNumber++;
        IAccount account = AccountFactory.createAccount(accNo, name, age, initialBalance, type, tenureYears);
        accounts.put(accNo, account);
        return account;
    }

    public void closeAccount(int accountNumber, int pin) throws AccountException {
        IAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        if (!account.verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }
        if (account instanceof Account acc) {
            acc.closeAccount();
        }
    }

    public Transaction deposit(int accountNumber, double amount) throws Exception {
        IAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        DepositCommand cmd = new DepositCommand((Account) account, amount);
        cmd.execute();
        if (logger != null) {
            logger.log(cmd);
        }
        return cmd.getTransaction();
    }

    public Transaction withdraw(int accountNumber, double amount, int pin) throws Exception {
        IAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        WithdrawCommand cmd = new WithdrawCommand((Account) account, amount, pin);
        cmd.execute();
        if (logger != null) {
            logger.log(cmd);
        }
        return cmd.getTransaction();
    }

    public Transaction transfer(int fromAccNo, int toAccNo, double amount, int pin) throws Exception {
        IAccount fromAccount = accounts.get(fromAccNo);
        if (fromAccount == null) {
            throw new AccountException("Account not found: " + fromAccNo);
        }
        IAccount toAccount = accounts.get(toAccNo);
        if (toAccount == null) {
            throw new AccountException("Account not found: " + toAccNo);
        }
        TransferCommand cmd = new TransferCommand((Account) fromAccount, (Account) toAccount, amount, pin);
        cmd.execute();
        if (logger != null) {
            logger.log(cmd);
        }
        return cmd.getTransaction();
    }

    public IAccount getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public Collection<IAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public List<Transaction> getTransactionHistory() {
        if (logger == null) {
            return Collections.emptyList();
        }
        List<TransactionCommand> commands = logger.readAll();
        List<Transaction> transactions = new ArrayList<>();
        for (TransactionCommand cmd : commands) {
            if (cmd != null && cmd.getTransaction() != null) {
                transactions.add(cmd.getTransaction());
            }
        }
        return transactions;
    }

    public int getNextAccountNumber() {
        return nextAccountNumber;
    }
}
