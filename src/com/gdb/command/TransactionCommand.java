package com.gdb.command;

import com.gdb.domain.Transaction;
import java.io.Serializable;

public interface TransactionCommand extends Serializable {
    void execute() throws Exception;
    Transaction getTransaction();
}
