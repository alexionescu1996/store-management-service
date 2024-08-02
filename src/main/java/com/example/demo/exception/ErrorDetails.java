package com.example.demo.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


public record ErrorDetails(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") Date timestamp,
        String message
) {
    public ErrorDetails(String message) {
        this(new Date(), message);
    }
}
