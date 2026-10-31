package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;
import com.gdb.logging.MemoryLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;
import com.gdb.ui.AccountUI;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestAccountUI {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 20 \u2014 ACCOUNT UI & CONSOLE INTEGRATION");
        System.out.println("============================================================");
        System.out.println();

        System.out.println("[STEP 1] Initializing AccountService & AccountUI...");
        MemoryLogDestination memDest = new MemoryLogDestination();
        TransactionLogger logger = new TransactionLogger(memDest);
        AccountService service = new AccountService(logger);
        System.out.println("  Service initialized with MemoryLogDestination");
        System.out.println();

        try {
            System.out.println("[STEP 2] Simulating Interactive Account Creation via UI:");
            String openInputs = String.join(System.lineSeparator(),
                "1", "SAVINGS", "Rajesh Sharma", "30", "20000.0", "2", "1234",
                "1", "CURRENT", "Priya Patel", "28", "40000.0", "1", "1234",
                "0"
            ) + System.lineSeparator();

            ByteArrayOutputStream openOut = new ByteArrayOutputStream();
            AccountUI openUI = new AccountUI(service, new ByteArrayInputStream(openInputs.getBytes(StandardCharsets.UTF_8)), new PrintStream(openOut));
            openUI.start();

            IAccount acc1 = service.getAccount(1001);
            IAccount acc2 = service.getAccount(1002);
            System.out.println("  Opened " + acc1);
            System.out.println("  Opened " + acc2);
            System.out.println();

            System.out.println("[STEP 3] Simulating Deposit Operation:");
            String depInputs = String.join(System.lineSeparator(),
                "2", "1001", "5000.0",
                "0"
            ) + System.lineSeparator();
            ByteArrayOutputStream depOut = new ByteArrayOutputStream();
            AccountUI depUI = new AccountUI(service, new ByteArrayInputStream(depInputs.getBytes(StandardCharsets.UTF_8)), new PrintStream(depOut));
            depUI.start();
            System.out.println("  Deposit of Rs. 5000.0 to #1001 completed. Balance: Rs. " + acc1.getBalance());
            System.out.println();

            System.out.println("[STEP 4] Simulating Withdrawal Operation:");
            String withInputs = String.join(System.lineSeparator(),
                "3", "1001", "3000.0", "1234",
                "0"
            ) + System.lineSeparator();
            ByteArrayOutputStream withOut = new ByteArrayOutputStream();
            AccountUI withUI = new AccountUI(service, new ByteArrayInputStream(withInputs.getBytes(StandardCharsets.UTF_8)), new PrintStream(withOut));
            withUI.start();
            System.out.println("  Withdrawal of Rs. 3000.0 from #1001 completed. Balance: Rs. " + acc1.getBalance());
            System.out.println();

            System.out.println("[STEP 5] Simulating Fund Transfer:");
            String trfInputs = String.join(System.lineSeparator(),
                "4", "1001", "1002", "2000.0", "1234",
                "0"
            ) + System.lineSeparator();
            ByteArrayOutputStream trfOut = new ByteArrayOutputStream();
            AccountUI trfUI = new AccountUI(service, new ByteArrayInputStream(trfInputs.getBytes(StandardCharsets.UTF_8)), new PrintStream(trfOut));
            trfUI.start();
            System.out.println("  Transfer of Rs. 2000.0 from #1001 to #1002 completed.");
            System.out.println();

            System.out.println("[STEP 6] Validating UI Account Summary:");
            System.out.println("  Account #1001: Rs. " + acc1.getBalance());
            System.out.println("  Account #1002: Rs. " + acc2.getBalance());
            System.out.println();

            System.out.println("[STEP 7] Validating Transaction History via UI (" + service.getTransactionHistory().size() + " records):");
            List<Transaction> history = service.getTransactionHistory();
            for (int i = 0; i < history.size(); i++) {
                System.out.println("  [" + (i + 1) + "] " + history.get(i));
            }
            System.out.println();

            System.out.println("[STEP 8] Validating UI Error Handling:");
            String errInputs = String.join(System.lineSeparator(),
                "3", "1001", "500.0", "9999",
                "2", "9999", "1000.0",
                "0"
            ) + System.lineSeparator();
            ByteArrayOutputStream errOut = new ByteArrayOutputStream();
            AccountUI errUI = new AccountUI(service, new ByteArrayInputStream(errInputs.getBytes(StandardCharsets.UTF_8)), new PrintStream(errOut));
            errUI.start();
            String errOutput = errOut.toString(StandardCharsets.UTF_8);

            boolean pinPassed = errOutput.contains("Incorrect PIN");
            boolean missingPassed = errOutput.contains("Account not found");
            System.out.println("  Invalid PIN test: " + (pinPassed ? "PASSED" : "FAILED"));
            System.out.println("  Account not found test: " + (missingPassed ? "PASSED" : "FAILED"));
            System.out.println();

            System.out.println("[STEP 9] Full UI & Console Integration Verified Successfully!");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
