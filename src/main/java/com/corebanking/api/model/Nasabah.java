package com.corebanking.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "nasabah")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nasabah {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nomor_nasabah", unique = true, nullable = false)
    private String nomorNasabah;
    
    @Column(name = "nama_lengkap", nullable = false)
    private String namaLengkap;
    
    @Column(name = "nomor_ktp", unique = true, nullable = false)
    private String nomorKtp;
    
    @Column(name = "tanggal_lahir", nullable = false)
    private LocalDateTime tanggalLahir;
    
    @Column(name = "alamat", nullable = false)
    private String alamat;
    
    @Column(name = "nomor_telepon", nullable = false)
    private String nomorTelepon;
    
    @Column(name = "email", unique = true)
    private String email;
    
    @Column(name = "tanggal_registrasi")
    private LocalDateTime tanggalRegistrasi;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @PrePersist
    protected void onCreate() {
        tanggalRegistrasi = LocalDateTime.now();
    }
} 