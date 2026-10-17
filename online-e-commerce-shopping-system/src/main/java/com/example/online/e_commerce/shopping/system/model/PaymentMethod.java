package com.example.online.e_commerce.shopping.system.model;

public enum PaymentMethod {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    UPI("UPI / Google Pay / PhonePe"),
    NET_BANKING("Net Banking"),
    CASH_ON_DELIVERY("Cash on Delivery (COD)");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
