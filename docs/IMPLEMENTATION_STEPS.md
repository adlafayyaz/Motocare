# MotoCare Implementation Steps

> Required worker skill: use plan/task execution. Do not push until user says `push`.

## FE And BE Scope
- FE: Android XML screens, Activity flow, RecyclerView lists, forms, dashboard, navigation, and profile pages.
- BE/data: local SQLite or Room database, CRUD helper/repository, service/oil estimate logic, import/export, and fuel price API client.
- No custom backend server for MVP.
- Google login is only for identity; vehicle data stays local.

## Step 0: Documentation Only
- Branch: `main`
- Status: current branch
- Files:
  - `AGENTS.md`
  - `CLAUDE.md`
  - `docs/PRD_ALIGNMENT.md`
  - `docs/IMPLEMENTATION_STEPS.md`
  - `docs/DATABASE_OPTIONS.md`
  - `docs/DESIGN_SPEC.md`
  - `docs/API_AND_REALTIME.md`
  - `docs/ICON_PLAN.md`
  - `README.md`
- Commit message: `docs: add motocare prd implementation plan`
- Verify:
  - `git status --short --branch`
  - Confirm only `.md` files are intentional changes.

## Step 1: Foundation And Auth
- Branch: create `feat/foundation` from `main`
- Goal: make app match first flow: Splash -> Onboarding -> Google Login -> Setup Motor.
- Files:
  - Modify `app/src/main/AndroidManifest.xml`
  - Modify `app/src/main/java/com/example/motocare/MainActivity.kt`
  - Create `app/src/main/java/com/example/motocare/OnboardingActivity.kt`
  - Create `app/src/main/java/com/example/motocare/LoginActivity.kt`
  - Create `app/src/main/java/com/example/motocare/setup/SetupMotorActivity.kt`
  - Create `app/src/main/java/com/example/motocare/auth/GoogleAuthManager.kt`
  - Create `app/src/main/res/layout/activity_splash.xml`
  - Create `app/src/main/res/layout/activity_onboarding.xml`
  - Create `app/src/main/res/layout/activity_login.xml`
  - Create `app/src/main/res/layout/activity_setup_motor.xml`
  - Modify `app/src/main/res/values/strings.xml`
- Commit message: `feat: add foundation`
- Verify:
  - `.\gradlew.bat :app:assembleDebug`
  - Open app. Expected: splash opens, onboarding opens, Google login button appears, setup motor opens after login.

## Step 2: Local Database
- Branch: create `feat/db` from `feat/foundation`
- Goal: add local storage for PRD tables.
- Files:
  - Create `app/src/main/java/com/example/motocare/data/MotoCareDbHelper.kt`
  - Create `app/src/main/java/com/example/motocare/data/Motor.kt`
  - Create `app/src/main/java/com/example/motocare/data/Servis.kt`
  - Create `app/src/main/java/com/example/motocare/data/Bensin.kt`
  - Create `app/src/main/java/com/example/motocare/data/Pajak.kt`
  - Create `app/src/main/java/com/example/motocare/data/UserProfile.kt`
  - Create `app/src/test/java/com/example/motocare/data/MotoCareDbHelperTest.kt`
- Commit message: `feat: add database`
- Verify:
  - `.\gradlew.bat :app:testDebugUnitTest`
  - `.\gradlew.bat :app:assembleDebug`

## Step 3: Data Motor CRUD
- Branch: create `feat/motor` from `feat/db`
- Goal: add data motor add, list, detail, edit, delete.
- Files:
  - Create `app/src/main/java/com/example/motocare/motor/MotorListActivity.kt`
  - Create `app/src/main/java/com/example/motocare/motor/MotorFormActivity.kt`
  - Create `app/src/main/java/com/example/motocare/motor/MotorDetailActivity.kt`
  - Create `app/src/main/java/com/example/motocare/motor/MotorAdapter.kt`
  - Create `app/src/main/res/layout/activity_motor_list.xml`
  - Create `app/src/main/res/layout/activity_motor_form.xml`
  - Create `app/src/main/res/layout/activity_motor_detail.xml`
  - Create `app/src/main/res/layout/item_motor.xml`
  - Modify `app/src/main/AndroidManifest.xml`
  - Modify `app/src/main/res/layout/activity_dashboard.xml`
- Commit message: `feat: add motor crud`
- Verify:
  - `.\gradlew.bat :app:assembleDebug`
  - Black box: add motor, show list, open detail, edit, delete.

## Step 4: Dashboard And Navigation
- Branch: create `feat/dashboard` from `feat/motor`
- Goal: add dashboard, bottom nav, catat menu, and active motor context.
- Files:
  - Create `app/src/main/java/com/example/motocare/DashboardActivity.kt`
  - Create `app/src/main/java/com/example/motocare/CatatActivity.kt`
  - Create `app/src/main/res/layout/activity_dashboard.xml`
  - Create `app/src/main/res/layout/activity_catat.xml`
  - Create reusable nav layout or helper.
  - Add query helpers in database helper.
- Commit message: `feat: add dashboard`
- Verify:
  - `.\gradlew.bat :app:assembleDebug`
  - Black box: dashboard shows active motor, chart values, service/oil estimates, bottom nav, catat menu.

## Step 5: Servis And Oli CRUD
- Branch: create `feat/service-oil` from `feat/dashboard`
- Goal: add riwayat servis and ganti oli records.
- Files:
  - Create `app/src/main/java/com/example/motocare/servis/ServisListActivity.kt`
  - Create `app/src/main/java/com/example/motocare/servis/ServisFormActivity.kt`
  - Create `app/src/main/java/com/example/motocare/servis/ServisDetailActivity.kt`
  - Create `app/src/main/java/com/example/motocare/servis/ServisAdapter.kt`
  - Create `app/src/main/res/layout/activity_servis_list.xml`
  - Create `app/src/main/res/layout/activity_servis_form.xml`
  - Create `app/src/main/res/layout/activity_servis_detail.xml`
  - Create `app/src/main/res/layout/item_servis.xml`
  - Modify database helper to store `servis` rows and mark oil-change service type.
- Commit message: `feat: add service and oil`
- Verify:
  - `.\gradlew.bat :app:assembleDebug`
  - Black box: add servis, add ganti oli, show list, detail, edit, delete.

## Step 6: Bensin CRUD And Price API
- Branch: create `feat/fuel` from `feat/service-oil`
- Goal: add pengeluaran bensin records and realtime fuel price helper.
- Files:
  - Create `app/src/main/java/com/example/motocare/bensin/BensinListActivity.kt`
  - Create `app/src/main/java/com/example/motocare/bensin/BensinFormActivity.kt`
  - Create `app/src/main/java/com/example/motocare/bensin/BensinDetailActivity.kt`
  - Create `app/src/main/java/com/example/motocare/bensin/BensinAdapter.kt`
  - Create `app/src/main/java/com/example/motocare/bensin/FuelPriceApi.kt`
  - Create `app/src/main/java/com/example/motocare/bensin/FuelPriceRepository.kt`
  - Create `app/src/main/res/layout/activity_bensin_list.xml`
  - Create `app/src/main/res/layout/activity_bensin_form.xml`
  - Create `app/src/main/res/layout/activity_bensin_detail.xml`
  - Create `app/src/main/res/layout/item_bensin.xml`
- Commit message: `feat: add fuel crud`
- Verify:
  - `.\gradlew.bat :app:assembleDebug`
  - Black box: load fuel price, manual fallback works, add bensin, show list, detail, edit, delete.

## Step 7: Pajak CRUD
- Branch: create `feat/tax` from `feat/fuel`
- Goal: add pajak/STNK due date, biaya, payment status.
- Files:
  - Create `app/src/main/java/com/example/motocare/pajak/PajakListActivity.kt`
  - Create `app/src/main/java/com/example/motocare/pajak/PajakFormActivity.kt`
  - Create `app/src/main/java/com/example/motocare/pajak/PajakDetailActivity.kt`
  - Create `app/src/main/java/com/example/motocare/pajak/PajakAdapter.kt`
  - Create `app/src/main/res/layout/activity_pajak_list.xml`
  - Create `app/src/main/res/layout/activity_pajak_form.xml`
  - Create `app/src/main/res/layout/activity_pajak_detail.xml`
  - Create `app/src/main/res/layout/item_pajak.xml`
- Commit message: `feat: add tax crud`
- Verify:
  - `.\gradlew.bat :app:assembleDebug`
  - Black box: add pajak, change status, show list, detail, edit, delete.

## Step 8: Profile And Settings
- Branch: create `feat/profile` from `feat/tax`
- Goal: add profile, edit profile, settings, backup/export, and about pages.
- Files:
  - Create `app/src/main/java/com/example/motocare/profile/ProfileActivity.kt`
  - Create `app/src/main/java/com/example/motocare/profile/EditProfileActivity.kt`
  - Create `app/src/main/java/com/example/motocare/profile/SettingsActivity.kt`
  - Create `app/src/main/java/com/example/motocare/profile/BackupActivity.kt`
  - Create `app/src/main/java/com/example/motocare/profile/AboutActivity.kt`
  - Create matching XML layouts.
- Commit message: `feat: add profile`
- Verify:
  - `.\gradlew.bat :app:assembleDebug`
  - Black box: profile opens from nav, edit profile works, settings opens, export/import actions show expected state.

## Step 9: UI Polish Icons And Final Test
- Branch: create `chore/final-test` from `feat/profile`
- Goal: align UI with PRD NFR: simple, neat, easy, fast, valid input.
- Files:
  - Modify `app/src/main/res/values/colors.xml`
  - Modify `app/src/main/res/values/themes.xml`
  - Add vector drawable icons from `docs/ICON_PLAN.md`.
  - Modify all form layouts for required field validation text.
  - Create `docs/BLACK_BOX_TESTING.md`
- Commit message: `test: add black box checklist`
- Verify:
  - `.\gradlew.bat :app:assembleDebug`
  - Run black box checklist:
    - Login
    - Add motor
    - Add servis
    - Add oli
    - Add bensin
    - Add pajak
    - Fuel price API fallback
    - Edit data
    - Delete data
    - Dashboard estimate
    - Profile pages
    - Backup/export

## Push Rule
- Do not push any branch now.
- Push only after user says: `push`.
