package com.corebanking.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "Username tidak boleh kosong")
    private String username;
    
    @NotBlank(message = "Password tidak boleh kosong")
    private String password;
} 