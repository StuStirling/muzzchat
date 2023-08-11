package com.stustirling.muzzchat.core.model

data class User(
    val uid: String,
    val isCurrentUser: Boolean,
    val name: String,
    val imageUrl: String?
)
