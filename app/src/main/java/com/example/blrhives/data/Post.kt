package com.example.blrhives.data

import java.time.LocalDateTime

data class Post(
    val id: String = "",
    val text: String = "",
    val imageUrl: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorProfilePic: String = "",
    val hiveId: String = "",
    val hiveName: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val likes: Int = 0,
    val comments: Int = 0
)