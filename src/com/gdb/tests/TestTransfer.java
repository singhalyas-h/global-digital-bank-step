package com.gdb.tests;

import com.gdb.domain.Account;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.AccountRulesEngine;
import com.gdb.domain.IAccount;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.service.TransferService;

public class TestTransfer {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 15 \u2014 TRANSFER WITH DAILY LIMITS");
        System.out.println("============================================================");
        System.out.println();

        AccountRulesEngine.getInstance().loadAllRules();
        System.out.println();

        IAccount iAcc1 = AccountFactory.createAccount(1001, "Rajesh Sharma", 30, 100000.0, "SAVINGS", 0);
        IAccount iAcc2 = AccountFactory.createAccount(1002, "Priya Patel", 28, 20000.0, "SAVINGS", 0);
        Account acc1 = (Account) iAcc1;
        Account acc2 = (Account) iAcc2;
        acc1.setPin(1234);
        acc2.setPin(1234);

        System.out.println("[STEP 9] " + formatAccount(acc1));
        System.out.println("[STEP 9] " + formatAccount(acc2));
        System.out.println();

        try {
            TransferService.transfer(acc1, acc2, 5000.0, 1234);
            System.out.println("[STEP 10] Transfer Rs. 5,000: SUCCESS | acc1 = Rs. " + acc1.getBalance() + " | acc2 = Rs. " + acc2.getBalance());
        } catch (Exception e) {
            System.out.println("[STEP 10] Transfer failed: " + e.getMessage());
        }

        try {
            TransferService.transfer(acc1, acc2, 100000.0, 1234);
        } catch (InsufficientBalanceException e) {
            System.out.println("[STEP 11] Caught InsufficientBalanceException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[STEP 11] Caught Exception: " + e.getMessage());
        }

        System.out.println("[STEP 12] Daily limit for acc1: Rs. " + acc1.getDailyTransferLimit());
        int transferCount = 1;
        while (true) {
            try {
                TransferService.transfer(acc1, acc2, 20000.0, 1234);
                System.out.println("  Transfer #" + transferCount + " of Rs. 20,000: SUCCESS | used today = Rs. " + acc1.getDailyTransferTotal());
                transferCount++;
            } catch (AccountException e) {
                System.out.println("[STEP 12] Caught AccountException: " + e.getMessage());
                break;
            }
        }

        System.out.println("[STEP 13] Used today: Rs. " + acc1.getDailyTransferTotal() + " | Remaining: Rs. " + acc1.getRemainingDailyTransferLimit());
    }

    private static String formatAccount(Account acc) {
        return "Account #" + acc.getAccountNumber() + " | " + acc.getName() + " (" + acc.getAge() + " yrs, Tenure: " + acc.getTenureYears() + " yrs) | " + acc.getAccountType() + " | Rs. " + acc.getBalance() + " | " + acc.getStatus();
    }
}
