package com.novacart;

import com.novacart.customer.Customer;
import com.novacart.customer.CustomerRepository;
import com.novacart.order.Order;
import com.novacart.order.OrderRepository;
import com.novacart.product.Product;
import com.novacart.product.ProductRepository;
import com.novacart.service.RagService;
import com.novacart.ticket.SupportTicket;
import com.novacart.ticket.SupportTicketRepository;
import com.novacart.user.User;
import com.novacart.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
public class NovaCartSupportPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaCartSupportPlatformApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      CustomerRepository customerRepository,
                                      ProductRepository productRepository,
                                      OrderRepository orderRepository,
                                      SupportTicketRepository supportTicketRepository,
                                      PasswordEncoder passwordEncoder,
                                      RagService ragService) {
        return args -> {
            // 1. Default Admin User Setup
            if (userRepository.findByEmail("test@novacart.com").isEmpty()) {
                User admin = new User();
                admin.setName("Admin User");
                admin.setEmail("test@novacart.com");
                admin.setPassword(passwordEncoder.encode("password123"));
                admin.setRole("ADMIN");
                userRepository.save(admin);
            }

            // 2. Default Customers
            Customer customer1 = customerRepository.findAll().stream()
                    .filter(c -> "CUST-1001".equals(c.getCustomerCode()))
                    .findFirst()
                    .orElseGet(() -> {
                        Customer c = new Customer();
                        c.setCustomerCode("CUST-1001");
                        c.setName("Rahul Sharma");
                        c.setEmail("rahul@example.com");
                        c.setPhone("9876543210");
                        c.setAddress("123, MG Road, Bangalore");
                        return customerRepository.save(c);
                    });

            // 3. Default Products
            Product product1 = productRepository.findByProductCode("PROD-LAPTOP-01").orElseGet(() -> {
                Product p = new Product();
                p.setProductCode("PROD-LAPTOP-01");
                p.setName("NovaBook Pro 15");
                p.setCategory("Electronics");
                p.setPrice(new BigDecimal("75000.00"));
                p.setWarrantyMonths(12);
                p.setDescription("High performance laptop with 16GB RAM and 512GB SSD.");
                return productRepository.save(p);
            });

            // 4. Default Orders
            if (orderRepository.findByOrderNumber("ORD-2026-001").isEmpty()) {
                Order order = new Order();
                order.setOrderNumber("ORD-2026-001");
                order.setCustomer(customer1);
                order.setProduct(product1);
                order.setQuantity(1);
                order.setStatus("DELIVERED");
                order.setPaymentStatus("PAID");
                order.setTotalAmount(new BigDecimal("75000.00"));
                order.setOrderDate(LocalDateTime.now().minusDays(5));
                order.setDeliveryDate(LocalDateTime.now().minusDays(1));
                orderRepository.save(order);
            }

            // 5. Default Support Tickets
            if (supportTicketRepository.findByTicketNumber("TICK-9991").isEmpty()) {
                SupportTicket ticket = new SupportTicket();
                ticket.setTicketNumber("TICK-9991");
                ticket.setCustomer(customer1);
                ticket.setCategory("Hardware");
                ticket.setSubject("Laptop charging issue");
                ticket.setDescription("My NovaBook Pro 15 is not charging after 3 days of use.");
                ticket.setStatus("OPEN");
                ticket.setPriority("HIGH");
                supportTicketRepository.save(ticket);
            }

            // 6. Seed Initial RAG Knowledge Base Policies
            ragService.addTrainingData("NovaCart Refund Policy: Customers can request a full refund within 7 days of product delivery if the item is defective or damaged.");
            ragService.addTrainingData("NovaCast Warranty Terms: All electronics come with a standard 1-year manufacturer warranty covering internal hardware defects. Accidental damage is not covered.");
            ragService.addTrainingData("NovaCart Shipping Policy: Standard delivery takes 3-5 business days across major cities. Express delivery is available for selected pin codes.");
        };
    }
}