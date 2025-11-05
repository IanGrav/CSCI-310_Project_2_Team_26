# BestLLM Backend

Node.js + Express + PostgreSQL backend for the BestLLM Android application.

## Setup Instructions

1. **Install PostgreSQL** (if not already installed)
   - macOS: `brew install postgresql`
   - Linux: `sudo apt-get install postgresql`
   - Windows: Download from https://www.postgresql.org/download/

2. **Create Database**
   Set path
   ```bash
   export PATH=$PATH:/Library/PostgreSQL/18/bin
   echo 'export PATH=$PATH:/Library/PostgreSQL/18/bin' >> ~/.zshrc
   source ~/.zshrc
   ```
   Start sql, create database, initialize database (Navigate to this project folder first)
   ```bash
   pg_ctl -D /Library/PostgreSQL/18/data start
   createdb bestllm
   psql bestllm < backend/init-db.sql
   ```
   Or connect to PostgreSQL and run the SQL from `init-db.sql`

3. **Configure Database Connection**
   - Update `config/db.js` with your PostgreSQL credentials
   - Default connection string: `postgres://user:password@localhost:5432/bestllm`
   - Or set `DATABASE_URL` environment variable

4. **Install Dependencies**
   Install node package manager. Then:
   ```bash
   npm install express pg bcrypt jsonwebtoken cors body-parser dotenv
   npm install --save-dev nodemon
   ```

5. **Configure Database Connection**
   In backend/config/db.js, edit:
   ```bash
   const pool = new Pool({
      connectionString: process.env.DATABASE_URL || 'postgres://postgres:YOUR_PASSWORD@localhost:5432/bestllm',
      ssl: false
      });
   ```
   Change the YOUR_PASSWORD to your postgre password

6. **Run the Server**
   ```bash
   cd backend
   npm run dev
   ```
7. **Run the App**
   1. Open the android folder in Android Studio.
   2. Wait for Gradle sync to complete.
      If you see a Gradle/Java compatibility error:
         - Go to Preferences → Build, Execution, Deployment → Build Tools → Gradle
         - Set Gradle JDK = Java 17
         - Path: /Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
      Sync again.
   3. Click the Run button in Android


The server will start on `http://localhost:3000`

## API Endpoints

- `POST /auth/register` - Register a new user
- `POST /auth/login` - Login user
- `GET /users/:id` - Get user profile
- `PUT /users/:id` - Update user profile (requires auth)
- `GET /posts` - Get all posts (optional ?tag= filter)
- `GET /posts/:id` - Get post by ID
- `POST /posts` - Create post (requires auth)
- `DELETE /posts/:id` - Delete post (requires auth)
- `GET /posts/:id/comments` - Get comments for a post
- `POST /posts/:id/comments` - Add comment (requires auth)
- `POST /posts/:id/vote` - Vote on post (requires auth)

## Environment Variables

- `DATABASE_URL` - PostgreSQL connection string
- `JWT_SECRET` - Secret key for JWT tokens (default: 'bestllm_secret_key')
- `PORT` - Server port (default: 3000)

