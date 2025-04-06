package com.corebanking.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private boolean sukses;
    private String pesan;
    private T data;
    private LocalDateTime timestamp = LocalDateTime.now();
    
    public static <T> ApiResponse<T> sukses(String pesan, T data) {
        return new ApiResponse<>(true, pesan, data, LocalDateTime.now());
    }
    
    public static <T> ApiResponse<T> gagal(String pesan) {
        return new ApiResponse<>(false, pesan, null, LocalDateTime.now());
    }
} 