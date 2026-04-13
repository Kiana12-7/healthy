package com.example.myapplication.ui.ai

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.ChatMessage
import com.example.myapplication.data.model.Result
import com.example.myapplication.data.remote.VitaDataSource
import com.example.myapplication.databinding.ActivityAiChatBinding
import kotlinx.coroutines.launch
import com.example.myapplication.R

class AiChatActivity : AppCompatActivity() {
    companion object {
        private const val PLAN_KEYWORD = "制定计划"
        // AI自我介绍固定话术
        private const val AI_INTRO_CONTENT = "我是你的专属AI健身教练，有任何健身相关的问题，都可以随时问我~"
    }

    private lateinit var binding: ActivityAiChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val vitaDataSource = VitaDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 获取来源信息
        val source = intent.getStringExtra("SOURCE") ?: "default"

        // 2. 初始化视图逻辑
        initView()

        // 3. 初始化发送按钮状态监听
        initSendButtonStateListener()

        // 4. 页面初始化完成后，自动添加AI自我介绍
        initAiIntroMessage()

        // 5. 自动弹出键盘处理
        if (source == "search") {
            binding.etMessage.requestFocus()
        }
    }

    /**
     * 初始化视图与交互逻辑
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

        // 发送按钮点击逻辑
        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text.toString()
            if (text.isNotBlank()) {
                performSendMessage(text)
                binding.etMessage.text.clear()
            }
        }
    }

    /**
     * 【核心新增】监听输入框内容，动态切换发送按钮状态
     */
    private fun initSendButtonStateListener() {
        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 输入框有内容 → 激活深色可点击状态
                if (!s.isNullOrBlank()) {
                    binding.btnSend.apply {
                        isClickable = true
                        isFocusable = true
                        setBackgroundResource(R.drawable.bg_send_button_enable)
                        imageTintList = getColorStateList(android.R.color.white)
                    }
                }
                // 输入框无内容 → 禁用浅色不可点击状态
                else {
                    binding.btnSend.apply {
                        isClickable = false
                        isFocusable = false
                        setBackgroundResource(R.drawable.bg_send_button_disable)
                        imageTintList = getColorStateList(android.R.color.darker_gray)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * 初始化AI开场自我介绍
     */
    private fun initAiIntroMessage() {
        // 页面创建后，自动添加AI自我介绍消息
        val introMessage = ChatMessage(AI_INTRO_CONTENT, isFromUser = false)
        chatAdapter.addMessage(introMessage)
        // 自动滚动到最新消息
        binding.rvChatMessages.scrollToPosition(chatAdapter.itemCount - 1)
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
            // 请求时禁用发送按钮，避免重复点击
            binding.btnSend.isEnabled = false
            binding.btnSend.alpha = 0.5f

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

            // 请求完成恢复按钮状态
            binding.btnSend.isEnabled = true
            binding.btnSend.alpha = 1f
        }
    }
}