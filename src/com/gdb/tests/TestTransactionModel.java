package com.gdb.tests;

import com.gdb.domain.Account;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.AccountRulesEngine;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.service.TransferService;

public class TestTransactionModel {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 16 \u2014 TRANSACTION MODEL TEST");
        System.out.println("============================================================");
        System.out.println();

        System.out.println("📂 Loading account rules from properties files...");
        System.out.println("--------------------------------------------------");
        System.out.println("✅ Loaded rules for: SAVINGS (" + AccountRulesEngine.getInstance().getSavingsLoader().getTenureBucketCount() + " tenure buckets)");
        System.out.println("✅ Loaded rules for: CURRENT (" + AccountRulesEngine.getInstance().getCurrentLoader().getTenureBucketCount() + " tenure buckets)");
        System.out.println("✅ Loaded rules for: FIXEDDEPOSIT (" + AccountRulesEngine.getInstance().getFdLoader().getTenureBucketCount() + " tenure buckets)");
        System.out.println("✅ Loaded rules for: SALARY (" + AccountRulesEngine.getInstance().getSalaryLoader().getTenureBucketCount() + " tenure buckets)");
        System.out.println("--------------------------------------------------");
        System.out.println("✅ All rules loaded successfully!");
        System.out.println();

        try {
            IAccount iAcc1 = AccountFactory.createAccount(1001, "Rajesh Sharma", 30, 50000.0, "SAVINGS", 0);
            Account acc1 = (Account) iAcc1;
            acc1.setPin(1234);

            Transaction depTxn = acc1.depositWithTransaction(5000.0);
            System.out.println("[STEP 10] Deposit Transaction: " + depTxn);

            Transaction withTxn = acc1.withdrawWithTransaction(2000.0, 1234);
            System.out.println("[STEP 11] Withdrawal Transaction: " + withTxn);

            IAccount iAcc2 = AccountFactory.createAccount(1002, "Priya Patel", 28, 20000.0, "SAVINGS", 0);
            Account acc2 = (Account) iAcc2;
            acc2.setPin(1234);

            Transaction trfTxn = TransferService.transferWithTransaction(acc1, acc2, 1000.0, 1234);
            System.out.println("[STEP 12] Transfer Transaction: " + trfTxn);

            acc1.deposit(1000.0);
            System.out.println("[STEP 13] Legacy Deposit +1000: " + acc1);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
