package com.gdb.tests;

import com.gdb.command.*;
import com.gdb.database.SimulatedDatabase;
import com.gdb.domain.Account;
import com.gdb.domain.AccountFactory;
import com.gdb.logging.*;

public class TestBridgeLogging {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 18 \u2014 BRIDGE PATTERN (FILE + DB)");
        System.out.println("============================================================");
        System.out.println();

        LogDestination fileDest = new FileLogDestination();
        fileDest.clear();

        SimulatedDatabase db = new SimulatedDatabase();
        LogDestination dbDest = new DatabaseLogDestination(db);
        dbDest.clear();

        LogDestination memDest = new MemoryLogDestination();
        memDest.clear();

        TransactionLogger logger = new TransactionLogger(fileDest);

        try {
            Account acc1 = (Account) AccountFactory.createAccount(1001, "Rajesh Sharma", 30, 15000.0, "SAVINGS", 0);
            acc1.setPin(1234);

            Account acc2 = (Account) AccountFactory.createAccount(1002, "Priya Patel", 28, 20000.0, "SAVINGS", 0);
            acc2.setPin(1234);

            System.out.println("[STEP 25] Logging to FILE destination...");
            DepositCommand cmd1 = new DepositCommand(acc1, 1000.0);
            cmd1.execute();
            logger.log(cmd1);

            WithdrawCommand cmd2 = new WithdrawCommand(acc1, 500.0, 1234);
            cmd2.execute();
            logger.log(cmd2);

            TransferCommand cmd3 = new TransferCommand(acc1, acc2, 1000.0, 1234);
            cmd3.execute();
            logger.log(cmd3);

            System.out.println("  FILE log count: " + logger.readAll().size());
            System.out.println();

            logger.setDestination(dbDest);
            System.out.println("[STEP 26] Switched to DATABASE destination...");
            DepositCommand cmd4 = new DepositCommand(acc1, 2000.0);
            cmd4.execute();
            logger.log(cmd4);

            WithdrawCommand cmd5 = new WithdrawCommand(acc1, 1000.0, 1234);
            cmd5.execute();
            logger.log(cmd5);

            TransferCommand cmd6 = new TransferCommand(acc1, acc2, 1500.0, 1234);
            cmd6.execute();
            logger.log(cmd6);

            System.out.println("  DATABASE log count: " + logger.readAll().size());
            System.out.println();

            logger.setDestination(memDest);
            System.out.println("[STEP 27] Switched to MEMORY destination...");
            DepositCommand cmd7 = new DepositCommand(acc1, 3000.0);
            cmd7.execute();
            logger.log(cmd7);

            WithdrawCommand cmd8 = new WithdrawCommand(acc1, 1500.0, 1234);
            cmd8.execute();
            logger.log(cmd8);

            TransferCommand cmd9 = new TransferCommand(acc1, acc2, 2000.0, 1234);
            cmd9.execute();
            logger.log(cmd9);

            System.out.println("  MEMORY log count: " + logger.readAll().size());
            System.out.println();

            System.out.println("[STEP 28] Verifying Data Isolation:");
            System.out.println("  FILE count: " + fileDest.readAll().size() + " [EXPECTED: 3]");
            System.out.println("  DATABASE count: " + dbDest.readAll().size() + " [EXPECTED: 3]");
            System.out.println("  MEMORY count: " + memDest.readAll().size() + " [EXPECTED: 3]");
            System.out.println();

            if (fileDest.readAll().size() == 3 && dbDest.readAll().size() == 3 && memDest.readAll().size() == 3) {
                System.out.println("[STEP 29] All Bridge Pattern log backends verified successfully!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
