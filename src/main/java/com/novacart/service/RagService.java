package com.novacart.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final VectorStore vectorStore;
    private final ChatModel chatModel;

    
    public RagService(VectorStore vectorStore, ChatModel chatModel) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
    }

    public void addTrainingData(String text) {
        Document document = new Document(text);
        vectorStore.add(List.of(document));
    }

    public String askAiWithContext(String query) {
        List<Document> similarDocuments = vectorStore.similaritySearch(
            SearchRequest.query(query).withTopK(3)
        );

        String context = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        if (context.isEmpty()) {
            context = "No specific internal context found.";
        }

        String prompt = "Context:\n" + context + "\n\nQuestion: " + query + "\nAnswer:";
        return chatModel.call(prompt);
    }
}