package com.novacart.controller;

import com.novacart.ticket.SupportTicket;
import com.novacart.ticket.SupportTicketRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketAiController {

    private final SupportTicketRepository supportTicketRepository;
    private final ChatModel chatModel;

    public TicketAiController(SupportTicketRepository supportTicketRepository, ChatModel chatModel) {
        this.supportTicketRepository = supportTicketRepository;
        this.chatModel = chatModel;
    }

    @PostMapping("/{id}/ai/analyze")
    public ResponseEntity<String> analyzeTicket(@PathVariable Long id) {
        Optional<SupportTicket> ticketOpt = supportTicketRepository.findById(id);
        if (ticketOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Support Ticket not found with ID: " + id);
        }

        SupportTicket ticket = ticketOpt.get();

        // Prompt for AI to analyze the support ticket
        String prompt = "You are an expert customer support assistant for NovaCart.\n" +
                "Analyze the following support ticket and provide:\n" +
                "1. Ticket Summary\n" +
                "2. Suggested Priority (Low/Medium/High)\n" +
                "3. Recommended Resolution Steps for the Support Agent\n\n" +
                "Ticket Subject: " + ticket.getSubject() + "\n" +
                "Ticket Description: " + ticket.getDescription() + "\n" +
                "Customer Email: " + ticket.getCustomer().getEmail() + "\n";

        String aiResponse = chatModel.call(prompt);
        return ResponseEntity.ok(aiResponse);
    }
}