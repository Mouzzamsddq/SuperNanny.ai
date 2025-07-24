package com.example.suppernanny.domain.chats.repository

import com.example.suppernanny.domain.chats.models.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(message: ChatMessage): Result<Unit>
    fun observeMessages(userId: String): Flow<List<ChatMessage>>
}