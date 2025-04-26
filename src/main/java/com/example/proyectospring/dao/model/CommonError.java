package com.example.proyectospring.dao.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommonError {
    private final int errorId;
    private final String message;

    private final LocalDateTime date;

    public CommonError(int errorId, String message) {
        this.errorId = errorId;
        this.message = message;
        this.date = LocalDateTime.now();
    }
}