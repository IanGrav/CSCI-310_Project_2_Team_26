# BestLLM Codebase Analysis & Implementation Plan

## Current State Analysis

### ✅ What's Currently Implemented

#### Android Client (MVVM Architecture)
1. **Authentication System** (Partially Complete)
   - `LoginActivity` with USC email validation
   - `RegistrationActivity` structure
   - `AuthRepository` with register/login methods
   - `AuthViewModel` with state management
   - Session management (placeholders only)

2. **Profile Management** (Complete)
   - `Profile` model with all required fields
   - `ProfileRepository` with create/get/update methods
   - `ProfileViewModel` for profile state
   - `ProfileCreateFragment` and `ProfileCreationActivity`
   - Profile update functionality

3. **Basic Infrastructure**
   - Retrofit setup for API calls
   - Gson for JSON parsing
   - Navigation component structure
   - Basic UI fragments (Home, Dashboard, Notifications) - **but these are placeholders**

4. **Data Models**
   - `User` model ✅
   - `Profile` model ✅

### ❌ What's Missing

#### Android Client - Core Features

1. **Post Management** (Completely Missing)
   - ❌ `Post` model class
   - ❌ `PostRepository` 
   - ❌ `PostViewModel`
   - ❌ Post list UI (fragment/activity)
   - ❌ Post detail UI (with comments)
   - ❌ Create post UI
   - ❌ Edit post UI
   - ❌ Prompt posts (separate from regular posts)

2. **Comment Management** (Completely Missing)
   - ❌ `Comment` model class
   - ❌ `CommentRepository`
   - ❌ `CommentViewModel`
   - ❌ Comment list UI
   - ❌ Create comment UI
   - ❌ Edit comment UI

3. **Voting System** (Completely Missing)
   - ❌ Vote model/logic
   - ❌ Upvote/downvote UI
   - ❌ Vote tracking and persistence

4. **Search Functionality** (Completely Missing)
   - ❌ Search UI (search bar with dropdown)
   - ❌ Search by tag, author, title, full text
   - ❌ Search implementation in Repository layer

5. **Trending Posts** (Completely Missing)
   - ❌ Sorting/filtering logic (newest, top K trending)
   - ❌ Trending posts UI

6. **API Service** (Incomplete)
   - ✅ Auth endpoints (register, login, validate, logout)
   - ✅ Profile endpoints (create, get, update, reset-password)
   - ❌ Post endpoints (GET, POST, PUT, DELETE)
   - ❌ Comment endpoints (GET, POST, PUT, DELETE)
   - ❌ Vote endpoints (POST, DELETE)
   - ❌ Search endpoints

#### Backend Server (Completely Missing)

1. **Node.js Backend** (Not Started)
   - ❌ Server setup (Express.js or similar)
   - ❌ API routes structure
   - ❌ Authentication middleware
   - ❌ Database connection setup
   - ❌ All API endpoints implementation

2. **PostgreSQL Database** (Not Started)
   - ❌ Database schema (users, posts, comments, votes, tags tables)
   - ❌ Database migrations
   - ❌ Connection configuration
   - ❌ Railway deployment setup

#### Additional Issues

1. **Session Management** - Currently has placeholder implementations
2. **Image Upload** - ProfileRepository has placeholder for image upload
3. **Error Handling** - Basic structure exists but needs enhancement
4. **Network Error Handling** - Needs offline support/caching

## Implementation Plan

### Phase 1: Backend Foundation (Node.js + PostgreSQL)

#### Step 1.1: Setup Node.js Backend Structure
- [ ] Initialize Node.js project with Express
- [ ] Setup project structure (routes, controllers, models, middleware)
- [ ] Install dependencies (express, pg, bcrypt, jsonwebtoken, cors, dotenv)
- [ ] Configure environment variables
- [ ] Setup Railway deployment configuration

#### Step 1.2: PostgreSQL Database Setup
- [ ] Create database schema (SQL migration files):
  - `users` table (id, username, password_hash, email, student_id, created_at, has_profile)
  - `profiles` table (user_id FK, name, email, affiliation, birth_date, bio, interests, profile_picture_url, created_at, updated_at)
  - `posts` table (id PK, author_id FK, title, content, llm_tag, is_prompt_post BOOLEAN, created_at, updated_at)
  - `comments` table (id PK, post_id FK, author_id FK, text, created_at, updated_at)
  - `votes` table (id PK, user_id FK, post_id FK, comment_id FK nullable, type ENUM('up', 'down'), created_at)
  - `tags` table (id PK, tag_name UNIQUE)
- [ ] Setup PostgreSQL on Railway
- [ ] Create database connection module
- [ ] Test database connection

#### Step 1.3: Authentication Backend
- [ ] Implement user registration endpoint (`POST /api/auth/register`)
- [ ] Implement login endpoint (`POST /api/auth/login`) with JWT
- [ ] Implement token validation (`POST /api/auth/validate`)
- [ ] Implement logout endpoint (`POST /api/auth/logout`)
- [ ] Create authentication middleware

#### Step 1.4: Profile API Endpoints
- [ ] `POST /api/profile/create` - Create profile
- [ ] `GET /api/profile/:userId` - Get profile
- [ ] `PUT /api/profile/:userId` - Update profile
- [ ] `POST /api/profile/reset-password` - Reset password

#### Step 1.5: Posts API Endpoints
- [ ] `GET /api/posts` - Get all posts (with pagination, sorting, filtering)
- [ ] `GET /api/posts/prompts` - Get prompt posts only
- [ ] `GET /api/posts/trending` - Get top K trending posts
- [ ] `GET /api/posts/:id` - Get single post with comments
- [ ] `POST /api/posts` - Create new post
- [ ] `PUT /api/posts/:id` - Update post
- [ ] `DELETE /api/posts/:id` - Delete post
- [ ] `GET /api/posts/search` - Search posts (by tag, author, title, content)

#### Step 1.6: Comments API Endpoints
- [ ] `GET /api/comments/:postId` - Get comments for a post
- [ ] `POST /api/comments` - Create comment
- [ ] `PUT /api/comments/:id` - Update comment
- [ ] `DELETE /api/comments/:id` - Delete comment

#### Step 1.7: Votes API Endpoints
- [ ] `POST /api/votes` - Create/update vote (upvote/downvote)
- [ ] `DELETE /api/votes/:postId` or `/api/votes/:commentId` - Remove vote
- [ ] `GET /api/votes/:postId` - Get vote counts for post

### Phase 2: Android Client - Data Models & Repositories

#### Step 2.1: Create Data Models
- [ ] Create `Post` model (id, author_id, author_name, title, content, llm_tag, is_prompt_post, votes, upvotes, downvotes, comment_count, created_at, updated_at)
- [ ] Create `Comment` model (id, post_id, author_id, author_name, text, upvotes, downvotes, created_at, updated_at)
- [ ] Create `Vote` model (id, user_id, post_id, comment_id, type)

#### Step 2.2: Extend ApiService
- [ ] Add all post endpoints to `ApiService`
- [ ] Add all comment endpoints to `ApiService`
- [ ] Add all vote endpoints to `ApiService`
- [ ] Add search endpoints to `ApiService`

#### Step 2.3: Create Repositories
- [ ] Create `PostRepository` with methods:
  - `getPosts(sortBy, filterBy, callback)`
  - `getPromptPosts(callback)`
  - `getTrendingPosts(k, callback)`
  - `getPost(postId, callback)`
  - `createPost(post, callback)`
  - `updatePost(postId, post, callback)`
  - `deletePost(postId, callback)`
  - `searchPosts(query, searchType, callback)`
- [ ] Create `CommentRepository` with methods:
  - `getComments(postId, callback)`
  - `createComment(comment, callback)`
  - `updateComment(commentId, text, callback)`
  - `deleteComment(commentId, callback)`
- [ ] Create `VoteRepository` with methods:
  - `votePost(postId, voteType, callback)`
  - `voteComment(commentId, voteType, callback)`
  - `removeVote(postId, commentId, callback)`
  - `getVoteCounts(postId, callback)`

### Phase 3: Android Client - ViewModels

#### Step 3.1: Create ViewModels
- [ ] Create `PostViewModel` with LiveData for:
  - Posts list
  - Single post
  - Loading states
  - Error states
  - Methods: loadPosts(), loadPromptPosts(), loadTrending(), createPost(), updatePost(), deletePost(), searchPosts()
- [ ] Create `CommentViewModel` with LiveData for:
  - Comments list
  - Loading/error states
  - Methods: loadComments(), createComment(), updateComment(), deleteComment()
- [ ] Create `VoteViewModel` or integrate into PostViewModel/CommentViewModel:
  - Methods: upvotePost(), downvotePost(), upvoteComment(), downvoteComment(), removeVote()

### Phase 4: Android Client - UI Implementation

#### Step 4.1: Post List UI
- [ ] Create `PostListFragment` layout with:
  - RecyclerView for posts
  - Search bar with dropdown (tag/author/title/full text)
  - Sort options (newest, trending, top)
  - Filter toggle (regular posts / prompt posts)
- [ ] Create `PostAdapter` for RecyclerView
- [ ] Implement post item layout (title, author, content preview, votes, comment count, tag)
- [ ] Connect to `PostViewModel`
- [ ] Implement click navigation to post detail

#### Step 4.2: Post Detail UI
- [ ] Create `PostDetailFragment` layout
- [ ] Display full post content
- [ ] Show upvote/downvote buttons with counts
- [ ] Display comments list (RecyclerView)
- [ ] Add comment input at bottom
- [ ] Edit/delete buttons (if author)
- [ ] Connect to `PostViewModel` and `CommentViewModel`

#### Step 4.3: Create/Edit Post UI
- [ ] Create `CreatePostFragment` layout with:
  - Title input
  - Content textarea
  - LLM tag selector
  - Prompt post checkbox
  - Submit button
- [ ] Create `EditPostFragment` (similar to create)
- [ ] Connect to `PostViewModel`
- [ ] Add validation

#### Step 4.4: Comment UI
- [ ] Create `CommentAdapter` for RecyclerView
- [ ] Comment item layout (author, text, votes, timestamp)
- [ ] Edit/delete comment functionality
- [ ] Connect to `CommentViewModel`

#### Step 4.5: Update Navigation
- [ ] Update `mobile_navigation.xml` to include:
  - Post list fragment
  - Post detail fragment
  - Create post fragment
  - Edit post fragment
- [ ] Update bottom navigation menu
- [ ] Add navigation actions

#### Step 4.6: Replace Placeholder Fragments
- [ ] Replace `HomeFragment` with `PostListFragment` (regular posts)
- [ ] Replace `DashboardFragment` with `PostListFragment` (prompt posts) or create separate
- [ ] Replace `NotificationsFragment` with trending posts or user posts

### Phase 5: Session Management & Polish

#### Step 5.1: Complete Session Management
- [ ] Implement SharedPreferences for token storage
- [ ] Implement secure storage for sensitive data
- [ ] Auto-login on app start
- [ ] Token refresh logic

#### Step 5.2: Error Handling & Loading States
- [ ] Add loading indicators
- [ ] Add error messages/toasts
- [ ] Handle network errors gracefully
- [ ] Add retry logic

#### Step 5.3: Image Upload
- [ ] Implement profile picture upload
- [ ] Add image picker
- [ ] Handle image compression

#### Step 5.4: Testing
- [ ] Test all API endpoints
- [ ] Test UI flows
- [ ] Test error scenarios
- [ ] Test offline behavior (if caching implemented)

### Phase 6: Deployment

#### Step 6.1: Backend Deployment
- [ ] Deploy Node.js backend to Railway
- [ ] Configure environment variables on Railway
- [ ] Setup PostgreSQL database on Railway
- [ ] Run database migrations
- [ ] Test deployed endpoints

#### Step 6.2: Update Android Client
- [ ] Update `ApiService.BASE_URL` to Railway backend URL
- [ ] Test Android app with deployed backend
- [ ] Fix any production issues

## Priority Order

1. **Critical Path (Must Have):**
   - Backend setup (Node.js + PostgreSQL)
   - Post/Comment/Vote models and repositories
   - Basic post list and detail UI
   - Create post functionality
   - Voting functionality

2. **Important (Should Have):**
   - Comment creation/editing
   - Search functionality
   - Post editing
   - Prompt posts separation

3. **Nice to Have:**
   - Trending posts
   - Advanced search
   - Image uploads
   - Offline caching

## Estimated Time Breakdown

- Phase 1 (Backend): 20-30 hours
- Phase 2 (Android Data Layer): 10-15 hours
- Phase 3 (Android ViewModels): 5-8 hours
- Phase 4 (Android UI): 20-30 hours
- Phase 5 (Polish): 10-15 hours
- Phase 6 (Deployment): 5-8 hours

**Total: ~70-106 hours**

## Notes

- The current `HomeFragment`, `DashboardFragment`, and `NotificationsFragment` are placeholders and need to be replaced with actual functionality
- The `ApiService.BASE_URL` is currently set to a placeholder URL - needs to be updated to Railway URL
- Session management is currently incomplete - needs SharedPreferences implementation
- Profile image upload is a placeholder - needs actual implementation

