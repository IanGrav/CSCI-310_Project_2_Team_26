# Remaining Steps to Complete BestLLM

## ✅ Completed

- [x] Backend setup (Node.js + Express + PostgreSQL)
- [x] Database schema and auto-migration
- [x] All API endpoints (Auth, Profile, Posts, Comments, Votes)
- [x] Railway deployment
- [x] Database connection and migrations
- [x] Basic Android structure (Auth, Profile)

## 🔧 Immediate Fix Needed

### 1. Update ApiService BASE_URL ✅ (Just Fixed)
- **File:** `app/src/main/java/.../data/network/ApiService.java`
- **Fix:** Added `https://` protocol and trailing `/`
- **Status:** ✅ Done

### 2. Test Backend Connection
- Test the health endpoint: `https://csci-310project2team26real-production.up.railway.app/api/health`
- Should return: `{"status":"ok","message":"BestLLM API is running"}`

---

## 📋 Remaining Implementation Steps

### Phase 2: Android Data Models & Repositories (CRITICAL - Must Do Next)

#### Step 2.1: Create Data Models
- [ ] **Create `Post.java`** model
  - Fields: id, author_id, author_name, title, content, llm_tag, is_prompt_post, upvotes, downvotes, comment_count, created_at, updated_at
  - Location: `app/src/main/java/.../data/model/Post.java`

- [ ] **Create `Comment.java`** model
  - Fields: id, post_id, author_id, author_name, text, upvotes, downvotes, created_at, updated_at
  - Location: `app/src/main/java/.../data/model/Comment.java`

- [ ] **Create `Vote.java`** model (optional - can be handled in repositories)
  - Fields: id, user_id, post_id, comment_id, type

#### Step 2.2: Extend ApiService
- [ ] **Add Post endpoints** to `ApiService.java`:
  - `GET /api/posts` - Get all posts
  - `GET /api/posts/prompts` - Get prompt posts
  - `GET /api/posts/trending` - Get trending posts
  - `GET /api/posts/search` - Search posts
  - `GET /api/posts/:id` - Get single post
  - `POST /api/posts` - Create post
  - `PUT /api/posts/:id` - Update post
  - `DELETE /api/posts/:id` - Delete post

- [ ] **Add Comment endpoints**:
  - `GET /api/comments/:postId` - Get comments
  - `POST /api/comments` - Create comment
  - `PUT /api/comments/:id` - Update comment
  - `DELETE /api/comments/:id` - Delete comment

- [ ] **Add Vote endpoints**:
  - `POST /api/votes/post/:postId` - Vote on post
  - `POST /api/votes/comment/:commentId` - Vote on comment
  - `DELETE /api/votes/post/:postId` - Remove vote
  - `GET /api/votes/post/:postId` - Get vote counts

#### Step 2.3: Create Repositories
- [ ] **Create `PostRepository.java`**
  - Methods: getPosts(), getPromptPosts(), getTrendingPosts(), getPost(), createPost(), updatePost(), deletePost(), searchPosts()
  - Location: `app/src/main/java/.../data/repository/PostRepository.java`

- [ ] **Create `CommentRepository.java`**
  - Methods: getComments(), createComment(), updateComment(), deleteComment()
  - Location: `app/src/main/java/.../data/repository/CommentRepository.java`

- [ ] **Create `VoteRepository.java`** (or integrate into PostRepository/CommentRepository)
  - Methods: votePost(), voteComment(), removeVote(), getVoteCounts()
  - Location: `app/src/main/java/.../data/repository/VoteRepository.java`

### Phase 3: Android ViewModels

- [ ] **Create `PostViewModel.java`**
  - LiveData for: posts list, single post, loading states, error states
  - Methods: loadPosts(), loadPromptPosts(), loadTrending(), createPost(), updatePost(), deletePost(), searchPosts()

- [ ] **Create `CommentViewModel.java`**
  - LiveData for: comments list, loading/error states
  - Methods: loadComments(), createComment(), updateComment(), deleteComment()

- [ ] **Integrate voting** into PostViewModel/CommentViewModel or create VoteViewModel

### Phase 4: Android UI Implementation

#### Step 4.1: Post List UI
- [ ] **Create `PostListFragment.java`** and `fragment_post_list.xml`
  - RecyclerView for posts
  - Search bar with dropdown (tag/author/title/full text)
  - Sort options (newest, trending, top)
  - Filter toggle (regular posts / prompt posts)
  - Connect to `PostViewModel`

- [ ] **Create `PostAdapter.java`** for RecyclerView
  - Post item layout showing: title, author, content preview, votes, comment count, tag
  - Click navigation to post detail

#### Step 4.2: Post Detail UI
- [ ] **Create `PostDetailFragment.java`** and `fragment_post_detail.xml`
  - Display full post content
  - Upvote/downvote buttons with counts
  - Comments list (RecyclerView)
  - Comment input at bottom
  - Edit/delete buttons (if author)
  - Connect to `PostViewModel` and `CommentViewModel`

#### Step 4.3: Create/Edit Post UI
- [ ] **Create `CreatePostFragment.java`** and `fragment_create_post.xml`
  - Title input
  - Content textarea
  - LLM tag selector
  - Prompt post checkbox
  - Submit button
  - Connect to `PostViewModel`

- [ ] **Create `EditPostFragment.java`** (similar to create)
  - Pre-fill with existing post data
  - Connect to `PostViewModel`

#### Step 4.4: Comment UI
- [ ] **Create `CommentAdapter.java`** for RecyclerView
  - Comment item layout: author, text, votes, timestamp
  - Edit/delete comment functionality
  - Connect to `CommentViewModel`

#### Step 4.5: Update Navigation
- [ ] **Update `mobile_navigation.xml`**:
  - Add post list fragment
  - Add post detail fragment
  - Add create post fragment
  - Add edit post fragment

- [ ] **Update bottom navigation menu** (`bottom_nav_menu.xml`)
  - Replace placeholder fragments with actual functionality

#### Step 4.6: Replace Placeholder Fragments
- [ ] **Replace `HomeFragment`** with `PostListFragment` (regular posts)
- [ ] **Replace `DashboardFragment`** with prompt posts or separate post list
- [ ] **Replace `NotificationsFragment`** with trending posts or user posts

### Phase 5: Session Management & Polish

- [ ] **Complete Session Management**:
  - Implement SharedPreferences for token storage
  - Implement secure storage for sensitive data
  - Auto-login on app start
  - Token refresh logic

- [ ] **Error Handling & Loading States**:
  - Add loading indicators
  - Add error messages/toasts
  - Handle network errors gracefully
  - Add retry logic

- [ ] **Image Upload** (Optional):
  - Implement profile picture upload
  - Add image picker
  - Handle image compression

---

## 🎯 Quick Start Guide

### To Start Implementing Now:

1. **First Priority - Data Models:**
   ```bash
   # Create these files:
   app/src/main/java/.../data/model/Post.java
   app/src/main/java/.../data/model/Comment.java
   ```

2. **Second Priority - Extend ApiService:**
   - Add all post/comment/vote endpoints to `ApiService.java`

3. **Third Priority - Create Repositories:**
   - Implement `PostRepository.java`
   - Implement `CommentRepository.java`

4. **Then Build UI:**
   - Create post list fragment
   - Create post detail fragment
   - Wire everything together

---

## 🧪 Testing Checklist

Before considering complete:

- [ ] Test user registration
- [ ] Test user login
- [ ] Test profile creation
- [ ] Test creating a post
- [ ] Test viewing posts
- [ ] Test voting on posts
- [ ] Test creating comments
- [ ] Test searching posts
- [ ] Test editing posts
- [ ] Test deleting posts
- [ ] Test error handling (network errors, validation errors)

---

## 📝 Notes

- The backend is **fully functional** and ready to use
- All API endpoints are implemented and tested
- Database schema is complete with auto-migration
- You only need to build the Android UI and connect it to the backend
- Start with Phase 2 (Data Models) - it's the foundation for everything else

---

## 🚀 Next Immediate Action

**Start with creating the Post model** - this is the foundation for everything else!

Would you like me to help you implement Phase 2 (Data Models & Repositories) next?

