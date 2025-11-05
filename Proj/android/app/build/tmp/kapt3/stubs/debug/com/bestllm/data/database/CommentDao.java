package com.bestllm.data.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u001c\u0010\u000e\u001a\u00020\u00032\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00a7@\u00a2\u0006\u0002\u0010\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/bestllm/data/database/CommentDao;", "", "deleteCommentsByPostId", "", "postId", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCommentsByPostId", "Landroidx/lifecycle/LiveData;", "", "Lcom/bestllm/data/database/CommentEntity;", "insertComment", "comment", "(Lcom/bestllm/data/database/CommentEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertComments", "comments", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao
public abstract interface CommentDao {
    
    @androidx.room.Query(value = "SELECT * FROM comments WHERE post_id = :postId ORDER BY id ASC")
    @org.jetbrains.annotations.NotNull
    public abstract androidx.lifecycle.LiveData<java.util.List<com.bestllm.data.database.CommentEntity>> getCommentsByPostId(int postId);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertComment(@org.jetbrains.annotations.NotNull
    com.bestllm.data.database.CommentEntity comment, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertComments(@org.jetbrains.annotations.NotNull
    java.util.List<com.bestllm.data.database.CommentEntity> comments, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM comments WHERE post_id = :postId")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteCommentsByPostId(int postId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}