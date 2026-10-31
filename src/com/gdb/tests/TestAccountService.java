package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;
import com.gdb.logging.*;
import com.gdb.service.AccountService;
import java.util.List;

public class TestAccountService {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 19 \u2014 ACCOUNT SERVICE DEMO");
        System.out.println("============================================================");
        System.out.println();

        // STEP 12
        LogDestination memDest = new MemoryLogDestination();
        TransactionLogger logger = new TransactionLogger(memDest);
        AccountService service = new AccountService(logger);

        try {
            // STEP 13
            IAccount acc1 = service.openAccount("SAVINGS", "John Doe", 25, 15000.0);
            ((Account) acc1).setPin(1234);
            System.out.println("[STEP 13] Opened: " + acc1);

            IAccount acc2 = service.openAccount("SAVINGS", "Jane Smith", 30, 10000.0);
            ((Account) acc2).setPin(1234);
            System.out.println("[STEP 13] Opened: " + acc2);
            System.out.println();

            // STEP 14
            Transaction depTxn = service.deposit(1001, 5000.0);
            System.out.println("[STEP 14] Deposit: " + depTxn);

            // STEP 15
            Transaction withTxn = service.withdraw(1001, 2000.0, 1234);
            System.out.println("[STEP 15] Withdrawal: " + withTxn);

            // STEP 16
            Transaction trfTxn = service.transfer(1001, 1002, 1000.0, 1234);
            System.out.println("[STEP 16] Transfer: " + trfTxn);
            System.out.println();

            // STEP 17
            System.out.println("[STEP 17] Final Balances:");
            System.out.println("  John (Account #1001): Rs. " + service.getAccount(1001).getBalance());
            System.out.println("  Jane (Account #1002): Rs. " + service.getAccount(1002).getBalance());
            System.out.println();

            // STEP 18
            List<Transaction> history = service.getTransactionHistory();
            System.out.println("[STEP 18] Transaction History (" + history.size() + " records):");
            for (int i = 0; i < history.size(); i++) {
                System.out.println("  [" + (i + 1) + "] " + history.get(i));
            }
            System.out.println();

            // STEP 19
            System.out.println("[STEP 19] Error Handling Checks:");
            try {
                service.deposit(9999, 1000.0);
            } catch (AccountException e) {
                System.out.println("  Deposit to missing account caught: " + e.getMessage() + " [PASS]");
            }
            try {
                service.withdraw(1001, 500.0, 9999);
            } catch (AccountException e) {
                System.out.println("  Withdrawal with wrong PIN caught: " + e.getMessage() + " [PASS]");
            }
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
