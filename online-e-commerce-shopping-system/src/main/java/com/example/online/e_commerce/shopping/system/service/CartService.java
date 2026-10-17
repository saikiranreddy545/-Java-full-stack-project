package com.example.online.e_commerce.shopping.system.service;

import com.example.online.e_commerce.shopping.system.dto.CartDto;
import com.example.online.e_commerce.shopping.system.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    public static final String SESSION_CART_KEY = "SHOPPING_CART";

    private final ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
    }

    public CartDto getCart(HttpSession session) {
        CartDto cart = (CartDto) session.getAttribute(SESSION_CART_KEY);
        if (cart == null) {
            cart = new CartDto();
            session.setAttribute(SESSION_CART_KEY, cart);
        }
        return cart;
    }

    public boolean addToCart(HttpSession session, Long productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        Optional<Product> prodOpt = productService.getProductById(productId);
        if (prodOpt.isPresent()) {
            Product product = prodOpt.get();
            if (product.isInStock()) {
                CartDto cart = getCart(session);
                cart.addItem(product, quantity);
                session.setAttribute(SESSION_CART_KEY, cart);
                return true;
            }
        }
        return false;
    }

    public void updateQuantity(HttpSession session, Long productId, int quantity) {
        CartDto cart = getCart(session);
        cart.updateQuantity(productId, quantity);
        session.setAttribute(SESSION_CART_KEY, cart);
    }

    public void removeFromCart(HttpSession session, Long productId) {
        CartDto cart = getCart(session);
        cart.removeItem(productId);
        session.setAttribute(SESSION_CART_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        CartDto cart = getCart(session);
        cart.clear();
        session.setAttribute(SESSION_CART_KEY, cart);
    }
}
