package com.gdb.database;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimulatedDatabase {
    private final Map<String, List<Object>> tables = new ConcurrentHashMap<>();

    public void insert(String tableName, Object record) {
        tables.computeIfAbsent(tableName, k -> Collections.synchronizedList(new ArrayList<>())).add(record);
    }

    public List<Object> selectAll(String tableName) {
        List<Object> list = tables.get(tableName);
        if (list == null) {
            return Collections.emptyList();
        }
        synchronized (list) {
            return new ArrayList<>(list);
        }
    }

    public void deleteAll(String tableName) {
        List<Object> list = tables.get(tableName);
        if (list != null) {
            list.clear();
        }
    }

    public int count(String tableName) {
        List<Object> list = tables.get(tableName);
        return list == null ? 0 : list.size();
    }
}
