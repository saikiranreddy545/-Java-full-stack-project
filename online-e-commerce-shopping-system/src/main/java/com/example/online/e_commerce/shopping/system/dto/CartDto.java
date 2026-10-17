package com.example.online.e_commerce.shopping.system.dto;

import com.example.online.e_commerce.shopping.system.model.Product;
import java.util.ArrayList;
import java.util.List;

public class CartDto {
    private List<CartItemDto> items = new ArrayList<>();

    public CartDto() {
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

    public void addItem(Product product, int quantity) {
        for (CartItemDto item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItemDto(product, quantity));
    }

    public void updateQuantity(Long productId, int quantity) {
        if (quantity <= 0) {
            removeItem(productId);
            return;
        }
        for (CartItemDto item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clear() {
        items.clear();
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItemDto::getQuantity).sum();
    }

    public double getSubtotal() {
        return items.stream().mapToDouble(CartItemDto::getSubtotal).sum();
    }

    public double getShippingFee() {
        // Free shipping if order > $50, else $5
        if (getSubtotal() == 0.0) {
            return 0.0;
        }
        return getSubtotal() >= 50.0 ? 0.0 : 5.0;
    }

    public double getTax() {
        return Math.round((getSubtotal() * 0.05) * 100.0) / 100.0; // 5% tax
    }

    public double getTotalAmount() {
        return Math.round((getSubtotal() + getShippingFee() + getTax()) * 100.0) / 100.0;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
