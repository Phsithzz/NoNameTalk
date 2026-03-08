package com.example.app_firebase.models

import com.google.firebase.Timestamp

data class Post(
    val id:String = "",
    val content:String = "",
    val userId:String = "",
    val createdAt: Timestamp? = null,
    val likeCount: Long = 0,

    val likedBy: List<String> = emptyList()
)
