package com.corebanking.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaksi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaksi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nomor_referensi", unique = true, nullable = false)
    private String nomorReferensi;
    
    @ManyToOne
    @JoinColumn(name = "rekening_id", nullable = false)
    private Rekening rekening;
    
    @Column(name = "jenis_transaksi", nullable = false)
    private String jenisTransaksi;
    
    @Column(name = "jumlah", nullable = false)
    private BigDecimal jumlah;
    
    @Column(name = "saldo_sebelum")
    private BigDecimal saldoSebelum;
    
    @Column(name = "saldo_sesudah")
    private BigDecimal saldoSesudah;
    
    @Column(name = "keterangan")
    private String keterangan;
    
    @Column(name = "tanggal_transaksi", nullable = false)
    private LocalDateTime tanggalTransaksi;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @PrePersist
    protected void onCreate() {
        tanggalTransaksi = LocalDateTime.now();
    }
} 