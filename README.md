# Core Banking API

Aplikasi REST API untuk Core Banking dengan PostgreSQL, RabbitMQ, dan Keycloak.

## Fitur

- Autentikasi dan otorisasi menggunakan Keycloak
- Pengelolaan data nasabah
- Pengelolaan rekening nasabah
- Transaksi perbankan (setor dan tarik)
- Pelaporan transaksi
- Notifikasi transaksi menggunakan RabbitMQ
- Dokumentasi API dengan Swagger/OpenAPI

## Teknologi yang Digunakan

- Java 17
- Spring Boot 3.2.4
- Spring Data JPA
- Spring Security dengan OAuth2/JWT
- PostgreSQL untuk penyimpanan data
- RabbitMQ untuk sistem pesan/notifikasi
- Keycloak untuk manajemen identitas dan akses
- Swagger/OpenAPI untuk dokumentasi API

## Prasyarat

Sebelum menjalankan aplikasi, pastikan Anda telah menginstal:

- Java 17 atau yang lebih baru
- PostgreSQL
- RabbitMQ
- Keycloak (versi 23.x)

## Konfigurasi Keycloak

1. Buat realm baru dengan nama `core-banking`
2. Buat client dengan nama `core-banking-client`
   - Client type: Confidential
   - Service accounts enabled: Yes
   - Authorization enabled: Yes
   - Valid redirect URIs: `http://localhost:8080/*`
3. Buat roles: `ADMIN`, `USER`, dan `TELLER`
4. Buat beberapa user dan tetapkan roles yang sesuai

## Konfigurasi PostgreSQL

1. Buat database baru dengan nama `corebanking`
2. Untuk lingkungan development, aplikasi akan secara otomatis membuat tabel dan data dummy
3. Untuk lingkungan production, gunakan skema migrasi database

## Konfigurasi RabbitMQ

1. Pastikan RabbitMQ berjalan dengan port default 5672
2. Gunakan username dan password default: guest/guest (hanya untuk lingkungan development)

## Menjalankan Aplikasi

1. Clone repository ini
2. Konfigurasi aplikasi pada `src/main/resources/application.properties` sesuai dengan lingkungan Anda
3. Jalankan aplikasi dengan perintah:

```bash
./mvnw spring-boot:run
```

## API Endpoints

Setelah aplikasi berjalan, Anda dapat mengakses dokumentasi API di:

```
http://localhost:8080/swagger-ui.html
```

### Endpoint Utama:

- **Autentikasi**: `/api/auth/**`
  - `/api/auth/login` - Login untuk mendapatkan token akses
  - `/api/auth/refresh` - Memperbarui token akses
  - `/api/auth/logout` - Logout

- **Nasabah**: `/api/nasabah/**`
  - `/api/nasabah` - CRUD operasi untuk data nasabah
  - `/api/nasabah/{nasabahId}/rekening` - Mendapatkan rekening nasabah

- **Rekening**: `/api/rekening/**`
  - `/api/rekening` - CRUD operasi untuk rekening
  - `/api/rekening/nomor/{nomorRekening}` - Mendapatkan rekening berdasarkan nomor

- **Transaksi**: `/api/transaksi/**`
  - `/api/transaksi` - Membuat dan mendapatkan transaksi
  - `/api/transaksi/rekening/{nomorRekening}` - Mendapatkan transaksi rekening
  - `/api/transaksi/rekening/{nomorRekening}/laporan` - Laporan transaksi berdasarkan periode

## Lisensi

Proyek ini dilisensikan di bawah lisensi [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 