package com.example.proyectospring.domain.errors;

public class UniqueConstraintError extends RuntimeException {
    public UniqueConstraintError(String message) {
        super(message);
    }
}
