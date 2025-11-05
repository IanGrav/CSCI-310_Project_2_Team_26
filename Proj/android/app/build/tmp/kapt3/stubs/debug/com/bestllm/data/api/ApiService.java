package com.bestllm.data.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00032\b\b\u0001\u0010\u0007\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u001e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00032\b\b\u0001\u0010\u0010\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0011J$\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00130\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u001e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00032\b\b\u0001\u0010\u0010\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0011J&\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00130\u00032\n\b\u0003\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u00a7@\u00a2\u0006\u0002\u0010\u0018J\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00032\b\b\u0001\u0010\u0010\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u001e\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00032\b\b\u0001\u0010\u0007\u001a\u00020\u001dH\u00a7@\u00a2\u0006\u0002\u0010\u001eJ\u001e\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00032\b\b\u0001\u0010\u0007\u001a\u00020 H\u00a7@\u00a2\u0006\u0002\u0010!J(\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00032\b\b\u0001\u0010\u0010\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020#H\u00a7@\u00a2\u0006\u0002\u0010$J(\u0010%\u001a\b\u0012\u0004\u0012\u00020&0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\'H\u00a7@\u00a2\u0006\u0002\u0010(\u00a8\u0006)"}, d2 = {"Lcom/bestllm/data/api/ApiService;", "", "createComment", "Lretrofit2/Response;", "Lcom/bestllm/data/model/Comment;", "postId", "", "request", "Lcom/bestllm/data/model/CreateCommentRequest;", "(ILcom/bestllm/data/model/CreateCommentRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createPost", "Lcom/bestllm/data/model/Post;", "Lcom/bestllm/data/model/CreatePostRequest;", "(Lcom/bestllm/data/model/CreatePostRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deletePost", "", "id", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getComments", "", "getPostById", "getPosts", "tag", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUser", "Lcom/bestllm/data/model/User;", "login", "Lcom/bestllm/data/model/AuthResponse;", "Lcom/bestllm/data/model/LoginRequest;", "(Lcom/bestllm/data/model/LoginRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "register", "Lcom/bestllm/data/model/RegisterRequest;", "(Lcom/bestllm/data/model/RegisterRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateUser", "Lcom/bestllm/data/model/UpdateUserRequest;", "(ILcom/bestllm/data/model/UpdateUserRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "votePost", "Lcom/bestllm/data/model/VoteResponse;", "Lcom/bestllm/data/model/VoteRequest;", "(ILcom/bestllm/data/model/VoteRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ApiService {
    
    @retrofit2.http.POST(value = "auth/register")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object register(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.bestllm.data.model.RegisterRequest request, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.bestllm.data.model.AuthResponse>> $completion);
    
    @retrofit2.http.POST(value = "auth/login")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object login(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.bestllm.data.model.LoginRequest request, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.bestllm.data.model.AuthResponse>> $completion);
    
    @retrofit2.http.GET(value = "users/{id}")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getUser(@retrofit2.http.Path(value = "id")
    int id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.bestllm.data.model.User>> $completion);
    
    @retrofit2.http.PUT(value = "users/{id}")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object updateUser(@retrofit2.http.Path(value = "id")
    int id, @retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.bestllm.data.model.UpdateUserRequest request, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.bestllm.data.model.User>> $completion);
    
    @retrofit2.http.GET(value = "posts")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getPosts(@retrofit2.http.Query(value = "tag")
    @org.jetbrains.annotations.Nullable
    java.lang.String tag, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.bestllm.data.model.Post>>> $completion);
    
    @retrofit2.http.GET(value = "posts/{id}")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getPostById(@retrofit2.http.Path(value = "id")
    int id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.bestllm.data.model.Post>> $completion);
    
    @retrofit2.http.POST(value = "posts")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object createPost(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.bestllm.data.model.CreatePostRequest request, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.bestllm.data.model.Post>> $completion);
    
    @retrofit2.http.DELETE(value = "posts/{id}")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deletePost(@retrofit2.http.Path(value = "id")
    int id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<kotlin.Unit>> $completion);
    
    @retrofit2.http.GET(value = "posts/{id}/comments")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getComments(@retrofit2.http.Path(value = "id")
    int postId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.bestllm.data.model.Comment>>> $completion);
    
    @retrofit2.http.POST(value = "posts/{id}/comments")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object createComment(@retrofit2.http.Path(value = "id")
    int postId, @retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.bestllm.data.model.CreateCommentRequest request, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.bestllm.data.model.Comment>> $completion);
    
    @retrofit2.http.POST(value = "posts/{id}/vote")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object votePost(@retrofit2.http.Path(value = "id")
    int postId, @retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.bestllm.data.model.VoteRequest request, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.bestllm.data.model.VoteResponse>> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}