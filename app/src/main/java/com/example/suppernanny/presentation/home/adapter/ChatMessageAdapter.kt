package com.example.suppernanny.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.suppernanny.R
import com.example.suppernanny.domain.chats.models.ChatMessage

class ChatMessageAdapter(
    private val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()

    companion object {
        private const val VIEW_TYPE_SENDER_TEXT = 1
        private const val VIEW_TYPE_RECEIVER_TEXT = 2
        private const val VIEW_TYPE_SENDER_IMAGE = 3
        private const val VIEW_TYPE_RECEIVER_IMAGE = 4
    }

    fun submitList(newMessages: List<ChatMessage>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return when {
            message.senderId == currentUserId && message.type == "image" -> VIEW_TYPE_SENDER_IMAGE
            message.senderId == currentUserId -> VIEW_TYPE_SENDER_TEXT
            message.type == "image" -> VIEW_TYPE_RECEIVER_IMAGE
            else -> VIEW_TYPE_RECEIVER_TEXT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENDER_IMAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.right_image_message_item, parent, false)
                SenderImageViewHolder(view)
            }
            VIEW_TYPE_RECEIVER_IMAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.left_image_message_item, parent, false)
                ReceiverImageViewHolder(view)
            }
            VIEW_TYPE_SENDER_TEXT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.right_text_message_item, parent, false)
                SenderTextViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.left_text_message_item, parent, false)
                ReceiverTextViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is SenderTextViewHolder -> holder.bind(message)
            is ReceiverTextViewHolder -> holder.bind(message)
            is SenderImageViewHolder -> holder.bind(message)
            is ReceiverImageViewHolder -> holder.bind(message)
        }
    }

    inner class SenderTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textMessage: TextView = itemView.findViewById(R.id.textMessage)
        fun bind(message: ChatMessage) {
            textMessage.text = message.message
        }
    }

    inner class ReceiverTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textMessage: TextView = itemView.findViewById(R.id.textMessage)
        fun bind(message: ChatMessage) {
            textMessage.text = message.message
        }
    }

    inner class SenderImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageMessage: ImageView = itemView.findViewById(R.id.imageMessage)
        private val imageCaption: TextView = itemView.findViewById(R.id.imageCaption)
        fun bind(message: ChatMessage) {
            Glide.with(itemView).load(message.imageUrl).into(imageMessage)
            if (!message.message.isNullOrBlank()) {
                imageCaption.text = message.message
                imageCaption.visibility = View.VISIBLE
            } else {
                imageCaption.visibility = View.GONE
            }
        }
    }

    inner class ReceiverImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageMessage: ImageView = itemView.findViewById(R.id.imageMessage)
        private val imageCaption: TextView = itemView.findViewById(R.id.imageCaption)
        fun bind(message: ChatMessage) {
            Glide.with(itemView).load(message.imageUrl).into(imageMessage)
            if (!message.message.isNullOrBlank()) {
                imageCaption.text = message.message
                imageCaption.visibility = View.VISIBLE
            } else {
                imageCaption.visibility = View.GONE
            }
        }
    }
}
