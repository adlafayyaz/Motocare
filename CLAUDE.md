# MotoCare Claude Instructions

## Role
Implement MotoCare from PRD without changing stack or scope.

## Source Of Truth
- PRD: `C:\Users\Admin\Downloads\PRD MotoCare.pdf`
- Repo: `https://github.com/adlafayyaz/Motocare`
- Local path: `D:\Adla\Kotlin\Motocare`

## Hard Constraints
- Use Kotlin.
- Use XML layouts.
- Use local SQLite or Room.
- Use Activity, Intent, RecyclerView, Adapter.
- Keep app offline-first.
- Do not add Firebase, online login, Maps, cloud sync, photo upload, admin dashboard, or Compose.
- Do not push unless user explicitly says `push`.

## Work Style
- Make small commits.
- Keep each step independently buildable.
- Preserve user changes in `.idea` and unrelated files.
- Report exact failing command and exact error if build fails.

## Branch Plan
- Docs branch: `chore/motocare-prd-docs`
- Implementation base: create `feat/...` branches from latest `main` only after user approves.
- Suggested first implementation branch: `feat/motocare-foundation`
