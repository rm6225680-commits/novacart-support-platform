package com.novacart.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class SupportTicketController {

    @Autowired
    private SupportTicketService supportTicketService;

    @PostMapping
    public ResponseEntity<SupportTicket> createTicket(@RequestBody SupportTicket ticket) {
        SupportTicket created = supportTicketService.createTicket(ticket);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        List<SupportTicket> tickets = supportTicketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicket> getTicketById(@PathVariable Long id) {
        return supportTicketService.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SupportTicket>> getTicketsByCustomerId(@PathVariable Long customerId) {
        List<SupportTicket> tickets = supportTicketService.getTicketsByCustomerId(customerId);
        return ResponseEntity.ok(tickets);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        supportTicketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}