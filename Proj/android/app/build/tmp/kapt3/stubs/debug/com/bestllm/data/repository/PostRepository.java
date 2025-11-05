package com.bestllm.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J6\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\b\u0010\u000f\u001a\u0004\u0018\u00010\rH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0010\u0010\u0011J\u0018\u0010\u0012\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0013\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180\u00172\u0006\u0010\u0013\u001a\u00020\u0014J\u001e\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001a0\u00172\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\rJ\u001a\u0010\u001b\u001a\u00020\u001c2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\rH\u0086@\u00a2\u0006\u0002\u0010\u001dJ,\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00140\n2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b \u0010!R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\""}, d2 = {"Lcom/bestllm/data/repository/PostRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "apiService", "Lcom/bestllm/data/api/ApiService;", "postDao", "Lcom/bestllm/data/database/PostDao;", "createPost", "Lkotlin/Result;", "Lcom/bestllm/data/model/Post;", "title", "", "content", "tag", "createPost-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPostById", "postId", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPostByIdLiveData", "Landroidx/lifecycle/LiveData;", "Lcom/bestllm/data/database/PostEntity;", "getPostsLiveData", "", "refreshPosts", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "votePost", "type", "votePost-0E7RQCE", "(ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class PostRepository {
    @org.jetbrains.annotations.NotNull
    private final com.bestllm.data.api.ApiService apiService = null;
    @org.jetbrains.annotations.NotNull
    private final com.bestllm.data.database.PostDao postDao = null;
    
    public PostRepository(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.bestllm.data.database.PostEntity>> getPostsLiveData(@org.jetbrains.annotations.Nullable
    java.lang.String tag) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object refreshPosts(@org.jetbrains.annotations.Nullable
    java.lang.String tag, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getPostById(int postId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.bestllm.data.model.Post> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.bestllm.data.database.PostEntity> getPostByIdLiveData(int postId) {
        return null;
    }
}