package com.corebanking.api.controller;

import com.corebanking.api.model.Nasabah;
import com.corebanking.api.model.Rekening;
import com.corebanking.api.model.dto.ApiResponse;
import com.corebanking.api.model.dto.NasabahRequest;
import com.corebanking.api.service.NasabahService;
import com.corebanking.api.service.RekeningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nasabah")
@RequiredArgsConstructor
@Tag(name = "Nasabah", description = "API untuk manajemen data nasabah")
@SecurityRequirement(name = "bearerAuth")
public class NasabahController {
    
    private final NasabahService nasabahService;
    private final RekeningService rekeningService;
    
    @GetMapping
    @Operation(summary = "Dapatkan semua nasabah", description = "Endpoint untuk mendapatkan daftar semua nasabah")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Nasabah>>> getAllNasabah() {
        List<Nasabah> nasabahList = nasabahService.getAllNasabah();
        return ResponseEntity.ok(ApiResponse.sukses("Daftar nasabah berhasil diambil", nasabahList));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Dapatkan nasabah berdasarkan ID", description = "Endpoint untuk mendapatkan detail nasabah berdasarkan ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<Nasabah>> getNasabahById(@PathVariable Long id) {
        Nasabah nasabah = nasabahService.getNasabahById(id);
        return ResponseEntity.ok(ApiResponse.sukses("Nasabah berhasil ditemukan", nasabah));
    }
    
    @GetMapping("/nomor/{nomorNasabah}")
    @Operation(summary = "Dapatkan nasabah berdasarkan nomor", description = "Endpoint untuk mendapatkan detail nasabah berdasarkan nomor nasabah")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<Nasabah>> getNasabahByNomor(@PathVariable String nomorNasabah) {
        Nasabah nasabah = nasabahService.getNasabahByNomorNasabah(nomorNasabah);
        return ResponseEntity.ok(ApiResponse.sukses("Nasabah berhasil ditemukan", nasabah));
    }
    
    @PostMapping
    @Operation(summary = "Daftarkan nasabah baru", description = "Endpoint untuk mendaftarkan nasabah baru")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Nasabah>> createNasabah(@Valid @RequestBody NasabahRequest request) {
        Nasabah nasabah = nasabahService.createNasabah(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sukses("Nasabah berhasil didaftarkan", nasabah));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Perbarui data nasabah", description = "Endpoint untuk memperbarui data nasabah")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Nasabah>> updateNasabah(
            @PathVariable Long id, @Valid @RequestBody NasabahRequest request) {
        Nasabah nasabah = nasabahService.updateNasabah(id, request);
        return ResponseEntity.ok(ApiResponse.sukses("Nasabah berhasil diperbarui", nasabah));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Nonaktifkan nasabah", description = "Endpoint untuk menonaktifkan nasabah")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateNasabah(@PathVariable Long id) {
        nasabahService.deactivateNasabah(id);
        return ResponseEntity.ok(ApiResponse.sukses("Nasabah berhasil dinonaktifkan", null));
    }
    
    @GetMapping("/{nasabahId}/rekening")
    @Operation(summary = "Dapatkan rekening nasabah", description = "Endpoint untuk mendapatkan semua rekening milik nasabah")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<List<Rekening>>> getNasabahRekening(@PathVariable Long nasabahId) {
        List<Rekening> rekeningList = rekeningService.getRekeningByNasabahId(nasabahId);
        return ResponseEntity.ok(ApiResponse.sukses("Daftar rekening nasabah berhasil diambil", rekeningList));
    }
} 