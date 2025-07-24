package com.example.suppernanny.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.suppernanny.R
import com.example.suppernanny.domain.chats.models.ChatMessage

class ChatMessageAdapter(
    private val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()

    companion object {
        private const val VIEW_TYPE_SENDER = 1
        private const val VIEW_TYPE_RECEIVER = 2
    }

    fun submitList(newMessages: List<ChatMessage>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == currentUserId) {
            VIEW_TYPE_SENDER
        } else {
            VIEW_TYPE_RECEIVER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENDER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.right_text_message_item, parent, false)
            SenderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.left_text_message_item, parent, false)
            ReceiverViewHolder(view)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SenderViewHolder) {
            holder.bind(message)
        } else if (holder is ReceiverViewHolder) {
            holder.bind(message)
        }
    }

    inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textMessage: TextView = itemView.findViewById(R.id.textMessage)

        fun bind(message: ChatMessage) {
            textMessage.text = message.message
        }
    }

    inner class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textMessage: TextView = itemView.findViewById(R.id.textMessage)

        fun bind(message: ChatMessage) {
            textMessage.text = message.message
        }
    }
}
