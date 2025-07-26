package com.example.ticketsystem.controller;

import com.example.ticketsystem.model.Configuration;
import com.example.ticketsystem.service.LogService;
import com.example.ticketsystem.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class SystemController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private LogService logService;

    @PostMapping("/config")
    public ResponseEntity<String> saveConfiguration(@RequestBody Configuration configuration) {
        systemService.saveConfiguration(configuration);
        logService.addLog("Configuration saved: " + configuration.toString()); // Log the saved configuration
        return ResponseEntity.ok("Configuration saved");
    }

    @GetMapping("/config")
    public ResponseEntity<Configuration> getConfiguration() {
        Configuration config = systemService.getCurrentConfig();
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(config);
    }

    @PostMapping("/start")
    public ResponseEntity<String> startSystem() {
        Configuration currentConfig = systemService.getCurrentConfig();
        if (currentConfig == null) {
            return ResponseEntity.badRequest().body("Configuration not saved. Please save configuration first.");
        }
        if (systemService.isSystemRunning()) {
            return ResponseEntity.badRequest().body("System is already running");
        }

        systemService.startSystem(currentConfig);
        return ResponseEntity.ok("System started");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopSystem() {
        if (!systemService.isSystemRunning()) {
            return ResponseEntity.badRequest().body("System is not running");
        }
        systemService.stopSystem();
        return ResponseEntity.ok("System stopped");
    }

    @GetMapping("/tickets/total")
    public ResponseEntity<Integer> getTotalTickets() {
        return ResponseEntity.ok(systemService.getTotalTickets());
    }

    @GetMapping("/tickets/available")
    public ResponseEntity<Integer> getAvailableTickets() {
        int availableTickets = systemService.getAvailableTickets();
        return ResponseEntity.ok(availableTickets);
    }

    @GetMapping("/logs")
    public ResponseEntity<List<String>> getLogs() {
        List<String> logs = logService.getLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/sales")
    public ResponseEntity<Map<LocalDateTime, Integer>> getSalesData() {
        Map<LocalDateTime, Integer> salesData = logService.getSalesData();
        return ResponseEntity.ok(salesData);
    }
}