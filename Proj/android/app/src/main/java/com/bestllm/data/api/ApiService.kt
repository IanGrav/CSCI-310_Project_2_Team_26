package com.bestllm.data.api

import com.bestllm.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Users
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): Response<User>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body request: UpdateUserRequest
    ): Response<User>

    // Posts
    @GET("posts")
    suspend fun getPosts(@Query("tag") tag: String? = null): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): Response<Post>

    @POST("posts")
    suspend fun createPost(@Body request: CreatePostRequest): Response<Post>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): Response<Unit>

    // Comments
    @GET("posts/{id}/comments")
    suspend fun getComments(@Path("id") postId: Int): Response<List<Comment>>

    @POST("posts/{id}/comments")
    suspend fun createComment(
        @Path("id") postId: Int,
        @Body request: CreateCommentRequest
    ): Response<Comment>

    // Votes
    @POST("posts/{id}/vote")
    suspend fun votePost(
        @Path("id") postId: Int,
        @Body request: VoteRequest
    ): Response<VoteResponse>
}

