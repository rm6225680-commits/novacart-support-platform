package com.novacart.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    
    Optional<SupportTicket> findByTicketNumber(String ticketNumber);
    
    // Find tickets belonging to a specific customer
    List<SupportTicket> findByCustomerId(Long customerId);
    
    // Find tickets by status (e.g., OPEN, IN_PROGRESS, ESCALATED)
    List<SupportTicket> findByStatus(String status);
}