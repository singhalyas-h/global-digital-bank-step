package com.gdb.tests;

import com.gdb.command.*;
import com.gdb.domain.Account;
import com.gdb.domain.AccountFactory;
import com.gdb.logging.TransactionLog;
import java.util.List;

public class TestCommandLogging {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 17 \u2014 COMMAND PATTERN + FILE LOGGING");
        System.out.println("============================================================");
        System.out.println();

        TransactionLog log = new TransactionLog();
        log.clear();

        try {
            Account acc1 = (Account) AccountFactory.createAccount(1001, "Rajesh Sharma", 30, 15000.0, "SAVINGS", 0);
            acc1.setPin(1234);

            Account acc2 = (Account) AccountFactory.createAccount(1002, "Priya Patel", 28, 20000.0, "SAVINGS", 0);
            acc2.setPin(1234);

            // STEP 11
            DepositCommand depCmd = new DepositCommand(acc1, 5000.0);
            depCmd.execute();
            log.log(depCmd);
            System.out.println("[STEP 11] Logged: " + depCmd.getTransaction());

            // STEP 12
            WithdrawCommand withCmd = new WithdrawCommand(acc1, 2000.0, 1234);
            withCmd.execute();
            log.log(withCmd);
            System.out.println("[STEP 12] Logged: " + withCmd.getTransaction());

            // STEP 13
            TransferCommand trfCmd = new TransferCommand(acc1, acc2, 3000.0, 1234);
            trfCmd.execute();
            log.log(trfCmd);
            System.out.println("[STEP 13] Logged: " + trfCmd.getTransaction());

            // STEP 14
            List<TransactionCommand> history = log.readAll();
            System.out.println();
            System.out.println("[STEP 14] Read " + history.size() + " commands from transaction log:");
            for (int i = 0; i < history.size(); i++) {
                System.out.println("  [" + (i + 1) + "] " + history.get(i).getTransaction());
            }

            // STEP 15
            TransactionLog freshLog = new TransactionLog();
            List<TransactionCommand> persisted = freshLog.readAll();
            if (persisted.size() == 3) {
                System.out.println();
                System.out.println("[STEP 15] Fresh reader verified 3 persisted commands [PASS]");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
