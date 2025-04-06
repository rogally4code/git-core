package com.corebanking.api.repository;

import com.corebanking.api.model.Rekening;
import com.corebanking.api.model.Transaksi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {
    
    Optional<Transaksi> findByNomorReferensi(String nomorReferensi);
    
    List<Transaksi> findByRekening(Rekening rekening);
    
    List<Transaksi> findByRekeningId(Long rekeningId);
    
    Page<Transaksi> findByRekeningNomorRekening(String nomorRekening, Pageable pageable);
    
    List<Transaksi> findByRekeningNomorRekeningAndTanggalTransaksiBetween(
            String nomorRekening, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT t FROM Transaksi t WHERE t.rekening.id = :rekeningId " +
           "AND t.tanggalTransaksi BETWEEN :startDate AND :endDate " +
           "ORDER BY t.tanggalTransaksi DESC")
    List<Transaksi> findTransaksiByRekeningAndDateRange(
            Long rekeningId, LocalDateTime startDate, LocalDateTime endDate);
} 