package com.example.myapplication.ui.personal

import android.annotation.SuppressLint
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
import com.example.myapplication.ui.login.UserViewModel

class PersonalFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var ivAvatar: ImageView
    private lateinit var tvNickname: TextView
    private lateinit var tvDays: TextView

    private lateinit var itemDynamic: LinearLayout
    private lateinit var itemFavorite: LinearLayout
    private lateinit var itemCourses: LinearLayout
    private lateinit var itemOrder: LinearLayout

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_personal, container, false)

        // 绑定控件
        ivAvatar = view.findViewById(R.id.iv_avatar)
        tvNickname = view.findViewById(R.id.tv_nickname)
        tvDays = view.findViewById(R.id.tv_days)

        // 绑定功能项布局
        itemDynamic = view.findViewById(R.id.item_dynamic)
        itemFavorite = view.findViewById(R.id.item_favorite)
        itemCourses = view.findViewById(R.id.item_courses)
        itemOrder = view.findViewById(R.id.item_order)

        // 获取 ViewModel
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // 订阅数据，绑定 UI 控件
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                // 绑定用户基础信息
                tvNickname.text = user.name
                // 绑定运动天数
                tvDays.text = "累计运动 ${user.days} 天"
            } else {
                // 数据为空时的默认显示
                tvNickname.text = "Keep 运动达人"
                tvDays.text = "累计运动 0 天"
            }
        }

        // 设置功能项点击事件
        setupClickListeners()

        return view
    }

    private fun setupClickListeners() {
        // 我的动态
        itemDynamic.setOnClickListener {
            showToast("点击了我的动态")
        }

        // 收藏与加油
        itemFavorite.setOnClickListener {
            showToast("点击了收藏与加油")
        }

        // 我的课程
        itemCourses.setOnClickListener {
            showToast("点击了我的课程")
        }

        // 订单与钱包
        itemOrder.setOnClickListener {
            showToast("点击了订单与钱包")
        }

        // 头像点击
        ivAvatar.setOnClickListener {
            showToast("点击了头像")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // 调用 ViewModel 获取数据
        userViewModel.me()
    }
}