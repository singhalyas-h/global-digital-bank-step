package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import com.gdb.database.SimulatedDatabase;
import java.util.ArrayList;
import java.util.List;

public class DatabaseLogDestination implements LogDestination {
    private final SimulatedDatabase db;

    public DatabaseLogDestination(SimulatedDatabase db) {
        this.db = db;
    }

    @Override
    public void write(TransactionCommand cmd) {
        db.insert("transaction_log", cmd);
    }

    @Override
    public List<TransactionCommand> readAll() {
        List<Object> records = db.selectAll("transaction_log");
        List<TransactionCommand> commands = new ArrayList<>();
        for (Object record : records) {
            if (record instanceof TransactionCommand cmd) {
                commands.add(cmd);
            }
        }
        return commands;
    }

    @Override
    public void clear() {
        db.deleteAll("transaction_log");
    }

    @Override
    public String getDestinationName() {
        return "DATABASE";
    }
}
