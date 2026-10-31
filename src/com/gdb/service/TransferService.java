package com.gdb.service;

import com.gdb.domain.Account;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.domain.TransactionType;
import com.gdb.exceptions.*;
import java.time.LocalDateTime;

public class TransferService {

    public static void transfer(IAccount from, IAccount to, double amount, int pin) throws AccountException {
        if (from == null || to == null) {
            throw new AccountException("Source and destination accounts are required");
        }
        if (!"Active".equalsIgnoreCase(from.getStatus()) || !"Active".equalsIgnoreCase(to.getStatus())) {
            throw new InactiveAccountException("Both accounts must be active to transfer funds");
        }
        if (!from.verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }
        if (!from.canWithdraw(amount)) {
            throw new InsufficientBalanceException("Insufficient balance for transfer of Rs. " + amount);
        }
        if (from instanceof Account sourceAccount) {
            sourceAccount.resetDailyTransferIfNeeded();
            if (!sourceAccount.canTransfer(amount)) {
                double remaining = sourceAccount.getRemainingDailyTransferLimit();
                throw new AccountException("Daily transfer limit exceeded. Remaining today: Rs. " + remaining);
            }
        }
        from.withdraw(amount, pin);
        to.deposit(amount);
        if (from instanceof Account sourceAccount) {
            sourceAccount.updateDailyTransferTotal(amount);
        }
    }

    public static Transaction transferWithTransaction(IAccount from, IAccount to, double amount, int pin) throws AccountException {
        transfer(from, to, amount, pin);
        return new Transaction(
            Transaction.generateId(),
            LocalDateTime.now(),
            from.getAccountNumber(),
            TransactionType.TRANSFER,
            amount,
            from.getBalance(),
            "SUCCESS",
            "Transfer of Rs. " + amount + " to Account #" + to.getAccountNumber(),
            from.getAccountNumber(),
            to.getAccountNumber()
        );
    }
}
