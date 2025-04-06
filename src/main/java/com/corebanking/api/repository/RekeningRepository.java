package com.corebanking.api.repository;

import com.corebanking.api.model.Nasabah;
import com.corebanking.api.model.Rekening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RekeningRepository extends JpaRepository<Rekening, Long> {
    
    Optional<Rekening> findByNomorRekening(String nomorRekening);
    
    List<Rekening> findByNasabah(Nasabah nasabah);
    
    List<Rekening> findByNasabahId(Long nasabahId);
    
    boolean existsByNomorRekening(String nomorRekening);
} 