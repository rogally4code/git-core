package com.corebanking.api.controller;

import com.corebanking.api.model.Transaksi;
import com.corebanking.api.model.dto.ApiResponse;
import com.corebanking.api.model.dto.TransaksiRequest;
import com.corebanking.api.service.TransaksiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transaksi")
@RequiredArgsConstructor
@Tag(name = "Transaksi", description = "API untuk transaksi perbankan")
@SecurityRequirement(name = "bearerAuth")
public class TransaksiController {
    
    private final TransaksiService transaksiService;
    
    @GetMapping
    @Operation(summary = "Dapatkan semua transaksi", description = "Endpoint untuk mendapatkan daftar semua transaksi")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Transaksi>>> getAllTransaksi() {
        List<Transaksi> transaksiList = transaksiService.getAllTransaksi();
        return ResponseEntity.ok(ApiResponse.sukses("Daftar transaksi berhasil diambil", transaksiList));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Dapatkan transaksi berdasarkan ID", description = "Endpoint untuk mendapatkan detail transaksi berdasarkan ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<Transaksi>> getTransaksiById(@PathVariable Long id) {
        Transaksi transaksi = transaksiService.getTransaksiById(id);
        return ResponseEntity.ok(ApiResponse.sukses("Transaksi berhasil ditemukan", transaksi));
    }
    
    @GetMapping("/referensi/{nomorReferensi}")
    @Operation(summary = "Dapatkan transaksi berdasarkan nomor referensi", 
            description = "Endpoint untuk mendapatkan detail transaksi berdasarkan nomor referensi")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<Transaksi>> getTransaksiByNomorReferensi(@PathVariable String nomorReferensi) {
        Transaksi transaksi = transaksiService.getTransaksiByNomorReferensi(nomorReferensi);
        return ResponseEntity.ok(ApiResponse.sukses("Transaksi berhasil ditemukan", transaksi));
    }
    
    @GetMapping("/rekening/{nomorRekening}")
    @Operation(summary = "Dapatkan transaksi berdasarkan nomor rekening", 
            description = "Endpoint untuk mendapatkan daftar transaksi berdasarkan nomor rekening (dengan pagination)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<Page<Transaksi>>> getTransaksiByRekening(
            @PathVariable String nomorRekening,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "tanggalTransaksi") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Transaksi> transaksiPage = transaksiService.getTransaksiByRekening(nomorRekening, pageRequest);
        return ResponseEntity.ok(ApiResponse.sukses("Daftar transaksi rekening berhasil diambil", transaksiPage));
    }
    
    @GetMapping("/rekening/{nomorRekening}/laporan")
    @Operation(summary = "Dapatkan laporan transaksi berdasarkan periode", 
            description = "Endpoint untuk mendapatkan laporan transaksi berdasarkan nomor rekening dan rentang waktu")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<List<Transaksi>>> getLaporanTransaksi(
            @PathVariable String nomorRekening,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<Transaksi> transaksiList = transaksiService.getTransaksiByRekeningAndDateRange(
                nomorRekening, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.sukses("Laporan transaksi berhasil diambil", transaksiList));
    }
    
    @PostMapping
    @Operation(summary = "Proses transaksi baru", description = "Endpoint untuk memproses transaksi baru (setor atau tarik)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TELLER')")
    public ResponseEntity<ApiResponse<Transaksi>> processTransaksi(@Valid @RequestBody TransaksiRequest request) {
        Transaksi transaksi = transaksiService.processTransaksi(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sukses("Transaksi berhasil diproses", transaksi));
    }
} 