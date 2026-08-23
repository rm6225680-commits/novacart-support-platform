package com.novacart.service;

import com.novacart.order.Order;
import com.novacart.order.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class OrderTools {

    private final OrderRepository orderRepository;

    public OrderTools(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Bean(name = "getOrderDetails")
    @Description("Get live order details and delivery status using the order number like ORD-2026-001")
    public Function<OrderRequest, OrderResponse> getOrderDetails() {
        return request -> {
            Order order = orderRepository.findByOrderNumber(request.orderNumber()).orElse(null);
            if (order == null) {
                return new OrderResponse("Order not found with number: " + request.orderNumber());
            }
            return new OrderResponse(
                "Order Number: " + order.getOrderNumber() + 
                ", Status: " + order.getStatus() + 
                ", Payment Status: " + order.getPaymentStatus() + 
                ", Total Amount: ₹" + order.getTotalAmount() + 
                ", Product: " + order.getProduct().getName()
            );
        };
    }

    public record OrderRequest(String orderNumber) {}
    public record OrderResponse(String details) {}
}