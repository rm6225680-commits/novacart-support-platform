package com.novacart.service;

import com.novacart.customer.CustomerRepository;
import com.novacart.order.OrderRepository;
import com.novacart.ticket.SupportTicketRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final SupportTicketRepository supportTicketRepository;

    public AnalyticsService(CustomerRepository customerRepository, 
                            OrderRepository orderRepository, 
                            SupportTicketRepository supportTicketRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.supportTicketRepository = supportTicketRepository;
    }

    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalCustomers", customerRepository.count());
        stats.put("totalOrders", orderRepository.count());
        stats.put("totalTickets", supportTicketRepository.count());
        
      
        long openTickets = supportTicketRepository.findAll().stream()
                .filter(t -> "OPEN".equalsIgnoreCase(t.getStatus()))
                .count();
        long resolvedTickets = supportTicketRepository.findAll().stream()
                .filter(t -> "RESOLVED".equalsIgnoreCase(t.getStatus()))
                .count();
        long escalatedTickets = supportTicketRepository.findAll().stream()
                .filter(t -> "ESCALATED".equalsIgnoreCase(t.getStatus()))
                .count();

        stats.put("openTickets", openTickets);
        stats.put("resolvedTickets", resolvedTickets);
        stats.put("escalatedTickets", escalatedTickets);

        return stats;
    }
}