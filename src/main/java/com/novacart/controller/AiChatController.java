package com.novacart.controller;

import com.novacart.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final RagService ragService;

    public AiChatController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/train")
    public ResponseEntity<String> trainData(@RequestBody String textData) {
        ragService.addTrainingData(textData);
        return ResponseEntity.ok("Data successfully chunked, embedded, and stored in Vector Store!");
    }

    @GetMapping("/ask")
    public ResponseEntity<String> askAi(@RequestParam String query) {
        String response = ragService.askAiWithContext(query);
        return ResponseEntity.ok(response);
    }
}