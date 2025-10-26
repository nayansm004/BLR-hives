package com.example.blrhives.data

import java.time.LocalDateTime

data class ChatMessage(
    val id: String = "",
    val text: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderProfilePic: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val isPrivate: Boolean = false,
    val hiveId: String = "" // For public hive chats
)