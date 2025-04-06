package com.corebanking.api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NasabahRequest {
    
    @NotBlank(message = "Nama lengkap tidak boleh kosong")
    private String namaLengkap;
    
    @NotBlank(message = "Nomor KTP tidak boleh kosong")
    @Pattern(regexp = "\\d{16}", message = "Nomor KTP harus terdiri dari 16 digit angka")
    private String nomorKtp;
    
    @NotNull(message = "Tanggal lahir tidak boleh kosong")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime tanggalLahir;
    
    @NotBlank(message = "Alamat tidak boleh kosong")
    private String alamat;
    
    @NotBlank(message = "Nomor telepon tidak boleh kosong")
    @Pattern(regexp = "^(\\+62|62|0)\\d{9,12}$", message = "Format nomor telepon tidak valid")
    private String nomorTelepon;
    
    @Email(message = "Format email tidak valid")
    private String email;
} 