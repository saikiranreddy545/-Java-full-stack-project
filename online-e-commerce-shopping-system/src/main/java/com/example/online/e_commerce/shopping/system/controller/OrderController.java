package com.example.online.e_commerce.shopping.system.controller;

import com.example.online.e_commerce.shopping.system.model.Order;
import com.example.online.e_commerce.shopping.system.model.User;
import com.example.online.e_commerce.shopping.system.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String myOrders(@RequestParam(name = "searchEmail", required = false) String searchEmail,
                           HttpSession session,
                           Model model) {
        User currentUser = (User) session.getAttribute(GlobalControllerAdvice.SESSION_USER_KEY);
        List<Order> orders;

        if (currentUser != null) {
            orders = orderService.getOrdersByCustomer(currentUser.getId());
        } else if (searchEmail != null && !searchEmail.isBlank()) {
            orders = orderService.getOrdersByEmail(searchEmail.trim());
            model.addAttribute("searchedEmail", searchEmail);
        } else {
            orders = Collections.emptyList();
        }

        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/orders/{orderNumber}")
    public String orderDetail(@PathVariable("orderNumber") String orderNumber, Model model) {
        Optional<Order> orderOpt = orderService.getOrderByOrderNumber(orderNumber);
        if (orderOpt.isEmpty()) {
            return "redirect:/orders";
        }
        model.addAttribute("order", orderOpt.get());
        return "order-detail";
    }
}
