# MotoCare

MotoCare adalah aplikasi mobile berbasis Android yang digunakan untuk mencatat servis motor, jadwal ganti oli, pajak/STNK, pengeluaran bensin, dan estimasi biaya perawatan motor setiap bulan. Aplikasi ini dibuat sebagai proyek akhir mata kuliah Pemrograman Mobile / Pemrograman Perangkat Bergerak.

## Deskripsi

Banyak pengguna motor sering lupa kapan terakhir servis, ganti oli, atau membayar pajak kendaraan. MotoCare membantu pengguna mencatat semua informasi tersebut secara lebih rapi dalam satu aplikasi.

<<<<<<< Updated upstream
Aplikasi ini bersifat personal. Login menggunakan Google, sedangkan data utama kendaraan tetap disimpan secara lokal di perangkat pengguna. Data setiap akun dipisahkan berdasarkan akun Google yang sedang login.
=======
<img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
<img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
<img src="https://img.shields.io/badge/IDE-Android%20Studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white" />
<img src="https://img.shields.io/badge/Database-SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white" />
<img src="https://img.shields.io/badge/Auth-Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" />
>>>>>>> Stashed changes

## Fitur Utama

<<<<<<< Updated upstream
* Onboarding pengenalan fitur aplikasi
* Login menggunakan Google melalui Firebase Authentication
* Setup awal data motor
* Setup opsional servis, oli, dan pajak
* Mengelola beberapa motor
* Memilih motor aktif untuk dashboard
* Mencatat riwayat servis
* Mencatat jadwal dan riwayat ganti oli
* Mencatat pengeluaran bensin
* Mengambil harga bensin terbaru dari data harga BBM
* Mencatat informasi pajak/STNK
* Melihat ringkasan pengeluaran bulanan
* Menghitung estimasi servis dan oli berikutnya dari kilometer serta interval bulan
* Melihat riwayat servis, oli, pajak, dan bensin dengan tab
* Mengubah dan menghapus data catatan
* Backup dan import data lokal dalam format JSON
* Mengelola profil, pengaturan, dan tentang aplikasi
=======
<img src="https://img.shields.io/badge/Status-Academic%20Project-blue?style=flat-square" />
<img src="https://img.shields.io/badge/Offline-Supported-success?style=flat-square" />
<img src="https://img.shields.io/badge/Data-isibens.in-orange?style=flat-square" />
<img src="https://img.shields.io/badge/Made%20with-Kotlin-purple?style=flat-square" />
<img src="https://img.shields.io/badge/Build-Gradle-02303A?style=flat-square&logo=gradle&logoColor=white" />
>>>>>>> Stashed changes

## Teknologi yang Digunakan

* Kotlin
* Android Studio
* Gradle
* XML Layout
* SQLite Database
* RecyclerView
* Intent & Activity
* Firebase Authentication untuk Google login
* Material Vector Drawable untuk icon
* Scraping data harga BBM dari sumber harga bensin

## Database Overview

Aplikasi ini menggunakan database lokal untuk menyimpan data motor dan riwayat pengeluaran kendaraan.

| Tabel             | Fungsi                               |
| ----------------- | ------------------------------------ |
| `users`           | Menyimpan profil pengguna login      |
| `motors`          | Menyimpan data motor pengguna        |
| `service_records` | Menyimpan riwayat servis motor       |
| `oil_records`     | Menyimpan catatan ganti oli          |
| `fuel_records`    | Menyimpan catatan pengeluaran bensin |
| `tax_records`     | Menyimpan informasi pajak/STNK       |

<<<<<<< Updated upstream
## Struktur Data Utama
=======
Many motorcycle users often forget when they last serviced their vehicle, changed the oil, or paid vehicle tax. MotoCare helps users store all of this information in a more organized, structured, and accessible way.

MotoCare is designed as a personal-use application. Users can log in using Google, while the main vehicle data is stored locally on the user's device, allowing the app to remain usable even without an internet connection.

<br />

## ✨ Key Features

* 👋 **Onboarding screen** to introduce the main features of MotoCare
* 🔐 **Google Login** using Firebase Authentication
* 🏍️ **Initial motorcycle setup**
* 🛠️ **Optional service, oil, and tax setup**
* 📋 **Manage multiple motorcycles**
* ⭐ **Select an active motorcycle for the dashboard**
* 🧾 **Record motorcycle service history**
* 🛢️ **Record oil change schedules and history**
* ⛽ **Track fuel expenses**
* 🌐 **Fetch the latest fuel prices from scraped fuel price data**
* 📄 **Record tax/STNK information**
* 📊 **View monthly expense summaries**
* 📍 **Calculate estimated next service and oil change based on mileage and time interval**
* 🗂️ **View service, oil, tax, and fuel history using tabs**
* ✏️ **Edit and delete recorded data**
* ⚙️ **Manage profile, settings, backup/export, and app information**

<br />

## 🧰 Tech Stack

| Category             | Technology                               |
| -------------------- | ---------------------------------------- |
| Programming Language | Kotlin                                   |
| IDE                  | Android Studio                           |
| Build Tool           | Gradle                                   |
| User Interface       | XML Layout                               |
| Local Database       | SQLite Database                          |
| List & Data Display  | RecyclerView                             |
| Navigation           | Intent & Activity                        |
| Authentication       | Firebase Authentication for Google Login |
| Icons                | Material Vector Drawable                 |
| Fuel Price Source    | Scraped data from isibens.in             |

<br />

## 🗄️ Database Overview

MotoCare uses a local database to store motorcycle data and all vehicle-related expense records.

| Table             | Description                        |
| ----------------- | ---------------------------------- |
| `users`           | Stores logged-in user profile data |
| `motors`          | Stores motorcycle data             |
| `service_records` | Stores motorcycle service history  |
| `oil_records`     | Stores oil change records          |
| `fuel_records`    | Stores fuel expense records        |
| `tax_records`     | Stores tax/STNK information        |

<br />

## 🧱 Main Data Structure
>>>>>>> Stashed changes

### `motors`

| Field              | Keterangan               |
| ------------------ | ------------------------ |
| `id`               | ID motor                 |
| `name`             | Nama atau tipe motor     |
| `plateNumber`      | Nomor plat motor         |
| `currentKilometer` | Kilometer motor saat ini |
| `isActive`         | Status motor aktif       |

### `service_records`

| Field           | Keterangan            |
| --------------- | --------------------- |
| `id`            | ID riwayat servis     |
| `motorId`       | ID motor terkait      |
| `serviceDate`   | Tanggal servis        |
| `serviceType`   | Jenis servis          |
| `kilometer`     | Kilometer saat servis |
| `intervalKm`    | Target KM servis      |
| `intervalMonth` | Interval servis bulan |
| `cost`          | Biaya servis          |
| `note`          | Catatan tambahan      |

### `oil_records`

| Field           | Keterangan                            |
| --------------- | ------------------------------------- |
| `id`            | ID catatan oli                        |
| `motorId`       | ID motor terkait                      |
| `oilChangeDate` | Tanggal ganti oli                     |
| `kilometer`     | Kilometer saat ganti oli              |
| `nextKilometer` | Target kilometer ganti oli berikutnya |
| `intervalKm`    | Target KM oli                         |
| `intervalMonth` | Interval oli bulan                    |
| `oilType`       | Jenis oli                             |
| `cost`          | Biaya ganti oli                       |

### `fuel_records`

| Field           | Keterangan                |
| --------------- | ------------------------- |
| `id`            | ID catatan bensin         |
| `motorId`       | ID motor terkait          |
| `fuelDate`      | Tanggal isi bensin        |
| `fuelType`      | Jenis BBM                 |
| `fuelBrand`     | Merek BBM                 |
| `octane`        | Oktan BBM                 |
| `pricePerLiter` | Harga per liter           |
| `liter`         | Jumlah liter bensin       |
| `cost`          | Total biaya bensin        |
| `kilometer`     | Kilometer saat isi bensin |

### `tax_records`

<<<<<<< Updated upstream
| Field     | Keterangan                |
| --------- | ------------------------- |
| `id`      | ID catatan pajak          |
| `motorId` | ID motor terkait          |
| `taxType` | Jenis pajak               |
| `dueDate` | Tanggal jatuh tempo pajak |
| `cost`    | Biaya pajak               |
| `status`  | Status pembayaran         |
=======
| Field      | Description           |
| ---------- | --------------------- |
| `id`       | Tax record ID         |
| `motorId`  | Related motorcycle ID |
| `taxType`  | Tax type              |
| `dueDate`  | Tax due date          |
| `cost`     | Tax cost              |
| `status`   | Payment status        |
>>>>>>> Stashed changes

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
13. Pengguna dapat membuka profil, pengaturan, backup/import, dan tentang aplikasi.

<<<<<<< Updated upstream
## Aturan Estimasi Servis dan Oli
=======
1. The user opens the application.
2. The splash screen checks the user's login status.
3. If the user is not logged in, they are redirected to the onboarding screen.
4. The user logs in using a Google account.
5. The user enters their first motorcycle data.
6. The user can complete or skip the optional service, oil, and tax setup.
7. The dashboard displays the active motorcycle, monthly expenses, and upcoming maintenance schedules.
8. The user can change the active motorcycle from the motorcycle list page.
9. The `+` button is used to add service, oil, fuel, tax, or motorcycle records.
10. Vehicle history is displayed through service, oil, tax, and fuel tabs.
11. The application can fetch the latest fuel prices from scraped fuel price data to help users input fuel expenses.
12. Data is stored in a local database and can still be accessed offline.
13. The user can access profile, settings, backup/export, and about pages.
>>>>>>> Stashed changes

Servis dan oli memakai dua patokan:

* Kilometer
* Bulan

Aplikasi menampilkan estimasi yang lebih dulu tercapai di dashboard dan riwayat.

Default rekomendasi:

| Jenis  | Kilometer | Waktu                |
| ------ | --------- | -------------------- |
| Servis | 4.000 km  | Bisa diatur pengguna |
| Oli    | 3.000 km  | Bisa diatur pengguna |

<<<<<<< Updated upstream
Jika kilometer motor sudah mencapai target servis, oli, atau pajak sudah jatuh tempo, aplikasi menampilkan status peringatan.
=======
| Maintenance Type |  Mileage | Time     |
| ---------------- | -------: | -------- |
| Service          | 4,000 km | Customizable |
| Oil Change       | 3,000 km | Customizable |
>>>>>>> Stashed changes

## Data Harga BBM

<<<<<<< Updated upstream
Aplikasi mengambil data harga BBM dari website sumber harga bensin:
=======
## 🌐 Fuel Price Data

MotoCare scrapes fuel price data from the following source:
>>>>>>> Stashed changes

```text
https://isibens.in/
```

<<<<<<< Updated upstream
Data ini digunakan untuk membantu pengisian harga per liter pada catatan bensin. Merek dan RON yang tidak memiliki harga valid, bernilai `0`, atau bernilai `-` tidak ditampilkan sebagai pilihan.

Jika pengambilan data gagal, pengguna tetap bisa memasukkan harga secara manual.

## Backup dan Import

Data lokal dapat diekspor dan diimpor dalam format JSON. Format JSON mengikuti struktur tabel lokal aplikasi dan digunakan untuk salinan data pribadi pengguna.

## Desain

Desain final berada di Figma:

```text
https://www.figma.com/design/SmNYZgqyZJYoF57be2rz5w/Motocare-Design--Copy-?node-id=2012-53&p=f&t=YSW39PW2qMv2LDrx-0
```

## Cara Menjalankan Project
=======
Fuel brands and RON options with invalid prices, `0`, or `-` are not shown as selectable options.

The scraped data is only used to help retrieve fuel prices. If the request fails, users can still enter the fuel price manually.
>>>>>>> Stashed changes

1. Clone repository ini.

```bash
git clone https://github.com/adlafayyaz/Motocare.git
```

2. Buka project menggunakan Android Studio.
3. Tunggu proses Gradle Sync selesai.
4. Pastikan konfigurasi Firebase tersedia untuk fitur Google Login.
5. Jalankan aplikasi menggunakan emulator atau perangkat Android.

## Anggota Kelompok

| Nama                              | NIM        | Role                                     |
| --------------------------------- | ---------- | ---------------------------------------- |
| Khaliz Kanigara Fathi Gunawan     | 2410512151 | Project Manager & Lead Backend Developer |
| Fathi Muhammad Luthfi Cardiana    | 2410512142 | UI/UX Designer & Frontend Developer      |
| M. Adla Fayyaz Fauzy              | 2410512154 | Frontend Developer - List & Navigation   |
| Rafid Abdan Syakur                | 2410512141 | Backend Developer                        |
| M. Syauqi Rabbani                 | 2410512166 | System Analyst                           |
| Ananta Jordan Surya Putra Ginting | 2410512164 | Quality Assurance & Technical Writer     |

## License

Project ini dibuat untuk kebutuhan pembelajaran sebagai proyek akhir mata kuliah Pemrograman Mobile / Pemrograman Perangkat Bergerak.
