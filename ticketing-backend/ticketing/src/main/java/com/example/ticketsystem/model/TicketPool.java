package com.example.ticketsystem.model;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TicketPool {
    private final ConcurrentLinkedQueue<Ticket> ticketQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger ticketIdCounter = new AtomicInteger(0);

    private int maxTicketCapacity;

    public synchronized void initialize(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
        ticketQueue.clear();
    }

    public synchronized void addTicket(String eventName, BigDecimal price) {
        if (ticketQueue.size() < maxTicketCapacity) {
            int ticketId = ticketIdCounter.incrementAndGet();
            Ticket ticket = new Ticket(ticketId, eventName, price);
            ticketQueue.add(ticket);
            notify();
        }
    }

    public synchronized Ticket buyTicket() {
        return ticketQueue.poll();
    }

    // Getters and setters for maxTicketCapacity

    public int getAvailableTickets() {
        return ticketQueue.size();
    }

    public boolean isFull() {
        return ticketQueue.size() >= maxTicketCapacity;
    }

    public boolean isEmpty() {
        return ticketQueue.isEmpty();
    }
}