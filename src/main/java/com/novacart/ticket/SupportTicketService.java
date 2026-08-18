package com.novacart.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupportTicketService {

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    public SupportTicket createTicket(SupportTicket ticket) {
        return supportTicketRepository.save(ticket);
    }

    public List<SupportTicket> getAllTickets() {
        return supportTicketRepository.findAll();
    }

    public Optional<SupportTicket> getTicketById(Long id) {
        return supportTicketRepository.findById(id);
    }

    public Optional<SupportTicket> getTicketByNumber(String ticketNumber) {
        return supportTicketRepository.findByTicketNumber(ticketNumber);
    }

    public List<SupportTicket> getTicketsByCustomerId(Long customerId) {
        return supportTicketRepository.findByCustomerId(customerId);
    }

    public List<SupportTicket> getTicketsByStatus(String status) {
        return supportTicketRepository.findByStatus(status);
    }

    public void deleteTicket(Long id) {
        supportTicketRepository.deleteById(id);
    }
}