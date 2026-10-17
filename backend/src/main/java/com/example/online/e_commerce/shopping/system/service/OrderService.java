package com.example.online.e_commerce.shopping.system.service;

import com.example.online.e_commerce.shopping.system.dto.CartDto;
import com.example.online.e_commerce.shopping.system.dto.CartItemDto;
import com.example.online.e_commerce.shopping.system.model.*;
import com.example.online.e_commerce.shopping.system.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @Transactional
    public Order createOrder(User customer, String customerName, String customerEmail,
                            String customerPhone, String shippingAddress, String city,
                            String postalCode, PaymentMethod paymentMethod, CartDto cart) {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalStateException("Cannot create order from an empty cart.");
        }

        // Generate unique order number (e.g. ORD-20261010-XXXX)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String orderNumber = "ORD-" + timestamp + "-" + randomSuffix;

        Order order = new Order(
                orderNumber,
                customer,
                customerName,
                customerEmail,
                customerPhone,
                shippingAddress,
                city,
                postalCode,
                cart.getTotalAmount(),
                OrderStatus.PLACED,
                paymentMethod,
                paymentMethod == PaymentMethod.CASH_ON_DELIVERY ? "PENDING" : "PAID"
        );

        for (CartItemDto item : cart.getItems()) {
            OrderItem orderItem = new OrderItem(
                    order,
                    item.getProduct(),
                    item.getProduct().getName(),
                    item.getProduct().getPrice(),
                    item.getQuantity()
            );
            order.addItem(orderItem);

            // Deduct inventory
            productService.decreaseStock(item.getProduct().getId(), item.getQuantity());
        }

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Order> getRecentOrders() {
        return orderRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        order.setStatus(newStatus);
        if (newStatus == OrderStatus.DELIVERED) {
            order.setPaymentStatus("PAID");
        }
        return orderRepository.save(order);
    }

    public long countTotalOrders() {
        return orderRepository.count();
    }

    public long countOrdersByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    public Double calculateTotalRevenue() {
        Double rev = orderRepository.calculateTotalRevenue();
        return rev != null ? Math.round(rev * 100.0) / 100.0 : 0.0;
    }
}
