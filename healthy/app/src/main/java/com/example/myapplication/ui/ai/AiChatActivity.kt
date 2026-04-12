package com.example.myapplication.ui.ai

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.ChatMessage
import com.example.myapplication.databinding.ActivityAiChatBinding

class AiChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAiChatBinding
    private lateinit var chatAdapter: ChatAdapter // 统一放在顶部声明

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 获取来源信息
        val source = intent.getStringExtra("SOURCE") ?: "default"

        // 2. 初始化视图逻辑（现在只有一个 initView 了）
        initView()

        // 3. 自动弹出键盘处理
        if (source == "search") {
            binding.etMessage.requestFocus()
            // 提示：如果要强制弹出软键盘，通常还需要加上 WindowInsetsController 代码
        }
    }

    /**
     * 将之前的两个 initView 合并为一个，解决冲突
     */
    private fun initView() {
        // 初始化适配器
        chatAdapter = ChatAdapter()
        binding.rvChatMessages.apply {
            layoutManager = LinearLayoutManager(this@AiChatActivity)
            adapter = chatAdapter
        }

        // 返回按钮逻辑
        binding.ivBack.setOnClickListener { finish() }

        // 发送按钮逻辑
        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text.toString()
            if (text.isNotBlank()) {
                // 执行发送逻辑
                performSendMessage(text)
                binding.etMessage.text.clear()
            }
        }
    }

    /**
     * 处理发送消息的逻辑
     */
    private fun performSendMessage(text: String) {
        // 1. 展示用户发送的消息
        val userMsg = ChatMessage(text, true)
        chatAdapter.addMessage(userMsg)

        // 自动滚动到底部
        binding.rvChatMessages.scrollToPosition(chatAdapter.itemCount - 1)

        // 2. 模拟 AI 自动回复（后续可在此接入 Gemini 接口）
        simulateAiResponse(text)
    }

    /**
     * 模拟延时回复
     */
    private fun simulateAiResponse(userText: String) {
        binding.root.postDelayed({
            val aiMsg = ChatMessage("收到你的问题：'$userText'。作为你的教练，我建议先热身 5 分钟。", false)
            chatAdapter.addMessage(aiMsg)
            binding.rvChatMessages.scrollToPosition(chatAdapter.itemCount - 1)
        }, 1000)
    }
}