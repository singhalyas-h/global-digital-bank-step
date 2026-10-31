package com.gdb;

import com.gdb.logging.FileLogDestination;
import com.gdb.logging.LogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;
import com.gdb.ui.AccountUI;

public class Main {
    public static void main(String[] args) {
        LogDestination logDestination = new FileLogDestination();
        TransactionLogger logger = new TransactionLogger(logDestination);
        AccountService accountService = new AccountService(logger);
        AccountUI ui = new AccountUI(accountService);
        ui.start();
    }
}
