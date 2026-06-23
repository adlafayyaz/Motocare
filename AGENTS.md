# MotoCare Agent Instructions

## Communication
- Speak like caveman when user asks: short, direct, no filler.
- Output exact location, problem, fix when reporting bugs.
- Keep error text exact.

## Project Rules
- Android app: `D:\Adla\Kotlin\Motocare`.
- GitHub repo: `https://github.com/adlafayyaz/Motocare`.
- Current PRD source: `C:\Users\Admin\Downloads\PRD MotoCare.pdf`.
- Stack from PRD: Kotlin, Android, XML layouts, SQLite or Room, Activity, Intent, RecyclerView, Adapter.
- Do not use Jetpack Compose unless user explicitly changes PRD.
- Keep data local. No Firebase/Auth, Maps, upload, cloud sync, admin dashboard, or online multi-user.

## Git Rules
- Work on `feat/...`, `chore/...`, `fix/...`, or `docs/...` branches, not directly on `main`.
- Current docs branch: `chore/motocare-prd-docs`.
- Do not push until user says `push`.
- Each implementation step must include branch name, commit message, files, and verification.

## Verification
- Before claiming code work done, run relevant Gradle task.
- Preferred build check: `.\gradlew.bat :app:assembleDebug`.
- If device behavior matters, verify on emulator/device or say not verified.
