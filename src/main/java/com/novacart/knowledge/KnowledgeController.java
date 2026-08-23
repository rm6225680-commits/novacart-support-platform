package com.novacart.knowledge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    @Autowired
    private KnowledgeService knowledgeService;

    @PostMapping
    public ResponseEntity<KnowledgeDocument> addDocument(@RequestBody Map<String, String> request) {
        String title = request.get("title");
        String content = request.get("content");
        KnowledgeDocument savedDoc = knowledgeService.saveDocument(title, content);
        return ResponseEntity.ok(savedDoc);
    }

    @GetMapping
    public ResponseEntity<List<KnowledgeDocument>> getAllDocuments() {
        List<KnowledgeDocument> docs = knowledgeService.getAllDocuments();
        return ResponseEntity.ok(docs);
    }
}