package com.corebanking.api.service;

import com.corebanking.api.model.Rekening;
import com.corebanking.api.model.Transaksi;
import com.corebanking.api.model.dto.TransaksiRequest;
import com.corebanking.api.repository.TransaksiRepository;
import com.corebanking.api.messaging.RabbitMQSender;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransaksiService {
    
    private final TransaksiRepository transaksiRepository;
    private final RekeningService rekeningService;
    private final RabbitMQSender rabbitMQSender;
    
    public List<Transaksi> getAllTransaksi() {
        return transaksiRepository.findAll();
    }
    
    public Transaksi getTransaksiById(Long id) {
        return transaksiRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaksi dengan ID " + id + " tidak ditemukan"));
    }
    
    public Transaksi getTransaksiByNomorReferensi(String nomorReferensi) {
        return transaksiRepository.findByNomorReferensi(nomorReferensi)
                .orElseThrow(() -> new EntityNotFoundException("Transaksi dengan nomor referensi " + nomorReferensi + " tidak ditemukan"));
    }
    
    public Page<Transaksi> getTransaksiByRekening(String nomorRekening, Pageable pageable) {
        return transaksiRepository.findByRekeningNomorRekening(nomorRekening, pageable);
    }
    
    public List<Transaksi> getTransaksiByRekeningAndDateRange(
            String nomorRekening, LocalDateTime startDate, LocalDateTime endDate) {
        return transaksiRepository.findByRekeningNomorRekeningAndTanggalTransaksiBetween(
                nomorRekening, startDate, endDate);
    }
    
    @Transactional
    public Transaksi processTransaksi(TransaksiRequest request) {
        String nomorRekening = request.getNomorRekening();
        String jenisTransaksi = request.getJenisTransaksi().toUpperCase();
        BigDecimal jumlah = request.getJumlah();
        
        // Validate transaction type
        if (!jenisTransaksi.equals("SETOR") && !jenisTransaksi.equals("TARIK")) {
            throw new IllegalArgumentException("Jenis transaksi tidak valid. Pilih SETOR atau TARIK.");
        }
        
        // Get account and update balance
        Rekening rekening = rekeningService.getRekeningByNomor(nomorRekening);
        BigDecimal saldoSebelum = rekening.getSaldo();
        
        Rekening updatedRekening = rekeningService.updateSaldo(nomorRekening, jumlah, jenisTransaksi);
        BigDecimal saldoSesudah = updatedRekening.getSaldo();
        
        // Create transaction record
        Transaksi transaksi = new Transaksi();
        transaksi.setNomorReferensi(generateNomorReferensi());
        transaksi.setRekening(updatedRekening);
        transaksi.setJenisTransaksi(jenisTransaksi);
        transaksi.setJumlah(jumlah);
        transaksi.setSaldoSebelum(saldoSebelum);
        transaksi.setSaldoSesudah(saldoSesudah);
        transaksi.setKeterangan(request.getKeterangan());
        transaksi.setStatus("SUKSES");
        
        Transaksi savedTransaksi = transaksiRepository.save(transaksi);
        
        // Send notification via RabbitMQ
        rabbitMQSender.sendTransaksiNotification(savedTransaksi);
        
        return savedTransaksi;
    }
    
    private String generateNomorReferensi() {
        // Format: TRX-yyyyMMddHHmmss-xxxxx (where xxxxx is random 5 chars)
        LocalDateTime now = LocalDateTime.now();
        String datePart = String.format("%d%02d%02d%02d%02d%02d", 
                now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                now.getHour(), now.getMinute(), now.getSecond());
        
        String uniquePart = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5).toUpperCase();
        
        return "TRX-" + datePart + "-" + uniquePart;
    }
} 