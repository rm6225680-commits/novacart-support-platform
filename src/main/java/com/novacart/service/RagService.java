package com.novacart.service;

import com.novacart.order.Order;
import com.novacart.order.OrderRepository;
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
    private final OrderRepository orderRepository;

    public RagService(VectorStore vectorStore, ChatModel chatModel, OrderRepository orderRepository) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
        this.orderRepository = orderRepository;
    }

    public void addTrainingData(String text) {
        Document document = new Document(text);
        vectorStore.add(List.of(document));
    }

    public String askAiWithContext(String query) {
        // 
        if (query.toUpperCase().contains("ORD-")) {
            String orderNumber = extractOrderNumber(query);
            if (orderNumber != null) {
                Order order = orderRepository.findByOrderNumber(orderNumber).orElse(null);
                if (order != null) {
                    return "Here are the live details for your order (" + order.getOrderNumber() + "):\n" +
                           "- Status: " + order.getStatus() + "\n" +
                           "- Payment Status: " + order.getPaymentStatus() + "\n" +
                           "- Total Amount: ₹" + order.getTotalAmount() + "\n" +
                           "- Product: " + order.getProduct().getName() + "\n" +
                           "- Order Date: " + order.getOrderDate();
                } else {
                    return "I checked the database, but no order was found with number: " + orderNumber;
                }
            }
        }

        //  RAG (Vector Store)
        List<Document> similarDocuments = vectorStore.similaritySearch(
            SearchRequest.query(query).withTopK(3)
        );

        String context = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        if (context.isEmpty()) {
            context = "No specific internal context found.";
        }

        String prompt = "Context from Policies:\n" + context + "\n\nQuestion: " + query + "\nAnswer:";
        return chatModel.call(prompt);
    }

    // Helper method to extract order number from query string
    private String extractOrderNumber(String query) {
        String[] words = query.split("\\s+");
        for (String word : words) {
            if (word.toUpperCase().startsWith("ORD-")) {
                return word.replaceAll("[^a-zA-Z0-9-]", "");
            }
        }
        return null;
    }
}