package com.corebanking.api.service;

import com.corebanking.api.model.Nasabah;
import com.corebanking.api.model.Rekening;
import com.corebanking.api.repository.NasabahRepository;
import com.corebanking.api.repository.RekeningRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RekeningService {
    
    private final RekeningRepository rekeningRepository;
    private final NasabahRepository nasabahRepository;
    
    public List<Rekening> getAllRekening() {
        return rekeningRepository.findAll();
    }
    
    public Rekening getRekeningById(Long id) {
        return rekeningRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rekening dengan ID " + id + " tidak ditemukan"));
    }
    
    public Rekening getRekeningByNomor(String nomorRekening) {
        return rekeningRepository.findByNomorRekening(nomorRekening)
                .orElseThrow(() -> new EntityNotFoundException("Rekening dengan nomor " + nomorRekening + " tidak ditemukan"));
    }
    
    public List<Rekening> getRekeningByNasabahId(Long nasabahId) {
        return rekeningRepository.findByNasabahId(nasabahId);
    }
    
    @Transactional
    public Rekening createRekening(Long nasabahId, String jenisRekening, BigDecimal setoran) {
        if (setoran.compareTo(BigDecimal.valueOf(50000)) < 0) {
            throw new IllegalArgumentException("Setoran awal minimal Rp 50.000");
        }
        
        Nasabah nasabah = nasabahRepository.findById(nasabahId)
                .orElseThrow(() -> new EntityNotFoundException("Nasabah dengan ID " + nasabahId + " tidak ditemukan"));
        
        if (!"AKTIF".equals(nasabah.getStatus())) {
            throw new IllegalArgumentException("Nasabah tidak aktif");
        }
        
        Rekening rekening = new Rekening();
        rekening.setNomorRekening(generateNomorRekening());
        rekening.setNasabah(nasabah);
        rekening.setJenisRekening(jenisRekening);
        rekening.setSaldo(setoran);
        rekening.setStatus("AKTIF");
        
        return rekeningRepository.save(rekening);
    }
    
    @Transactional
    public Rekening updateSaldo(String nomorRekening, BigDecimal jumlah, String jenisTransaksi) {
        Rekening rekening = getRekeningByNomor(nomorRekening);
        
        if (!"AKTIF".equals(rekening.getStatus())) {
            throw new IllegalArgumentException("Rekening tidak aktif");
        }
        
        BigDecimal saldoBaru;
        if ("SETOR".equals(jenisTransaksi)) {
            saldoBaru = rekening.getSaldo().add(jumlah);
        } else if ("TARIK".equals(jenisTransaksi)) {
            if (rekening.getSaldo().compareTo(jumlah) < 0) {
                throw new IllegalArgumentException("Saldo tidak mencukupi");
            }
            saldoBaru = rekening.getSaldo().subtract(jumlah);
        } else {
            throw new IllegalArgumentException("Jenis transaksi tidak valid");
        }
        
        rekening.setSaldo(saldoBaru);
        return rekeningRepository.save(rekening);
    }
    
    @Transactional
    public void closeRekening(String nomorRekening) {
        Rekening rekening = getRekeningByNomor(nomorRekening);
        
        if (rekening.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("Rekening masih memiliki saldo. Tarik semua saldo sebelum menutup rekening.");
        }
        
        rekening.setStatus("TUTUP");
        rekeningRepository.save(rekening);
    }
    
    private String generateNomorRekening() {
        // Format: YYYYMMDDxxxxxxx (where xxxxxxx is random 7 digits)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String prefix = LocalDateTime.now().format(formatter);
        
        Random random = new Random();
        int randomNum = random.nextInt(9000000) + 1000000; // 7-digit number
        
        return prefix + randomNum;
    }
} 