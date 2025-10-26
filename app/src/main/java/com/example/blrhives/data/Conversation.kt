package com.example.blrhives.data

import java.time.LocalDateTime

data class Conversation(
    val id: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: LocalDateTime = LocalDateTime.now(),
    val otherUserName: String = "",
    val otherUserProfilePic: String = ""
)