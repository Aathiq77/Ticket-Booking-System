package com.example.ticketsystem.service;

import com.example.ticketsystem.model.Configuration;
import com.example.ticketsystem.model.Customer;
import com.example.ticketsystem.model.TicketPool;
import com.example.ticketsystem.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;

@Service
public class SystemService {

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private LogService logService;

    private ExecutorService vendorExecutor;
    private ExecutorService customerExecutor;
    private volatile boolean isRunning = false;
    private Configuration currentConfig;
    private List<Vendor> vendors = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();

    public void startSystem(Configuration config) {
        if (isRunning) {
            logService.addLog("System is already running");
            return;
        }

        ticketPool.initialize(config.getMaxTicketCapacity());
        logService.clearLogs();
        isRunning = true;
        currentConfig = config;

        vendorExecutor = Executors.newFixedThreadPool(config.getNumVendors());
        customerExecutor = Executors.newFixedThreadPool(config.getNumCustomers());

        logService.addLog("Starting system with configuration: " + config.toString());

        // Create and start Vendor threads
        vendors.clear(); // Clear any existing vendors
        for (int i = 0; i < config.getNumVendors(); i++) {
            Vendor vendor = new Vendor(ticketPool, config.getTicketReleaseRate(), logService);
            vendors.add(vendor);
            vendorExecutor.execute(vendor);
        }

        // Create and start Customer threads
        customers.clear(); // Clear any existing customers
        for (int i = 0; i < config.getNumCustomers(); i++) {
            Customer customer = new Customer(ticketPool, config.getCustomerRetrievalRate(), logService);
            customers.add(customer);
            customerExecutor.submit(customer); // Use submit() to start the customer thread
        }

        logService.addLog("Vendors and customers started.");
    }

    public void stopSystem() {
        if (!isRunning) {
            logService.addLog("System is not running");
            return;
        }

        isRunning = false;
        logService.addLog("Stopping system...");

        // Stop vendors
        for (Vendor vendor : vendors) {
            if (vendor != null)
                vendor.stop();
        }

        // Shutdown vendor executor
        shutdownExecutor(vendorExecutor, "Vendor");

        // Stop customers
        for (Customer customer : customers) {
            if (customer != null)
                customer.stop();
        }

        // Shutdown customer executor
        shutdownExecutor(customerExecutor, "Customer");

        logService.addLog("System stopped.");
    }

    private void shutdownExecutor(ExecutorService executor, String executorName) {
        if (executor != null) {
            executor.shutdownNow();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    logService.addLog(executorName + " executor did not terminate in the allotted time.");
                }
            } catch (InterruptedException ex) {
                logService.addLog("Interrupted while waiting for " + executorName + " executor to stop.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getTotalTickets() {
        return currentConfig != null ? currentConfig.getTotalTickets() : 0;
    }

    public int getAvailableTickets() {
        return ticketPool.getAvailableTickets();
    }

    public boolean isSystemRunning() {
        return isRunning;
    }

    public void saveConfiguration(Configuration configuration) {
        this.currentConfig = configuration;
        logService.addLog("Configuration saved: " + configuration.toString());
    }

    public Configuration getCurrentConfig() {
        return currentConfig;
    }
}