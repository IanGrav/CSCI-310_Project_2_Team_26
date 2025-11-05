# BestLLM Project

A complete Android application (Kotlin) with a Node.js + Express + PostgreSQL backend that allows USC students to share experiences and prompts about Large Language Models (LLMs).

## Project Structure

```
Proj/
├── backend/          # Node.js + Express + PostgreSQL backend
├── android/          # Android Kotlin application
└── Wibing.md         # Complete project specification
```

## Backend Setup

1. **Install PostgreSQL** (if not already installed)
   - macOS: `brew install postgresql`
   - Linux: `sudo apt-get install postgresql`
   - Windows: Download from https://www.postgresql.org/download/

2. **Create Database**
   ```bash
   cd backend
   createdb bestllm
   psql bestllm < init-db.sql
   ```

3. **Configure Database Connection**
   - Update `backend/config/db.js` with your PostgreSQL credentials
   - Default: `postgres://user:password@localhost:5432/bestllm`
   - Or set `DATABASE_URL` environment variable

4. **Install and Run Backend**
   ```bash
   cd backend
   npm install
   npm start
   # Or for development: npm run dev
   ```

   The server will start on `http://localhost:3000`

## Android Setup

1. **Open in Android Studio**
   - Open the `android` folder in Android Studio
   - Sync Gradle files
   - Wait for dependencies to download

2. **Configure for Android Emulator**
   - The app is configured to connect to `http://10.0.2.2:3000/` (Android emulator's localhost)
   - Start the backend server before running the app

3. **Run the App**
   - Create an Android Virtual Device (AVD) if needed
   - Run the app on the emulator or a connected device

## Features

- **User Authentication**: Register and login
- **Post Management**: Create, view, and browse posts about LLMs
- **Tag Filtering**: Search posts by LLM tags
- **Voting**: Upvote/downvote posts
- **Comments**: Add comments to posts
- **Offline Support**: Posts and comments cached locally using Room database
- **Profile**: View user profile and logout

## Architecture

### Backend
- Node.js + Express
- PostgreSQL database
- JWT-based authentication
- RESTful API endpoints

### Android
- MVVM architecture
- Repository pattern
- Room database for offline caching
- Retrofit for networking
- SharedPreferences for token storage

## API Endpoints

- `POST /auth/register` - Register new user
- `POST /auth/login` - Login user
- `GET /users/:id` - Get user profile
- `GET /posts` - Get all posts (optional ?tag= filter)
- `GET /posts/:id` - Get post by ID
- `POST /posts` - Create post (requires auth)
- `GET /posts/:id/comments` - Get comments for a post
- `POST /posts/:id/comments` - Add comment (requires auth)
- `POST /posts/:id/vote` - Vote on post (requires auth)

## Development Notes

- All development is local - nothing is hosted online
- Backend runs on port 3000
- Android emulator connects via `10.0.2.2:3000`
- Database schema is defined in `backend/init-db.sql`

