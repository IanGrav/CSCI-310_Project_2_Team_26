package com.bestllm.data.model

data class Post(
    val id: Int,
    val author_id: Int,
    val title: String,
    val content: String,
    val llm_tag: String?,
    val votes: Int,
    val author_name: String?
)

data class CreatePostRequest(
    val title: String,
    val content: String,
    val llm_tag: String?
)

