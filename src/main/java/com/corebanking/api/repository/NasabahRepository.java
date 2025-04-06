package com.corebanking.api.repository;

import com.corebanking.api.model.Nasabah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NasabahRepository extends JpaRepository<Nasabah, Long> {
    
    Optional<Nasabah> findByNomorNasabah(String nomorNasabah);
    
    Optional<Nasabah> findByNomorKtp(String nomorKtp);
    
    Optional<Nasabah> findByEmail(String email);
    
    boolean existsByNomorKtp(String nomorKtp);
    
    boolean existsByEmail(String email);
} 