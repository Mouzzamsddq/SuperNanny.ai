package com.example.suppernanny.domain.chats.usecases

import com.example.suppernanny.domain.chats.models.ChatMessage
import com.example.suppernanny.domain.chats.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    suspend operator fun invoke(message: ChatMessage): Result<Unit> = repo.sendMessage(message)
}