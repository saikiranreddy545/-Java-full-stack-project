package com.example.online.e_commerce.shopping.system.dto;

import com.example.online.e_commerce.shopping.system.model.Product;

public class CartItemDto {
    private Product product;
    private int quantity;

    public CartItemDto() {
    }

    public CartItemDto(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        if (product != null && product.getPrice() != null) {
            return product.getPrice() * quantity;
        }
        return 0.0;
    }
}
