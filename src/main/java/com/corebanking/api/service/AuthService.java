package com.corebanking.api.service;

import com.corebanking.api.model.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String keycloakIssuerUri;
    
    public ResponseEntity<Map> login(LoginRequest loginRequest) {
        try {
            String tokenEndpoint = keycloakIssuerUri + "/protocol/openid-connect/token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", "core-banking-client");
            map.add("username", loginRequest.getUsername());
            map.add("password", loginRequest.getPassword());
            map.add("grant_type", "password");
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                tokenEndpoint,
                HttpMethod.POST,
                request,
                Map.class
            );
            
            log.info("Login berhasil untuk pengguna: {}", loginRequest.getUsername());
            return response;
            
        } catch (Exception e) {
            log.error("Login gagal untuk pengguna: {}, error: {}", loginRequest.getUsername(), e.getMessage());
            throw new RuntimeException("Autentikasi gagal: " + e.getMessage());
        }
    }
    
    public ResponseEntity<Map> refreshToken(String refreshToken) {
        try {
            String tokenEndpoint = keycloakIssuerUri + "/protocol/openid-connect/token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", "core-banking-client");
            map.add("refresh_token", refreshToken);
            map.add("grant_type", "refresh_token");
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                tokenEndpoint,
                HttpMethod.POST,
                request,
                Map.class
            );
            
            log.info("Token berhasil diperbarui");
            return response;
            
        } catch (Exception e) {
            log.error("Gagal memperbarui token: {}", e.getMessage());
            throw new RuntimeException("Gagal memperbarui token: " + e.getMessage());
        }
    }
    
    public ResponseEntity<Void> logout(String refreshToken) {
        try {
            String logoutEndpoint = keycloakIssuerUri + "/protocol/openid-connect/logout";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", "core-banking-client");
            map.add("refresh_token", refreshToken);
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            
            ResponseEntity<Void> response = restTemplate.exchange(
                logoutEndpoint,
                HttpMethod.POST,
                request,
                Void.class
            );
            
            log.info("Logout berhasil");
            return response;
            
        } catch (Exception e) {
            log.error("Logout gagal: {}", e.getMessage());
            throw new RuntimeException("Logout gagal: " + e.getMessage());
        }
    }
} 