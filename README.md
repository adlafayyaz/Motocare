# MotoCare

MotoCare adalah aplikasi Android sederhana untuk mencatat perawatan dan pengeluaran motor, seperti servis, ganti oli, bensin, dan pajak/STNK. Aplikasi ini dibuat sebagai proyek akhir mata kuliah Pemrograman Mobile / Pemrograman Perangkat Bergerak.

## Deskripsi

Banyak pengguna motor sering lupa kapan terakhir servis, ganti oli, atau membayar pajak kendaraan. MotoCare membantu pengguna mencatat semua informasi tersebut secara lebih rapi dalam satu aplikasi.

Aplikasi ini bersifat personal. Login menggunakan Google, sedangkan data utama kendaraan tetap disimpan secara lokal di perangkat pengguna.

## Fitur Utama

* Onboarding pengenalan fitur aplikasi
* Login menggunakan Google
* Setup awal data motor
* Setup opsional servis, oli, dan pajak
* Mengelola beberapa motor
* Memilih motor aktif untuk dashboard
* Mencatat riwayat servis
* Mencatat jadwal dan riwayat ganti oli
* Mencatat pengeluaran bensin
* Mengambil harga bensin terbaru dari API hargaBensin
* Mencatat informasi pajak/STNK
* Melihat ringkasan pengeluaran bulanan
* Menghitung estimasi servis dan oli berikutnya dari kilometer serta interval bulan
* Melihat riwayat servis, oli, pajak, dan bensin dengan tab
* Mengubah dan menghapus data catatan
* Mengelola profil, pengaturan, backup/export, dan tentang aplikasi

## Teknologi yang Digunakan

* Kotlin
* Android Studio
* Gradle
* XML Layout
* SQLite / Room Database
* RecyclerView
* Intent & Activity
* Firebase Authentication untuk Google login
* Material Vector Drawable untuk icon
* API hargaBensin untuk harga BBM

## Database Overview

Aplikasi ini menggunakan database lokal untuk menyimpan data motor dan riwayat pengeluaran kendaraan.

| Tabel             | Fungsi                               |
| ----------------- | ------------------------------------ |
| `users`           | Menyimpan profil pengguna login       |
| `motors`          | Menyimpan data motor pengguna         |
| `service_records` | Menyimpan riwayat servis motor        |
| `oil_records`     | Menyimpan catatan ganti oli           |
| `fuel_records`    | Menyimpan catatan pengeluaran bensin  |
| `tax_records`     | Menyimpan informasi pajak/STNK        |

### Struktur Data Utama

#### `motors`

| Field              | Keterangan               |
| ------------------ | ------------------------ |
| `id`               | ID motor                 |
| `name`             | Nama atau tipe motor     |
| `plateNumber`      | Nomor plat motor         |
| `currentKilometer` | Kilometer motor saat ini |
| `isActive`         | Status motor aktif       |

#### `service_records`

| Field         | Keterangan            |
| ------------- | --------------------- |
| `id`          | ID riwayat servis     |
| `motorId`     | ID motor terkait      |
| `serviceDate` | Tanggal servis        |
| `serviceType` | Jenis servis          |
| `kilometer`   | Kilometer saat servis |
| `intervalKm`  | Interval servis KM    |
| `intervalMonth` | Interval servis bulan |
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
| `intervalKm`    | Interval oli KM                         |
| `intervalMonth` | Interval oli bulan                      |
| `oilType`       | Jenis oli                               |
| `cost`          | Biaya ganti oli                         |

#### `fuel_records`

| Field       | Keterangan                |
| ----------- | ------------------------- |
| `id`        | ID catatan bensin         |
| `motorId`   | ID motor terkait          |
| `fuelDate`  | Tanggal isi bensin        |
| `fuelType`  | Jenis BBM                 |
| `fuelBrand` | Merek BBM                 |
| `octane`    | Oktan BBM                 |
| `pricePerLiter` | Harga per liter       |
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
2. Splash mengecek status login.
3. Jika belum login, pengguna melihat onboarding.
4. Pengguna masuk menggunakan Google.
5. Pengguna mengisi setup motor pertama.
6. Pengguna dapat mengisi setup servis, oli, dan pajak atau melewatinya.
7. Dashboard menampilkan motor aktif, pengeluaran bulanan, dan jadwal berikutnya.
8. Pengguna dapat mengganti motor aktif dari halaman motor.
9. Tombol `+` digunakan untuk mencatat servis, oli, bensin, pajak, atau motor.
10. Riwayat ditampilkan dengan tab servis, oli, pajak, dan bensin.
11. Aplikasi dapat mengambil harga BBM terbaru untuk membantu input bensin.
12. Data tersimpan di database lokal dan tetap bisa dipakai offline.
13. Pengguna dapat membuka profil, pengaturan, backup/export, dan tentang aplikasi.

## Aturan Estimasi Servis dan Oli

Servis dan oli memakai dua patokan:

* Kilometer
* Bulan

Aplikasi menampilkan estimasi yang lebih dulu tercapai di dashboard dan riwayat.

Default awal:

| Jenis  | Kilometer | Waktu   |
| ------ | --------- | ------- |
| Servis | 3.000 km  | 3 bulan |
| Oli    | 2.000 km  | 2 bulan |

## API Eksternal

Aplikasi menggunakan repo `https://github.com/alifmaulidanar/hargaBensin` untuk mengambil harga BBM realtime.

Endpoint utama:

```text
https://api.alifmaulidanar.my.id/api-bbm/full
https://api.alifmaulidanar.my.id/api-bbm/{jenis}/{merek}/{oktan}
```

API hanya digunakan untuk harga bensin. Jika API gagal, pengguna tetap bisa memasukkan harga secara manual.

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
