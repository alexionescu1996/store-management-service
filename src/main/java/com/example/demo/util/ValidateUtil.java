package com.example.demo.util;

public class ValidateUtil {

    public static void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID: " + id);
        }
    }
}
