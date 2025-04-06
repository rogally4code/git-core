package com.corebanking.api.controller;

import com.corebanking.api.model.dto.ApiResponse;
import com.corebanking.api.model.dto.LoginRequest;
import com.corebanking.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autentikasi", description = "API untuk autentikasi pengguna")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    @Operation(summary = "Login pengguna", description = "Endpoint untuk login pengguna dan mendapatkan token akses")
    public ResponseEntity<ApiResponse<Map>> login(@Valid @RequestBody LoginRequest loginRequest) {
        ResponseEntity<Map> keycloakResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.sukses("Login berhasil", keycloakResponse.getBody()));
    }
    
    @PostMapping("/refresh")
    @Operation(summary = "Perbarui token", description = "Endpoint untuk memperbarui token akses menggunakan refresh token")
    public ResponseEntity<ApiResponse<Map>> refreshToken(@RequestParam String refreshToken) {
        ResponseEntity<Map> keycloakResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.sukses("Token berhasil diperbarui", keycloakResponse.getBody()));
    }
    
    @PostMapping("/logout")
    @Operation(summary = "Logout pengguna", description = "Endpoint untuk logout pengguna")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestParam String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok(ApiResponse.sukses("Logout berhasil", null));
    }
} 