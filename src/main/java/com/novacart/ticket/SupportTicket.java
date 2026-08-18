package com.novacart.ticket;

import com.novacart.customer.Customer;
import com.novacart.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "support_tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String ticketNumber; // e.g., TKT-1001

    // Many Support Tickets belong to One Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Optional link to an Order if the complaint is order-related
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false, length = 200)
    private String subject; // Brief title of the issue

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description; // Detailed customer complaint

    @Column(nullable = false, length = 100)
    private String category; // DAMAGED_PRODUCT, WRONG_ITEM, DELAYED_DELIVERY, WARRANTY_CLAIM, GENERAL_INQUIRY

    @Column(nullable = false, length = 50)
    private String priority; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(nullable = false, length = 50)
    private String status; // OPEN, IN_PROGRESS, ESCALATED, RESOLVED, CLOSED

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}