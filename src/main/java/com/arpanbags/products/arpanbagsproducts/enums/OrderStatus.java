package com.arpanbags.products.arpanbagsproducts.enums;

public enum OrderStatus {
    BOOKED("Order is booked"),
    PROCESSING("Currently being processed"),
    SHIPPED("Order has been shipped"),
    DELIVERED("Delivered to customer"),
    CANCELLED("Order was cancelled");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
