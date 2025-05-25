package com.hainly;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class FoodItem implements Serializable {
    private String id;
    private String name;
    private int quantity;
    private LocalDate expiryDate;
    private String category;

    public FoodItem(String name, int quantity, LocalDate expiryDate, String category) {
        if (expiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiry date cannot be in the past");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        if (expiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiry date cannot be in the past");
        }
        this.expiryDate = expiryDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getDaysUntilExpiry() {
        return LocalDate.now().until(expiryDate).getDays();
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public String getExpiryStatus() {
        if (isExpired()) {
            return "EXPIRED";
        }
        long days = getDaysUntilExpiry();
        if (days <= 3) {
            return "EXPIRING SOON";
        }
        return "GOOD";
    }

    @Override
    public String toString() {
        return name + " (" + quantity + " units) - Expires: " + expiryDate + " - Status: " + getExpiryStatus();
    }
} 