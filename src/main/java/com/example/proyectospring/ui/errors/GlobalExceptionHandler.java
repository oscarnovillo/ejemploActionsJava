package com.example.proyectospring.ui.errors;

import com.example.proyectospring.domain.errors.ApiError;
import com.example.proyectospring.domain.errors.ForeignKeyConstraintError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //Spring to have all exceptions together
    @ExceptionHandler(ForeignKeyConstraintError.class)
    //Devuelve objeto JSON con ResponseEntity
    public ResponseEntity<ApiError> handleException(ForeignKeyConstraintError e) {
        //Log
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(e.getMessage()));
    }
}
