package com.hainly;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        LocalDate today = LocalDate.now();
        if (expiryDate.isBefore(today)) {
            return -ChronoUnit.DAYS.between(expiryDate, today);
        } else {
            return ChronoUnit.DAYS.between(today, expiryDate);
        }
    }

    public String getExpiryDisplay() {
        long days = getDaysUntilExpiry();
        if (days < 0) {
            return "Expired " + Math.abs(days) + " days ago";
        } else if (days == 0) {
            return "Expires today";
        } else if (days == 1) {
            return "Expires tomorrow";
        } else {
            return days + " days until expiry";
        }
    }

    @Override
    public String toString() {
        return name + " (" + quantity + " units) - " + getExpiryDisplay();
    }
} 