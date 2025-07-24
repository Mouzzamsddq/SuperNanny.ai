package com.example.suppernanny.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suppernanny.core.base.State
import com.example.suppernanny.domain.chats.models.ChatMessage
import com.example.suppernanny.domain.chats.usecases.ObserveMessagesUseCase
import com.example.suppernanny.domain.chats.usecases.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val sendMessageUseCase: SendMessageUseCase,
  private val observeMessagesUseCase: ObserveMessagesUseCase
) : ViewModel() {

  private val _sendMessageState = MutableStateFlow<State<Boolean>>(State.Default)
  val sendMessageState: StateFlow<State<Boolean>> = _sendMessageState.asStateFlow()

  private val _observeMessageState = MutableStateFlow<State<List<ChatMessage>>>(State.Default)
  val observeMessageState: StateFlow<State<List<ChatMessage>>> = _observeMessageState.asStateFlow()

  private fun sendMessage(message: ChatMessage) {
    viewModelScope.launch {
      val sendMessageState: Result<Unit> = sendMessageUseCase(message)
      when {
        sendMessageState.isSuccess -> _sendMessageState.value = State.Success(true)
        sendMessageState.isFailure -> _sendMessageState.value = State.Error("Failed to send message")
      }
    }
  }

  fun observeMessages(userId: String) {
     viewModelScope.launch {
       observeMessagesUseCase(userId).collectLatest {
         _observeMessageState.value = State.Success(it)
       }
     }
  }

  fun sendTextMessage(senderUid: String, text: String, receiverUid: String = "LLMAgent") {
    val message = ChatMessage(
      senderId = senderUid,
      receiverId = receiverUid,
      message = text,
      timestamp = System.currentTimeMillis(),
      type = "text"
    )
    sendMessage(message)
  }
}