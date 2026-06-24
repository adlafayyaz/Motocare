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
- Google login
- Onboarding
- Setup awal motor
- Dashboard
- Manajemen data motor
- Riwayat servis motor
- Catatan ganti oli
- Catatan pengeluaran bensin
- Catatan pajak/STNK
- Estimasi pengeluaran bulanan
- Edit dan hapus data

## PRD Out Of Scope
- Google Maps or bengkel location
- Upload foto struk servis
- Multi-user online
- Dashboard admin
- Cloud sync

## Scope Update
- Firebase Auth is allowed only for Google login.
- App data stays local with SQLite or Room.
- Cloud sync is still out of scope.
- Fuel price may use `https://github.com/alifmaulidanar/hargaBensin`.
- Fuel API is optional; app must still work offline.

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
- `interval_km`: Integer
- `interval_month`: Integer
- `biaya`: Integer
- `catatan`: String

### `bensin`
- `id`: Integer primary key
- `motor_id`: Integer
- `tanggal`: String
- `jenis_bbm`: String
- `merek_bbm`: String
- `oktan`: String
- `harga_per_liter`: Integer
- `liter`: Double
- `nominal`: Integer
- `kilometer`: Integer

### `pajak`
- `id`: Integer primary key
- `motor_id`: Integer
- `jatuh_tempo`: String
- `biaya`: Integer
- `status`: String

### `oli`
- `id`: Integer primary key
- `motor_id`: Integer
- `tanggal`: String
- `kilometer`: Integer
- `interval_km`: Integer
- `interval_month`: Integer
- `jenis_oli`: String
- `biaya`: Integer

## Gap
- Repo is starter app only.
- PRD needs full offline CRUD app.
- No database layer exists.
- No RecyclerView screens exist.
- No navigation flow exists.
- No black box test checklist exists.
- Google login is not implemented.
- Fuel price API is not integrated.
- Final Figma design is not implemented.

## Fix Direction
- Keep XML + Activity + Intent + RecyclerView.
- Add local database.
- Add Google login for identity.
- Add optional fuel price API with offline fallback.
- Build screen-by-screen using small branches.
- Verify each branch with Gradle build and manual black box checklist.

## Database Decision Note
- Use SQLite or Room for app data.
- Google login does not require cloud database.
- Use cloud database only if data sync across devices becomes scope.
- Detailed option notes: `docs/DATABASE_OPTIONS.md`.

## Related Docs
- Design spec: `docs/DESIGN_SPEC.md`
- API and realtime plan: `docs/API_AND_REALTIME.md`
- Icon plan: `docs/ICON_PLAN.md`
