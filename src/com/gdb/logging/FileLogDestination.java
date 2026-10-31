package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.util.List;

public class FileLogDestination implements LogDestination {
    private TransactionLog log;

    public FileLogDestination() {
        this.log = new TransactionLog();
    }

    public FileLogDestination(TransactionLog log) {
        this.log = log;
    }

    @Override
    public void write(TransactionCommand cmd) {
        log.log(cmd);
    }

    @Override
    public List<TransactionCommand> readAll() {
        return log.readAll();
    }

    @Override
    public void clear() {
        log.clear();
    }

    @Override
    public String getDestinationName() {
        return "FILE";
    }
}
