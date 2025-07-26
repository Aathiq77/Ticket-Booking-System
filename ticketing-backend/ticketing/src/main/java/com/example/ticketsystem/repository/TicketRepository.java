package com.example.ticketsystem.repository;

import com.example.ticketsystem.model.Ticket;
import org.springframework.stereotype.Repository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class TicketRepository {

    private final ConcurrentHashMap<Integer, Ticket> tickets = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    public Ticket save(Ticket ticket) {
        if (ticket.getTicketId() == 0) {
            ticket.setTicketId(idGenerator.incrementAndGet());
        }
        tickets.put(ticket.getTicketId(), ticket);
        return ticket;
    }

    public Ticket findById(int ticketId) {
        return tickets.get(ticketId);
    }

    // Add other methods as needed for data access
}