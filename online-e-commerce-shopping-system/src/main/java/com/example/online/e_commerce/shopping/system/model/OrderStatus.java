package com.example.online.e_commerce.shopping.system.model;

public enum OrderStatus {
    PLACED("Placed", "badge-primary"),
    PROCESSING("Processing", "badge-info"),
    SHIPPED("Shipped", "badge-warning"),
    DELIVERED("Delivered", "badge-success"),
    CANCELLED("Cancelled", "badge-danger");

    private final String displayName;
    private final String badgeClass;

    OrderStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
