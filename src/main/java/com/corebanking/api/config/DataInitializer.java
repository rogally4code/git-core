package com.corebanking.api.config;

import com.corebanking.api.model.Nasabah;
import com.corebanking.api.model.Rekening;
import com.corebanking.api.model.Transaksi;
import com.corebanking.api.repository.NasabahRepository;
import com.corebanking.api.repository.RekeningRepository;
import com.corebanking.api.repository.TransaksiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final NasabahRepository nasabahRepository;
    private final RekeningRepository rekeningRepository;
    private final TransaksiRepository transaksiRepository;

    @Bean
    @Profile("!prod")
    public CommandLineRunner initData() {
        return args -> {
            if (nasabahRepository.count() > 0) {
                log.info("Data dummy sudah ada, tidak perlu menginisialisasi lagi.");
                return;
            }
            
            log.info("Menginisialisasi data dummy...");
            
            // Buat beberapa nasabah
            Nasabah nasabah1 = createNasabah("NSB-20250406ABC12", "Budi Santoso", "3174011234567890", 
                    LocalDateTime.of(1985, 5, 15, 0, 0), 
                    "Jl. Sudirman No. 123, Jakarta", "081234567890", "budi.santoso@example.com");
            
            Nasabah nasabah2 = createNasabah("NSB-20250406DEF34", "Siti Rahayu", "3174022345678901", 
                    LocalDateTime.of(1990, 8, 23, 0, 0), 
                    "Jl. Thamrin No. 45, Jakarta", "082345678901", "siti.rahayu@example.com");
            
            Nasabah nasabah3 = createNasabah("NSB-20250406GHI56", "Ahmad Hidayat", "3174033456789012", 
                    LocalDateTime.of(1978, 3, 10, 0, 0), 
                    "Jl. Gatot Subroto No. 67, Jakarta", "083456789012", "ahmad.hidayat@example.com");
            
            List<Nasabah> nasabahList = nasabahRepository.saveAll(List.of(nasabah1, nasabah2, nasabah3));
            log.info("Berhasil membuat {} data nasabah", nasabahList.size());
            
            // Buat rekening untuk masing-masing nasabah
            Rekening rekening1 = createRekening("20250406123456", nasabah1, "TABUNGAN", new BigDecimal("5000000"));
            Rekening rekening2 = createRekening("20250406234567", nasabah2, "TABUNGAN", new BigDecimal("7500000"));
            Rekening rekening3 = createRekening("20250406345678", nasabah3, "GIRO", new BigDecimal("15000000"));
            Rekening rekening4 = createRekening("20250406456789", nasabah1, "DEPOSITO", new BigDecimal("10000000"));
            
            List<Rekening> rekeningList = rekeningRepository.saveAll(List.of(rekening1, rekening2, rekening3, rekening4));
            log.info("Berhasil membuat {} data rekening", rekeningList.size());
            
            // Buat beberapa transaksi
            Transaksi transaksi1 = createTransaksi("TRX-20250406120000-ABC12", rekening1, "SETOR", 
                    new BigDecimal("1000000"), new BigDecimal("4000000"), new BigDecimal("5000000"), 
                    "Setoran awal");
            
            Transaksi transaksi2 = createTransaksi("TRX-20250406130000-DEF34", rekening2, "SETOR", 
                    new BigDecimal("2500000"), new BigDecimal("5000000"), new BigDecimal("7500000"), 
                    "Setoran awal");
            
            Transaksi transaksi3 = createTransaksi("TRX-20250406140000-GHI56", rekening1, "TARIK", 
                    new BigDecimal("500000"), new BigDecimal("5000000"), new BigDecimal("4500000"), 
                    "Penarikan tunai");
            
            Transaksi transaksi4 = createTransaksi("TRX-20250406150000-JKL78", rekening3, "SETOR", 
                    new BigDecimal("5000000"), new BigDecimal("10000000"), new BigDecimal("15000000"), 
                    "Setoran awal");
            
            Transaksi transaksi5 = createTransaksi("TRX-20250406160000-MNO90", rekening4, "SETOR", 
                    new BigDecimal("10000000"), new BigDecimal("0"), new BigDecimal("10000000"), 
                    "Setoran awal deposito");
            
            List<Transaksi> transaksiList = transaksiRepository.saveAll(
                    List.of(transaksi1, transaksi2, transaksi3, transaksi4, transaksi5));
            log.info("Berhasil membuat {} data transaksi", transaksiList.size());
            
            log.info("Inisialisasi data dummy selesai");
        };
    }
    
    private Nasabah createNasabah(String nomorNasabah, String namaLengkap, String nomorKtp, 
                                 LocalDateTime tanggalLahir, String alamat, String nomorTelepon, String email) {
        Nasabah nasabah = new Nasabah();
        nasabah.setNomorNasabah(nomorNasabah);
        nasabah.setNamaLengkap(namaLengkap);
        nasabah.setNomorKtp(nomorKtp);
        nasabah.setTanggalLahir(tanggalLahir);
        nasabah.setAlamat(alamat);
        nasabah.setNomorTelepon(nomorTelepon);
        nasabah.setEmail(email);
        nasabah.setStatus("AKTIF");
        nasabah.setTanggalRegistrasi(LocalDateTime.now());
        return nasabah;
    }
    
    private Rekening createRekening(String nomorRekening, Nasabah nasabah, String jenisRekening, BigDecimal saldo) {
        Rekening rekening = new Rekening();
        rekening.setNomorRekening(nomorRekening);
        rekening.setNasabah(nasabah);
        rekening.setJenisRekening(jenisRekening);
        rekening.setSaldo(saldo);
        rekening.setStatus("AKTIF");
        rekening.setTanggalPembukaan(LocalDateTime.now());
        return rekening;
    }
    
    private Transaksi createTransaksi(String nomorReferensi, Rekening rekening, String jenisTransaksi, 
                                      BigDecimal jumlah, BigDecimal saldoSebelum, BigDecimal saldoSesudah, 
                                      String keterangan) {
        Transaksi transaksi = new Transaksi();
        transaksi.setNomorReferensi(nomorReferensi);
        transaksi.setRekening(rekening);
        transaksi.setJenisTransaksi(jenisTransaksi);
        transaksi.setJumlah(jumlah);
        transaksi.setSaldoSebelum(saldoSebelum);
        transaksi.setSaldoSesudah(saldoSesudah);
        transaksi.setKeterangan(keterangan);
        transaksi.setStatus("SUKSES");
        transaksi.setTanggalTransaksi(LocalDateTime.now().minusHours(UUID.randomUUID().getLeastSignificantBits() % 24));
        return transaksi;
    }
} 