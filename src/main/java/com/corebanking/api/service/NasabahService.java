package com.corebanking.api.service;

import com.corebanking.api.model.Nasabah;
import com.corebanking.api.model.dto.NasabahRequest;
import com.corebanking.api.repository.NasabahRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NasabahService {
    
    private final NasabahRepository nasabahRepository;
    
    public List<Nasabah> getAllNasabah() {
        return nasabahRepository.findAll();
    }
    
    public Nasabah getNasabahById(Long id) {
        return nasabahRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nasabah dengan ID " + id + " tidak ditemukan"));
    }
    
    public Nasabah getNasabahByNomorNasabah(String nomorNasabah) {
        return nasabahRepository.findByNomorNasabah(nomorNasabah)
                .orElseThrow(() -> new EntityNotFoundException("Nasabah dengan nomor " + nomorNasabah + " tidak ditemukan"));
    }
    
    @Transactional
    public Nasabah createNasabah(NasabahRequest request) {
        // Check if KTP number already exists
        if (nasabahRepository.existsByNomorKtp(request.getNomorKtp())) {
            throw new IllegalArgumentException("Nomor KTP sudah terdaftar");
        }
        
        // Check if email already exists
        if (request.getEmail() != null && !request.getEmail().isEmpty() && 
            nasabahRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email sudah terdaftar");
        }
        
        Nasabah nasabah = new Nasabah();
        nasabah.setNomorNasabah(generateNomorNasabah());
        nasabah.setNamaLengkap(request.getNamaLengkap());
        nasabah.setNomorKtp(request.getNomorKtp());
        nasabah.setTanggalLahir(request.getTanggalLahir());
        nasabah.setAlamat(request.getAlamat());
        nasabah.setNomorTelepon(request.getNomorTelepon());
        nasabah.setEmail(request.getEmail());
        nasabah.setStatus("AKTIF");
        
        return nasabahRepository.save(nasabah);
    }
    
    @Transactional
    public Nasabah updateNasabah(Long id, NasabahRequest request) {
        Nasabah nasabah = getNasabahById(id);
        
        nasabah.setNamaLengkap(request.getNamaLengkap());
        nasabah.setAlamat(request.getAlamat());
        nasabah.setNomorTelepon(request.getNomorTelepon());
        nasabah.setEmail(request.getEmail());
        
        return nasabahRepository.save(nasabah);
    }
    
    @Transactional
    public void deactivateNasabah(Long id) {
        Nasabah nasabah = getNasabahById(id);
        nasabah.setStatus("TIDAK AKTIF");
        nasabahRepository.save(nasabah);
    }
    
    private String generateNomorNasabah() {
        // Format: NSB-YYYYMMDDxxxxx (where xxxxx is random 5 digits)
        LocalDateTime now = LocalDateTime.now();
        String datePart = String.format("%d%02d%02d", 
                now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        
        String uniquePart = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5).toUpperCase();
        
        return "NSB-" + datePart + uniquePart;
    }
} 