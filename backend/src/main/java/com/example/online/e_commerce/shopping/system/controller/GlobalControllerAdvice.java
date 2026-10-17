package com.example.online.e_commerce.shopping.system.controller;

import com.example.online.e_commerce.shopping.system.dto.CartDto;
import com.example.online.e_commerce.shopping.system.model.Category;
import com.example.online.e_commerce.shopping.system.model.User;
import com.example.online.e_commerce.shopping.system.service.CartService;
import com.example.online.e_commerce.shopping.system.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    public static final String SESSION_USER_KEY = "LOGGED_IN_USER";

    private final CategoryService categoryService;
    private final CartService cartService;

    public GlobalControllerAdvice(CategoryService categoryService, CartService cartService) {
        this.categoryService = categoryService;
        this.cartService = cartService;
    }

    @ModelAttribute("allCategories")
    public List<Category> populateCategories() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("currentUser")
    public User populateCurrentUser(HttpSession session) {
        return (User) session.getAttribute(SESSION_USER_KEY);
    }

    @ModelAttribute("cartItemCount")
    public int populateCartItemCount(HttpSession session) {
        CartDto cart = cartService.getCart(session);
        return cart != null ? cart.getTotalQuantity() : 0;
    }
}
