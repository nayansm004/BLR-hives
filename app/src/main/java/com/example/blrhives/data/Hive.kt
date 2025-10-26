package com.example.blrhives.data


import java.time.LocalDateTime

data class Hive(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val tags: List<String> = emptyList(),
    val isPrivate: Boolean = false,
    val creatorId: String = "",
    val memberCount: Int = 0,
    val coverImageUrl: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now()
)