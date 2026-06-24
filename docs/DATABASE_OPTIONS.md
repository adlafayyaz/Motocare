# Database Options

## Current PRD Decision
- Use local SQLite or Room.
- Keep app offline-first.
- Use Firebase Auth only for Google login.
- Do not add Firestore, cloud sync, or online multi-user unless scope changes.

## If Google Login Is Added
- Google login with Firebase Auth can still use local SQLite or Room.
- Use local database when data only belongs to one phone.
- Use cloud database when user data must sync across devices.
- Current decision: Firebase Auth + local SQLite or Room.

## Firebase Free Limits
- Firebase Authentication: free Spark plan supports 50,000 monthly active users.
- Cloud Firestore Spark plan:
  - Stored data: 1 GiB
  - Reads: 50,000 per day
  - Writes: 20,000 per day
  - Deletes: 20,000 per day
  - Network egress: 10 GiB per month
- Realtime Database Spark plan:
  - Stored data: 1 GiB
  - Downloaded data: 10 GiB per month
- Cloud Storage for Firebase Spark plan:
  - Stored data: 5 GB

Source: [Firebase pricing](https://firebase.google.com/pricing)

## Free Cloud Database Alternatives
- Supabase Free:
  - Better if app needs SQL/Postgres, relational data, and easier reporting.
  - Free storage is usually bigger for database use than Firestore Spark.
  - Check latest quota before choosing: [Supabase pricing](https://supabase.com/pricing)
- Appwrite Free:
  - Better if app needs backend features plus file storage.
  - Check latest quota before choosing: [Appwrite pricing](https://appwrite.io/pricing)
- MongoDB Atlas Free:
  - Better for simple document data.
  - Free M0 storage is small, so not best if "memory besar" is the main goal.
  - Check latest quota before choosing: [MongoDB Atlas pricing](https://www.mongodb.com/pricing)

## Recommendation For This App
- MVP: Firebase Auth for Google login + SQLite or Room for app data.
- If sync across phones is required: Firebase Auth + Supabase Postgres.
- If staying fully Firebase: Firebase Auth + Firestore, but watch read/write quota.
- Fuel price API does not change the main database decision.
