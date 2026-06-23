# MotoCare PRD Alignment

## Location
- PRD: `C:\Users\Admin\Downloads\PRD MotoCare.pdf`
- Repo: `D:\Adla\Kotlin\Motocare`
- Remote: `https://github.com/adlafayyaz/Motocare`
- Docs branch: `chore/docs`

## Current Project
- Android app module: `app`
- Package: `com.example.motocare`
- Current UI: `activity_main.xml` with `Hello World!`
- Current activity: `MainActivity.kt`
- Current dependencies: AppCompat, Material, Activity, ConstraintLayout, Core KTX
- Dirty pre-existing files: `.idea/gradle.xml`, `.idea/misc.xml`, `.idea/appInsightsSettings.xml`, `.idea/deploymentTargetSelector.xml`, `.idea/vcs.xml`

## PRD Required Scope
- Splash Screen
- Login sederhana
- Dashboard
- Manajemen data motor
- Riwayat servis motor
- Catatan ganti oli
- Catatan pengeluaran bensin
- Catatan pajak/STNK
- Estimasi pengeluaran bulanan
- Edit dan hapus data

## PRD Out Of Scope
- Firebase/Auth
- Real-time notification
- Google Maps or bengkel location
- Upload foto struk servis
- Multi-user online
- Dashboard admin
- Cloud sync

## Data Model From PRD

### `motor`
- `id`: Integer primary key
- `nama_motor`: String
- `plat_nomor`: String
- `kilometer`: Integer

### `servis`
- `id`: Integer primary key
- `motor_id`: Integer
- `tanggal`: String
- `jenis_servis`: String
- `kilometer`: Integer
- `biaya`: Integer
- `catatan`: String

### `bensin`
- `id`: Integer primary key
- `motor_id`: Integer
- `tanggal`: String
- `nominal`: Integer
- `kilometer`: Integer

### `pajak`
- `id`: Integer primary key
- `motor_id`: Integer
- `jatuh_tempo`: String
- `biaya`: Integer
- `status`: String

## Gap
- Repo is starter app only.
- PRD needs full offline CRUD app.
- No database layer exists.
- No RecyclerView screens exist.
- No navigation flow exists.
- No black box test checklist exists.

## Fix Direction
- Keep XML + Activity + Intent + RecyclerView.
- Add local database.
- Build screen-by-screen using small branches.
- Verify each branch with Gradle build and manual black box checklist.
