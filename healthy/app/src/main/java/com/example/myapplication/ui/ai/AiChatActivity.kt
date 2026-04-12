package com.example.myapplication.ui.ai

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.ChatMessage
import com.example.myapplication.data.model.Result
import com.example.myapplication.data.remote.VitaDataSource
import com.example.myapplication.databinding.ActivityAiChatBinding
import kotlinx.coroutines.launch

class AiChatActivity : AppCompatActivity() {
    companion object {
        private const val PLAN_KEYWORD = "制定计划"
    }

    private lateinit var binding: ActivityAiChatBinding
    private lateinit var chatAdapter: ChatAdapter // 统一放在顶部声明
    private val vitaDataSource = VitaDataSource()

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
        val userMsg = ChatMessage(text, true)
        chatAdapter.addMessage(userMsg)
        binding.rvChatMessages.scrollToPosition(chatAdapter.itemCount - 1)
        requestAiReply(text)
    }

    private fun requestAiReply(userText: String) {
        lifecycleScope.launch {
            binding.btnSend.isEnabled = false
            val originalText = binding.btnSend.text
            binding.btnSend.text = "发送中"

            val result = if (userText.contains(PLAN_KEYWORD)) {
                vitaDataSource.generatePlan().let { planResult ->
                    when (planResult) {
                        is Result.Success -> Result.Success("已根据你的健身信息开始生成训练计划，请稍后到“今日”页面查看最新安排。")
                        is Result.Error -> Result.Error(planResult.exception)
                    }
                }
            } else {
                vitaDataSource.chat(userText)
            }

            val aiMsg = when (result) {
                is Result.Success -> ChatMessage(result.data, false)
                is Result.Error -> ChatMessage(result.exception.message ?: "请求失败，请稍后再试。", false)
            }
            chatAdapter.addMessage(aiMsg)
            binding.rvChatMessages.scrollToPosition(chatAdapter.itemCount - 1)
            binding.btnSend.text = originalText
            binding.btnSend.isEnabled = true
        }
    }
}
