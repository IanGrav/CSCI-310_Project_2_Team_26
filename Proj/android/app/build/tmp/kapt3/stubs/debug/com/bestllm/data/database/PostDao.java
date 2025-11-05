package com.bestllm.data.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006H\'J\u0018\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u00062\u0006\u0010\n\u001a\u00020\u000bH\'J\u001c\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010\r\u001a\u00020\u000eH\'J\u0016\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u001c\u0010\u0012\u001a\u00020\u00032\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\u0011\u00a8\u0006\u0016"}, d2 = {"Lcom/bestllm/data/database/PostDao;", "", "deleteAllPosts", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllPosts", "Landroidx/lifecycle/LiveData;", "", "Lcom/bestllm/data/database/PostEntity;", "getPostById", "postId", "", "getPostsByTag", "tag", "", "insertPost", "post", "(Lcom/bestllm/data/database/PostEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertPosts", "posts", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updatePost", "app_debug"})
@androidx.room.Dao
public abstract interface PostDao {
    
    @androidx.room.Query(value = "SELECT * FROM posts ORDER BY id DESC")
    @org.jetbrains.annotations.NotNull
    public abstract androidx.lifecycle.LiveData<java.util.List<com.bestllm.data.database.PostEntity>> getAllPosts();
    
    @androidx.room.Query(value = "SELECT * FROM posts WHERE llm_tag = :tag ORDER BY id DESC")
    @org.jetbrains.annotations.NotNull
    public abstract androidx.lifecycle.LiveData<java.util.List<com.bestllm.data.database.PostEntity>> getPostsByTag(@org.jetbrains.annotations.NotNull
    java.lang.String tag);
    
    @androidx.room.Query(value = "SELECT * FROM posts WHERE id = :postId")
    @org.jetbrains.annotations.NotNull
    public abstract androidx.lifecycle.LiveData<com.bestllm.data.database.PostEntity> getPostById(int postId);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertPost(@org.jetbrains.annotations.NotNull
    com.bestllm.data.database.PostEntity post, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertPosts(@org.jetbrains.annotations.NotNull
    java.util.List<com.bestllm.data.database.PostEntity> posts, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object updatePost(@org.jetbrains.annotations.NotNull
    com.bestllm.data.database.PostEntity post, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM posts")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteAllPosts(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}