package com.example.online.e_commerce.shopping.system.controller;

import com.example.online.e_commerce.shopping.system.dto.CartDto;
import com.example.online.e_commerce.shopping.system.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        CartDto cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam(name = "quantity", defaultValue = "1") int quantity,
                            @RequestParam(name = "redirectUrl", required = false) String redirectUrl,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        boolean success = cartService.addToCart(session, productId, quantity);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Item successfully added to your shopping cart!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add item. The product may be out of stock.");
        }

        if (redirectUrl != null && !redirectUrl.isBlank()) {
            return "redirect:" + redirectUrl;
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam("productId") Long productId,
                                 @RequestParam("quantity") int quantity,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        cartService.updateQuantity(session, productId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Cart updated successfully!");
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeItem(@PathVariable("productId") Long productId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        cartService.removeFromCart(session, productId);
        redirectAttributes.addFlashAttribute("infoMessage", "Item removed from cart.");
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        cartService.clearCart(session);
        redirectAttributes.addFlashAttribute("infoMessage", "Your cart has been cleared.");
        return "redirect:/cart";
    }
}
