package com.tutorvirtual.tutorvirtual_backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception e) {
        System.err.println("❌ CRITICAL ERROR: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.internalServerError()
                .body(Map.of(
                        "error", "Internal Server Error",
                        "message", e.getMessage() != null ? e.getMessage() : "Unknown error",
                        "type", e.getClass().getSimpleName()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeExceptions(RuntimeException e) {
        System.err.println("⚠️ RUNTIME ERROR: " + e.getMessage());
        return ResponseEntity.badRequest()
                .body(Map.of(
                        "error", "Runtime Error",
                        "message", e.getMessage()));
    }
}
