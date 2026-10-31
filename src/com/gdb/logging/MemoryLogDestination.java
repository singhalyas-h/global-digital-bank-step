package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryLogDestination implements LogDestination {
    private final List<TransactionCommand> logs = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void write(TransactionCommand cmd) {
        logs.add(cmd);
    }

    @Override
    public List<TransactionCommand> readAll() {
        synchronized (logs) {
            return new ArrayList<>(logs);
        }
    }

    @Override
    public void clear() {
        logs.clear();
    }

    @Override
    public String getDestinationName() {
        return "MEMORY";
    }
}
