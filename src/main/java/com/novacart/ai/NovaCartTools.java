package com.novacart.ai;

import com.novacart.order.Order;
import com.novacart.order.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

@Configuration
public class NovaCartTools {

    private final OrderRepository orderRepository;

    public NovaCartTools(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public static class OrderRequest {
        private String orderNumber;

        public String getOrderNumber() { return orderNumber; }
        public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    }

    public static class OrderResponse {
        private String orderNumber;
        private String status;
        private String paymentStatus;
        private BigDecimal totalAmount;

        public OrderResponse(String orderNumber, String status, String paymentStatus, BigDecimal totalAmount) {
            this.orderNumber = orderNumber;
            this.status = status;
            this.paymentStatus = paymentStatus;
            this.totalAmount = totalAmount;
        }

        public String getOrderNumber() { return orderNumber; }
        public String getStatus() { return status; }
        public String getPaymentStatus() { return paymentStatus; }
        public BigDecimal getTotalAmount() { return totalAmount; }
    }

    @Bean
    @Description("Get order details and status by order number from the database")
    public Function<OrderRequest, OrderResponse> getOrderStatus() {
        return request -> {
            Optional<Order> optionalOrder = orderRepository.findByOrderNumber(request.getOrderNumber());
            
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                return new OrderResponse(
                    order.getOrderNumber(),
                    order.getStatus(),
                    order.getPaymentStatus(),
                    order.getTotalAmount()
                );
            }
            
            return new OrderResponse(request.getOrderNumber(), "NOT_FOUND", "N/A", BigDecimal.ZERO);
        };
    }
}