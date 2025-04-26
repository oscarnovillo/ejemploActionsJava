package com.example.proyectospring.domain.errors;

import lombok.Data;

@Data
public class ApiError {
    private final String message;

    public ApiError(String message) {
        this.message = message;
    }
}
