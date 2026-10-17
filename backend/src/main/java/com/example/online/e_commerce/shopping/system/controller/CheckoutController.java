package com.example.online.e_commerce.shopping.system.controller;

import com.example.online.e_commerce.shopping.system.dto.CartDto;
import com.example.online.e_commerce.shopping.system.model.Order;
import com.example.online.e_commerce.shopping.system.model.PaymentMethod;
import com.example.online.e_commerce.shopping.system.model.User;
import com.example.online.e_commerce.shopping.system.service.CartService;
import com.example.online.e_commerce.shopping.system.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        CartDto cart = cartService.getCart(session);
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty. Add products before checking out.");
            return "redirect:/products";
        }

        User currentUser = (User) session.getAttribute(GlobalControllerAdvice.SESSION_USER_KEY);
        model.addAttribute("cart", cart);
        model.addAttribute("paymentMethods", PaymentMethod.values());

        if (currentUser != null) {
            model.addAttribute("defaultName", currentUser.getName());
            model.addAttribute("defaultEmail", currentUser.getEmail());
            model.addAttribute("defaultPhone", currentUser.getPhone());
            model.addAttribute("defaultAddress", currentUser.getAddress());
        }

        return "checkout";
    }

    @PostMapping("/checkout/place-order")
    public String placeOrder(@RequestParam("customerName") String customerName,
                             @RequestParam("customerEmail") String customerEmail,
                             @RequestParam("customerPhone") String customerPhone,
                             @RequestParam("shippingAddress") String shippingAddress,
                             @RequestParam("city") String city,
                             @RequestParam("postalCode") String postalCode,
                             @RequestParam("paymentMethod") PaymentMethod paymentMethod,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        CartDto cart = cartService.getCart(session);
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot place an order with an empty cart.");
            return "redirect:/cart";
        }

        User currentUser = (User) session.getAttribute(GlobalControllerAdvice.SESSION_USER_KEY);

        try {
            Order order = orderService.createOrder(
                    currentUser,
                    customerName,
                    customerEmail,
                    customerPhone,
                    shippingAddress,
                    city,
                    postalCode,
                    paymentMethod,
                    cart
            );

            // Empty cart after successful placement
            cartService.clearCart(session);

            return "redirect:/order-confirmation/" + order.getOrderNumber();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to process order: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/order-confirmation/{orderNumber}")
    public String orderConfirmation(@PathVariable("orderNumber") String orderNumber, Model model) {
        Optional<Order> orderOpt = orderService.getOrderByOrderNumber(orderNumber);
        if (orderOpt.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("order", orderOpt.get());
        return "order-confirmation";
    }
}
