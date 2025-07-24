package com.example.suppernanny.domain.chats.usecases

import com.example.suppernanny.domain.chats.models.ChatMessage
import com.example.suppernanny.domain.chats.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(userId: String): Flow<List<ChatMessage>> {
        return chatRepository.observeMessages(userId)
    }
}