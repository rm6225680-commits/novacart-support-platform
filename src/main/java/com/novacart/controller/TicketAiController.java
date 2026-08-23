package com.novacart.controller;

import com.novacart.ticket.SupportTicket;
import com.novacart.ticket.SupportTicketRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketAiController {

    private final SupportTicketRepository supportTicketRepository;
    private final ChatClient chatClient;

    public TicketAiController(SupportTicketRepository supportTicketRepository, ChatClient.Builder chatClientBuilder) {
        this.supportTicketRepository = supportTicketRepository;
        this.chatClient = chatClientBuilder.build();
    }

    // Phase 18: AI Ticket Analysis Endpoint
    @PostMapping("/{id}/ai/analyze")
    public ResponseEntity<Map<String, String>> analyzeTicket(@PathVariable Long id) {
        Optional<SupportTicket> ticketOpt = supportTicketRepository.findById(id);
        if (ticketOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Support Ticket not found with ID: " + id));
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
                "Customer Email: " + (ticket.getCustomer() != null ? ticket.getCustomer().getEmail() : "N/A") + "\n";

        String aiResponse = chatClient.prompt().user(prompt).call().content();

        Map<String, String> response = new HashMap<>();
        response.put("ticketNumber", ticket.getTicketNumber());
        response.put("analysis", aiResponse);

        return ResponseEntity.ok(response);
    }

    // Phase 19: AI Suggested Reply Endpoint
    @PostMapping("/{id}/ai/reply")
    public ResponseEntity<Map<String, String>> generateAiReply(@PathVariable Long id) {
        Optional<SupportTicket> ticketOpt = supportTicketRepository.findById(id);
        if (ticketOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Support Ticket not found with ID: " + id));
        }

        SupportTicket ticket = ticketOpt.get();

        String prompt = "Write a professional customer support email response for the following ticket:\n" +
                "Customer Issue: " + ticket.getDescription() + "\n" +
                "Category: " + ticket.getCategory() + "\n" +
                "Status: " + ticket.getStatus() + "\n\n" +
                "Make it polite, empathetic, and clear on next steps.";

        String suggestedReply = chatClient.prompt().user(prompt).call().content();

        Map<String, String> response = new HashMap<>();
        response.put("ticketNumber", ticket.getTicketNumber());
        response.put("suggestedReply", suggestedReply);

        return ResponseEntity.ok(response);
    }
}