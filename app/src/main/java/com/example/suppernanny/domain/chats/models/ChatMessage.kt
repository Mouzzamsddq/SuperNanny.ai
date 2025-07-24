package com.example.suppernanny.domain.chats.models

data class ChatMessage(
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = 0L,
    val type: String = "text"
)


