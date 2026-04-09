package com.example.myapplication.ui.ai

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.ChatMessage
import com.example.myapplication.databinding.ItemChatLeftBinding
import com.example.myapplication.databinding.ItemChatRightBinding

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()

    companion object {
        private const val TYPE_USER = 1
        private const val TYPE_AI = 2
    }

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isFromUser) TYPE_USER else TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_USER) {
            UserViewHolder(ItemChatRightBinding.inflate(inflater, parent, false))
        } else {
            AiViewHolder(ItemChatLeftBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messages[position]
        if (holder is UserViewHolder) holder.bind(msg)
        else if (holder is AiViewHolder) holder.bind(msg)
    }

    override fun getItemCount() = messages.size

    class UserViewHolder(private val binding: ItemChatRightBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(msg: ChatMessage) { binding.tvContent.text = msg.content }
    }

    class AiViewHolder(private val binding: ItemChatLeftBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(msg: ChatMessage) { binding.tvContent.text = msg.content }
    }
}