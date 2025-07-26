package com.example.ticketsystem.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Service
public class LogService {
    private final BlockingQueue<String> logs = new LinkedBlockingQueue<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm:ss a");

    // Map to store log entries with UUIDs
    private final ConcurrentHashMap<String, String> logEntries = new ConcurrentHashMap<>();

    // ConcurrentHashMap to store sales data
    private final ConcurrentHashMap<LocalDateTime, Integer> salesData = new ConcurrentHashMap<>();

    public void addLog(String message) {
        String logId = UUID.randomUUID().toString();
        String formattedLog = String.format("%s: %s", LocalDateTime.now().format(formatter), message);
        logs.offer(formattedLog);
        logEntries.put(logId, formattedLog);

        // Check if the log message indicates a ticket sale
        if (message.contains("bought a ticket")) {
            recordSale();
        }
    }

    // Method to record a sale
    public void recordSale() {
        LocalDateTime now = LocalDateTime.now();
        // Round down to the nearest minute
        LocalDateTime timestamp = now.withSecond(0).withNano(0);
        salesData.compute(timestamp, (key, value) -> (value == null) ? 1 : value + 1);
    }

    public List<String> getAndClearLogs() {
        List<String> logsToReturn = new ArrayList<>();
        logs.drainTo(logsToReturn);
        return logsToReturn;
    }

    public void clearLogs() {
        logs.clear();
        logEntries.clear(); // Clear the log entries map
    }

    public List<String> getLogs() {
        return new ArrayList<>(logEntries.values());
    }

    // Method to get a specific log by its ID
    public String getLogById(String logId) {
        return logEntries.get(logId);
    }

    public Map<LocalDateTime, Integer> getSalesData() {
        return new HashMap<>(salesData);
    }
}