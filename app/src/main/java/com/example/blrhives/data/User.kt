
package com.example.blrhives.data

data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val bio: String = "",
    val profileImageUrl: String = "",
    val joinedHives: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val followers: List<String> = emptyList()
)