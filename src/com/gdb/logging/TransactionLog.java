package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionLog {
    private static final String DEFAULT_FILE_PATH = "data/transactions.ser";
    private final String filePath;

    public TransactionLog() {
        this(DEFAULT_FILE_PATH);
    }

    public TransactionLog(String filePath) {
        this.filePath = filePath;
    }

    public void log(TransactionCommand cmd) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        boolean append = file.exists() && file.length() > 0;
        try (OutputStream fos = new FileOutputStream(file, true);
             ObjectOutputStream oos = append ? new AppendableObjectOutputStream(fos) : new ObjectOutputStream(fos)) {
            oos.writeObject(cmd);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Error logging transaction command: " + e.getMessage());
        }
    }

    public List<TransactionCommand> readAll() {
        List<TransactionCommand> commands = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return commands;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof TransactionCommand cmd) {
                        commands.add(cmd);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading transaction log: " + e.getMessage());
        }
        return commands;
    }

    public void clear() {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}
