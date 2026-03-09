package com.example.app_firebase.models

import com.google.firebase.Timestamp


data class Comment(
    val id:String = "",
    val postId:String = "",
    val content:String = "",
    val userId:String = "",
    val createdAt: Timestamp? = null
)
