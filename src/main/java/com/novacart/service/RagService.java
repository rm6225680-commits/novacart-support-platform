package com.novacart.service;

import com.novacart.order.OrderRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final VectorStore vectorStore;
    private final ChatModel chatModel;
    private final ChatClient chatClient;
    private final OrderRepository orderRepository;

    public RagService(VectorStore vectorStore, ChatModel chatModel, ChatClient.Builder chatClientBuilder, OrderRepository orderRepository) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
        this.orderRepository = orderRepository;
        
        // ChatClient initialize with tools enabled
        this.chatClient = chatClientBuilder
                .defaultFunctions("getOrderStatus") 
                .build();
    }

    public void addTrainingData(String text) {
        Document document = new Document(text);
        vectorStore.add(List.of(document));
    }

    public String askAiWithContext(String query) {
        // 1. Vector Store (RAG) 
        List<Document> similarDocuments = vectorStore.similaritySearch(
            SearchRequest.query(query).withTopK(3)
        );

        String context = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        if (context.isEmpty()) {
            context = "No specific internal policy context found.";
        }

       
        String systemPrompt = "You are an intelligent customer support assistant for NovaCart. " +
                "You have access to tools. If the user mentions an order number (like ORD-XXXXX), " +
                "you MUST call the getOrderStatus tool immediately to fetch live database details and present them.\n\n" +
                "Policy Context:\n" + context;

        try {
           
            return chatClient.prompt()
                    .system(systemPrompt)
                    .user(query)
                    .call()
                    .content();
        } catch (Exception e) {
           
            String fallbackPrompt = systemPrompt + "\n\nQuestion: " + query + "\nAnswer:";
            return chatModel.call(fallbackPrompt);
        }
    }
}