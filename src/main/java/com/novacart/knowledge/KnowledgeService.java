package com.novacart.knowledge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeService {

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    public KnowledgeDocument saveDocument(String title, String content) {
        KnowledgeDocument doc = new KnowledgeDocument(title, content);
        return knowledgeRepository.save(doc);
    }

    public List<KnowledgeDocument> getAllDocuments() {
        return knowledgeRepository.findAll();
    }
}