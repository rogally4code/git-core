package com.corebanking.api.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaksiRequest {
    
    @NotBlank(message = "Nomor rekening tidak boleh kosong")
    private String nomorRekening;
    
    @NotBlank(message = "Jenis transaksi tidak boleh kosong")
    private String jenisTransaksi;
    
    @NotNull(message = "Jumlah transaksi tidak boleh kosong")
    @DecimalMin(value = "1.0", message = "Jumlah transaksi minimal 1.0")
    private BigDecimal jumlah;
    
    private String keterangan;
} 