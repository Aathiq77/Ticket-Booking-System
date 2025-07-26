package com.example.ticketsystem.model;

import com.example.ticketsystem.service.LogService;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Customer implements Runnable {
    private TicketPool ticketPool;
    private int customerRetrievalRate;
    private LogService logService;
    private ScheduledExecutorService scheduler;
    private int customerId;
    private static int nextCustomerId = 1;
    private volatile boolean running = true;
    private boolean hasBoughtTicket = false;
    private AtomicInteger ticketsBought = new AtomicInteger(0); // Counter for tickets bought

    public Customer(TicketPool ticketPool, int customerRetrievalRate, LogService logService) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.logService = logService;
        this.customerId = nextCustomerId++;
        this.scheduler = createScheduler();
    }

    public void start() {
        running = true;
        scheduler.scheduleAtFixedRate(this, 0, customerRetrievalRate, TimeUnit.SECONDS);
    }

    public void stop() {
        running = false;
        scheduler.shutdownNow();
    }

    private ScheduledExecutorService createScheduler() {
        return java.util.concurrent.Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("Customer-" + customerId);
            return thread;
        });
    }

    public int getCustomerId() {
        return customerId;
    }

    @Override
    public void run() {
        if (!running || hasBoughtTicket || ticketPool.isEmpty()) {
            return;
        }
        try {
            Ticket ticket = ticketPool.buyTicket();
            if (ticket != null) {
                hasBoughtTicket = true;
                int bought = ticketsBought.incrementAndGet(); // Increment and get updated count
                logService.addLog("Customer " + customerId + " bought a ticket. Total tickets bought by this customer: " + bought + ". Available tickets: " + ticketPool.getAvailableTickets());
            }
        } catch (Exception e) {
            logService.addLog("Error during ticket retrieval for customer " + customerId + ": " + e.getMessage());
        }
    }
}