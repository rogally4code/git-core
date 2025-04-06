package com.corebanking.api.controller;

import com.corebanking.api.model.Rekening;
import com.corebanking.api.model.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.corebanking.api.service.RekeningService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/rekening")
@RequiredArgsConstructor
@Validated
@Tag(name = "Rekening", description = "API untuk manajemen rekening nasabah")
@SecurityRequirement(name = "bearerAuth")
public class RekeningController {
    
    private final RekeningService rekeningService;
    
    @GetMapping
    @Operation(summary = "Dapatkan semua rekening", description = "Endpoint untuk mendapatkan daftar semua rekening")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Rekening>>> getAllRekening() {
        List<Rekening> rekeningList = rekeningService.getAllRekening();
        return ResponseEntity.ok(ApiResponse.sukses("Daftar rekening berhasil diambil", rekeningList));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Dapatkan rekening berdasarkan ID", description = "Endpoint untuk mendapatkan detail rekening berdasarkan ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<Rekening>> getRekeningById(@PathVariable Long id) {
        Rekening rekening = rekeningService.getRekeningById(id);
        return ResponseEntity.ok(ApiResponse.sukses("Rekening berhasil ditemukan", rekening));
    }
    
    @GetMapping("/nomor/{nomorRekening}")
    @Operation(summary = "Dapatkan rekening berdasarkan nomor", description = "Endpoint untuk mendapatkan detail rekening berdasarkan nomor rekening")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<Rekening>> getRekeningByNomor(@PathVariable String nomorRekening) {
        Rekening rekening = rekeningService.getRekeningByNomor(nomorRekening);
        return ResponseEntity.ok(ApiResponse.sukses("Rekening berhasil ditemukan", rekening));
    }
    
    @PostMapping
    @Operation(summary = "Buka rekening baru", description = "Endpoint untuk membuka rekening baru bagi nasabah")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Rekening>> createRekening(
            @RequestParam @NotNull(message = "ID nasabah tidak boleh kosong") Long nasabahId,
            @RequestParam @NotBlank(message = "Jenis rekening tidak boleh kosong") String jenisRekening,
            @RequestParam @NotNull(message = "Setoran awal tidak boleh kosong") 
            @DecimalMin(value = "50000", message = "Setoran awal minimal Rp 50.000") BigDecimal setoran) {
        
        Rekening rekening = rekeningService.createRekening(nasabahId, jenisRekening, setoran);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sukses("Rekening berhasil dibuka", rekening));
    }
    
    @DeleteMapping("/tutup/{nomorRekening}")
    @Operation(summary = "Tutup rekening", description = "Endpoint untuk menutup rekening nasabah (saldo harus 0)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> closeRekening(@PathVariable String nomorRekening) {
        rekeningService.closeRekening(nomorRekening);
        return ResponseEntity.ok(ApiResponse.sukses("Rekening berhasil ditutup", null));
    }
} 