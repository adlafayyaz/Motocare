# MotoCare

MotoCare adalah aplikasi Android sederhana untuk mencatat perawatan dan pengeluaran motor, seperti servis, ganti oli, bensin, dan pajak/STNK. Aplikasi ini dibuat sebagai proyek akhir mata kuliah Pemrograman Mobile / Pemrograman Perangkat Bergerak.

## Deskripsi

Banyak pengguna motor sering lupa kapan terakhir servis, ganti oli, atau membayar pajak kendaraan. MotoCare membantu pengguna mencatat semua informasi tersebut secara lebih rapi dalam satu aplikasi.

Aplikasi ini bersifat personal dan menggunakan database lokal, sehingga data tersimpan di perangkat pengguna.

## Fitur Utama

* Mencatat data motor
* Mencatat riwayat servis
* Mencatat jadwal ganti oli
* Mencatat pengeluaran bensin
* Mencatat informasi pajak/STNK
* Melihat estimasi pengeluaran motor
* Mengubah dan menghapus data catatan

## Teknologi yang Digunakan

* Kotlin
* Android Studio
* Gradle
* XML Layout
* SQLite / Room Database
* RecyclerView
* Intent & Activity

## Database Overview

Aplikasi ini menggunakan database lokal untuk menyimpan data motor dan riwayat pengeluaran kendaraan.

| Tabel             | Fungsi                               |
| ----------------- | ------------------------------------ |
| `motors`          | Menyimpan data motor pengguna        |
| `service_records` | Menyimpan riwayat servis motor       |
| `oil_records`     | Menyimpan catatan ganti oli          |
| `fuel_records`    | Menyimpan catatan pengeluaran bensin |
| `tax_records`     | Menyimpan informasi pajak/STNK       |

### Struktur Data Utama

#### `motors`

| Field              | Keterangan               |
| ------------------ | ------------------------ |
| `id`               | ID motor                 |
| `name`             | Nama atau tipe motor     |
| `plateNumber`      | Nomor plat motor         |
| `brand`            | Merek motor              |
| `currentKilometer` | Kilometer motor saat ini |

#### `service_records`

| Field         | Keterangan            |
| ------------- | --------------------- |
| `id`          | ID riwayat servis     |
| `motorId`     | ID motor terkait      |
| `serviceDate` | Tanggal servis        |
| `serviceType` | Jenis servis          |
| `kilometer`   | Kilometer saat servis |
| `cost`        | Biaya servis          |
| `note`        | Catatan tambahan      |

#### `oil_records`

| Field           | Keterangan                              |
| --------------- | --------------------------------------- |
| `id`            | ID catatan oli                          |
| `motorId`       | ID motor terkait                        |
| `oilChangeDate` | Tanggal ganti oli                       |
| `kilometer`     | Kilometer saat ganti oli                |
| `nextKilometer` | Estimasi kilometer ganti oli berikutnya |
| `oilType`       | Jenis oli                               |
| `cost`          | Biaya ganti oli                         |

#### `fuel_records`

| Field       | Keterangan                |
| ----------- | ------------------------- |
| `id`        | ID catatan bensin         |
| `motorId`   | ID motor terkait          |
| `fuelDate`  | Tanggal isi bensin        |
| `fuelType`  | Jenis bensin              |
| `liter`     | Jumlah liter bensin       |
| `cost`      | Biaya bensin              |
| `kilometer` | Kilometer saat isi bensin |

#### `tax_records`

| Field      | Keterangan                |
| ---------- | ------------------------- |
| `id`       | ID catatan pajak          |
| `motorId`  | ID motor terkait          |
| `dueDate`  | Tanggal jatuh tempo pajak |
| `taxCost`  | Biaya pajak               |
| `status`   | Status pembayaran         |
| `paidDate` | Tanggal pembayaran pajak  |

## Alur Aplikasi

1. Pengguna membuka aplikasi.
2. Pengguna masuk ke halaman utama.
3. Pengguna menambahkan data motor.
4. Pengguna mencatat servis, ganti oli, bensin, atau pajak.
5. Data tersimpan di database lokal.
6. Pengguna dapat melihat, mengubah, dan menghapus data.
7. Aplikasi menampilkan ringkasan pengeluaran motor.

## Cara Menjalankan Project

1. Clone repository ini.

```bash
git clone https://github.com/adlafayyaz/Motocare.git
```

2. Buka project menggunakan Android Studio.

3. Tunggu proses Gradle Sync selesai.

4. Jalankan aplikasi menggunakan emulator atau perangkat Android.

## Anggota Kelompok

| Nama                              | NIM             | Role                                     |
| --------------------------------- | --------------- | ---------------------------------------- |
| Khaliz Kanigara Fathi Gunawan     | 2410512151      | Project Manager & Lead Backend Developer |
| Fathi Muhammad Luthfi Cardiana    | 2410512142      | UI/UX Designer & Frontend Developer      |
| M. Adla Fayyaz Fauzy              | 2410512154      | Frontend Developer - List & Navigation   |
| Rafid Abdan Syakur                | 2410512141      | Backend Developer                        |
| M. Syauqi Rabbani                 | 2410512166      | System Analyst                           |
| Ananta Jordan Surya Putra Ginting | 2410512164      | Quality Assurance & Technical Writer     |
