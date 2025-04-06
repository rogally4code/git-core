package com.corebanking.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rekening")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rekening {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nomor_rekening", unique = true, nullable = false)
    private String nomorRekening;
    
    @ManyToOne
    @JoinColumn(name = "nasabah_id", nullable = false)
    private Nasabah nasabah;
    
    @Column(name = "jenis_rekening", nullable = false)
    private String jenisRekening;
    
    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "tanggal_pembukaan", nullable = false)
    private LocalDateTime tanggalPembukaan;
    
    @Column(name = "tanggal_update")
    private LocalDateTime tanggalUpdate;
    
    @PrePersist
    protected void onCreate() {
        tanggalPembukaan = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        tanggalUpdate = LocalDateTime.now();
    }
} 