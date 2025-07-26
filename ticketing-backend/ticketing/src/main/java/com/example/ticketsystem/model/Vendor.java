package com.example.ticketsystem.model;

import com.example.ticketsystem.service.LogService;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class Vendor implements Runnable {
    private volatile boolean running = true;
    private TicketPool ticketPool;
    private int ticketReleaseRate;
    private LogService logService;
    private int vendorId;
    private static int nextVendorId = 1;
    private AtomicInteger ticketsAdded = new AtomicInteger(0); // Counter for tickets added

    public Vendor(TicketPool ticketPool, int ticketReleaseRate, LogService logService) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.logService = logService;
        this.vendorId = nextVendorId++;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running && !ticketPool.isFull()) {
            try {
                ticketPool.addTicket("Event Simple", new BigDecimal("1000"));
                int added = ticketsAdded.incrementAndGet(); // Increment and get the updated count
                logService.addLog("Vendor " + vendorId + " added a ticket. Total tickets added by this vendor: " + added + ". Available tickets in pool: " + ticketPool.getAvailableTickets());
                Thread.sleep(ticketReleaseRate * 1000L);
            } catch (InterruptedException e) {
                if (!running) {
                    break;
                }
            }
        }
        if (ticketPool.isFull()) {
            logService.addLog("Ticket pool is full. Vendor " + vendorId + " stopping. Total tickets added: " + ticketsAdded.get());
        }
    }
}