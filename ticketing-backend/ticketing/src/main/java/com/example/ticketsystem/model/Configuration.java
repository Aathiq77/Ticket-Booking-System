// Configuration.java
package com.example.ticketsystem.model;

import lombok.Data;

@Data
public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int numVendors;
    private int numCustomers;

    public int getTotalTickets() {
        return totalTickets;
    }
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
    public int getNumVendors() {
        return numVendors;
    }
    public int getNumCustomers() {
        return numCustomers;
    }
}