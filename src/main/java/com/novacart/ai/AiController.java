package com.novacart.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novacart.service.RagService;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired(required = false)
    private AiService aiService;

    private final RagService ragService;

    public AiController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chatWithAi(@RequestBody AiRequest request) {
        if (aiService == null) {
            return ResponseEntity.badRequest().body("AiService is not configured.");
        }
        String response = aiService.getResponse(request.getMessage());
        return ResponseEntity.ok(response);
    }

    // Postman ke request URL /api/ai/
    @PostMapping("/ask")
    public ResponseEntity<String> askAiWithBody(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        if (query == null || query.isEmpty()) {
            return ResponseEntity.badRequest().body("Query parameter is missing in JSON body.");
        }
        String response = ragService.askAiWithContext(query);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rag-chat")
    public ResponseEntity<String> chatWithRag(@RequestParam String message) {
        String response = ragService.askAiWithContext(message);
        return ResponseEntity.ok(response);
    }
}

class AiRequest {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}