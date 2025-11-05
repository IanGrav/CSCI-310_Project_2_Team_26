# Railway Deployment Guide

## Step 1: Create Railway Account & Project

1. Go to [railway.app](https://railway.app) and sign up/login
2. Click "New Project"
3. Select "Deploy from GitHub repo" (recommended) or "Empty Project"

## Step 2: Add PostgreSQL Database

1. In your Railway project, click "New" → "Database" → "Add PostgreSQL"
2. Railway will automatically create a PostgreSQL database
3. Note: Railway will automatically set `DATABASE_URL` environment variable

## Step 3: Deploy Backend Code

### Option A: GitHub Deployment (Recommended)

1. Push your code to GitHub (if not already)
2. In Railway, click "New" → "GitHub Repo"
3. Select your repository
4. Railway will detect the `backend/` folder
5. Set the root directory to `backend/` in Railway settings

### Option B: Railway CLI

```bash
# Install Railway CLI
npm i -g @railway/cli

# Login
railway login

# Link to project
railway link

# Deploy
railway up
```

## Step 4: Configure Environment Variables

In Railway dashboard, go to your service → Variables tab, add:

- `JWT_SECRET` - Generate a strong random string (use: `openssl rand -base64 32`)
- `NODE_ENV` - Set to `production`
- `CORS_ORIGIN` - Your frontend URL (or `*` for development)
- `DATABASE_URL` - Already set by Railway automatically

**Note:** Railway automatically provides `DATABASE_URL` when you add PostgreSQL. Our `database.js` config will use this automatically!

## Step 5: Run Database Migration

### Option A: Using Railway PostgreSQL Dashboard

1. Go to your PostgreSQL service in Railway
2. Click "Query" tab
3. Copy contents of `backend/database/schema.sql`
4. Paste and click "Run"

### Option B: Using Railway CLI

```bash
# Connect to Railway PostgreSQL
railway connect postgres

# Run migration
psql $DATABASE_URL -f database/schema.sql

# Or use our migration script
railway run node database/migrate.js
```

### Option C: Using Node.js Script

Add to `package.json`:
```json
"scripts": {
  "migrate": "node database/migrate.js"
}
```

Then in Railway:
```bash
railway run npm run migrate
```

## Step 6: Verify Deployment

1. Check Railway logs to ensure server started
2. Test health endpoint: `https://your-app.railway.app/api/health`
3. Test registration: `POST /api/auth/register`

## Step 7: Update Android App

Update `ApiService.BASE_URL` in your Android app to your Railway URL:
```java
String BASE_URL = "https://your-app.railway.app/";
```

## Troubleshooting

### Database Connection Issues
- Verify `DATABASE_URL` is set correctly
- Check Railway PostgreSQL service is running
- Ensure migration ran successfully

### Build Errors
- Check Railway build logs
- Verify `package.json` has correct start script
- Ensure Node.js version is compatible (v16+)

### Runtime Errors
- Check Railway logs tab
- Verify all environment variables are set
- Test database connection manually

## Next Steps

After deployment:
1. ✅ Test all API endpoints
2. ✅ Update Android app with Railway URL
3. ✅ Test end-to-end functionality
4. ✅ Set up monitoring (optional)

## Railway URLs

Your deployed backend will have a URL like:
- `https://your-app-name.up.railway.app`

You can also set up a custom domain in Railway settings.

