# MotoCare Design Spec

## Figma
- File: `Motocare Design Copy`
- Page: `Page 3`
- URL: `https://www.figma.com/design/SmNYZgqyZJYoF57be2rz5w/Motocare-Design--Copy-?node-id=2012-53&p=f&t=YSW39PW2qMv2LDrx-0`

## Final Screen List
- `MotoCare Flow 00 Splash`
- `MotoCare Flow 00 Onboarding 1`
- `MotoCare Flow 00 Onboarding 2`
- `MotoCare Flow 00 Onboarding 3`
- `MotoCare Flow 00 Login`
- `MotoCare Flow 00 Setup Motor`
- `MotoCare Flow 00 Setup Servis`
- `MotoCare Flow 00 Setup Oli`
- `MotoCare Flow 00 Setup Pajak`
- `MotoCare Flow 01 Dashboard`
- `MotoCare Flow 02 Pilih Motor`
- `MotoCare Flow 03 Detail Motor`
- `MotoCare Flow 04 Catat`
- `MotoCare Flow 05 Riwayat Servis`
- `MotoCare Flow 06 Riwayat Oli`
- `MotoCare Flow 07 Riwayat Pajak`
- `MotoCare Flow 08 Riwayat Bensin`
- `MotoCare Flow 09 Profil`
- `MotoCare Flow 10 Edit Profil`
- `MotoCare Flow 11 Pengaturan`
- `MotoCare Flow 12 Backup Export`
- `MotoCare Flow 13 Tentang Aplikasi`

## Flow
- Splash checks login state.
- If not logged in: Onboarding -> Login.
- Login uses Google only.
- After first login: Setup Motor is required.
- Setup Servis, Setup Oli, and Setup Pajak are optional.
- Dashboard shows one active motor.
- Motor page changes active motor.
- Center `+` button opens Catat.
- Riwayat uses tabs: Servis, Oli, Pajak, Bensin.
- Profil opens Edit Profil, Pengaturan, Backup Export, and Tentang Aplikasi.

## Navigation
- Bottom nav:
  - Home
  - Motor
  - `+` Catat
  - Riwayat
  - Profil
- Dashboard has greeting and profile shortcut.
- Other pages do not show greeting.

## Dashboard Data
- Active motor summary.
- Monthly spending total.
- Donut chart: bensin, servis, oli, pajak.
- Next service card:
  - `Servis berikutnya`
  - `1.200 km / 18 hari`
  - `13.650 km`
  - `Target`
- Data rows:
  - Bensin
  - Pajak
  - Oli

## Setup Required Data
- Google login.
- At least one motor:
  - nama motor
  - plat nomor
  - kilometer sekarang

## Setup Optional Data
- Servis terakhir.
- Interval servis.
- Interval bulan servis.
- Oli terakhir.
- Interval oli.
- Interval bulan oli.
- Pajak/STNK.

## Copy Text
- Setup Motor: `Masukkan data motor pertama kamu`
- Setup Servis: `Isi jika pernah servis sebelumnya`
- Setup Oli: `Isi jika sudah pernah ganti oli`
- Setup Pajak: `Isi tanggal pajak agar tidak lupa`
- Servis note: `Kami bantu pantau kapan motor perlu servis lagi.`
- Oli note: `Kami bantu pantau kapan oli perlu diganti lagi.`
- Pajak note: `Tanggal pajak jadi lebih mudah dipantau.`

## Estimation Rule
- Servis uses kilometer and month interval.
- Oli uses kilometer and month interval.
- App shows whichever estimate is closer.
- Default:
  - Servis: `3.000 km / 3 bulan`
  - Oli: `2.000 km / 2 bulan`

## Visual Style
- Dark navy background.
- Yellow primary accent.
- Purple and orange secondary accents.
- Rounded cards.
- Simple flat vector onboarding images.
- No crowded floating icons.
- History row icons are removed.

## Feature Status From Design
- Done in design:
  - Onboarding
  - Google login
  - Setup motor
  - Setup servis
  - Setup oli
  - Setup pajak
  - Dashboard
  - Multi motor picker
  - Motor detail
  - Catat
  - Riwayat servis
  - Riwayat oli
  - Riwayat pajak
  - Riwayat bensin
  - Profile
  - Edit profile
  - Pengaturan
  - Backup/export
  - Tentang aplikasi
- State frames added:
  - `MotoCare State Form Validation`
  - `MotoCare State Empty Riwayat`
  - `MotoCare State Delete Confirm`
  - `MotoCare State API Failure`
  - `MotoCare State Import Export Picker`
- Still not fully detailed:
  - Every exact add/edit form variant for each data type.
  - Every success state after save/export/import.
