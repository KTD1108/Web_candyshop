package com.candyshop.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
// Lớp này xử lý các ngoại lệ (exception) phát sinh trên toàn bộ ứng dụng một cách tập trung.
public class GlobalExceptionHandler {

    // Xử lý ngoại lệ khi các tham số phương thức không hợp lệ (ví dụ: lỗi @Valid trong DTO).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
        return ResponseEntity.badRequest().body(Map.of("error", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    // Xử lý các ngoại lệ RuntimeException chung.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
    }

    // Xử lý các ngoại lệ liên quan đến bảo mật (SecurityException).
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handleSecurity(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ex.getMessage()));
    }
}
