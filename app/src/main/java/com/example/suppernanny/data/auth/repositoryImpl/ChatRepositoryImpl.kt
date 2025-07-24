package com.example.suppernanny.data.auth.repositoryImpl

import android.util.Log
import com.example.suppernanny.domain.chats.models.ChatMessage
import com.example.suppernanny.domain.chats.repository.ChatRepository
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
  database: FirebaseDatabase
) : ChatRepository {

  private val chatsRef = database.getReference()

  override suspend fun sendMessage(message: ChatMessage): Result<Unit> {
    return try {
      val userUid = message.senderId
      val messageId = chatsRef.child("chats").child(userUid).push().key
        ?: return Result.failure(IllegalStateException("Failed to generate message ID"))
      chatsRef.child("chats").child(userUid).child(messageId).setValue(message)
        .await()
      Result.success(Unit)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  override fun observeMessages(userId: String): Flow<List<ChatMessage>> =
    callbackFlow {
      val ref = chatsRef.child("chats").child(userId)
        .orderByChild("timestamp")

      val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          val messages = snapshot.children
            .mapNotNull { it.getValue(ChatMessage::class.java) }
            .sortedBy { it.timestamp }
          trySend(messages).isSuccess
        }

        override fun onCancelled(error: DatabaseError) {
          close(error.toException())
        }
      }

      ref.addValueEventListener(listener)
      awaitClose { ref.removeEventListener(listener) }
    }
}
