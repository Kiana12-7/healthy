package com.example.myapplication.ui.personal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.ui.fitness.StatisticsActivity
import com.example.myapplication.ui.login.UserViewModel

class PersonalFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var ivAvatar: ImageView
    private lateinit var tvNickname: TextView
    private lateinit var tvDays: TextView
    private lateinit var itemCourses: LinearLayout
    private var itemMyData: LinearLayout? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            val view = inflater.inflate(R.layout.fragment_personal, container, false)
            ivAvatar = view.findViewById(R.id.iv_avatar)
            tvNickname = view.findViewById(R.id.tv_nickname)
            tvDays = view.findViewById(R.id.tv_days)
            itemCourses = view.findViewById(R.id.item_courses)
            itemMyData = view.findViewById(R.id.item_my_data)

            userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

            userViewModel.user.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    tvNickname.text = user.name
                    tvDays.text = "累计运动 ${user.days} 天"
                } else {
                    tvNickname.text = "Keep 运动达人"
                    tvDays.text = "累计运动 0 天"
                }
            }

            view
        } catch (e: Exception) {
            e.printStackTrace()
            // 如果布局加载失败，返回一个简单视图避免崩溃
            TextView(requireContext()).apply {
                text = "页面加载失败，请检查布局资源"
                setPadding(50, 50, 50, 50)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        itemCourses.setOnClickListener {
            showToast("点击了我的课程")
        }

        itemMyData?.setOnClickListener {
            try {
                val intent = Intent(requireContext(), StatisticsActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                showToast("无法打开统计数据页面，请检查 Activity 是否注册")
                e.printStackTrace()
            }
        }

        ivAvatar.setOnClickListener {
            showToast("点击了头像")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        try {
            userViewModel.me()
        } catch (e: Exception) {
            e.printStackTrace()
            // 不崩溃，仅记录日志
        }
    }
}