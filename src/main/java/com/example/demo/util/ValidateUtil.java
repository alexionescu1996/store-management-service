package com.example.demo.util;

public class ValidateUtil {

    public static void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID: " + id);
        }
    }

    public static void validatePrice(Double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Invalid price: " + price + ". Price must be greater than zero.");
        }
    }
}
