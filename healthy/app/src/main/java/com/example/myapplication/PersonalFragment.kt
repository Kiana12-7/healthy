package com.example.myapplication

import com.example.myapplication.data.service.UserViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class PersonalFragment : Fragment() {
    // 直接获取当前页面的 ViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var tvName: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_personal, container, false)
        // 绑定控件
        tvName = view.findViewById<TextView>(R.id.textView1)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // 订阅请求数据，状态管理
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                tvName.text = "用户名：${user.name}"
            } else {
                tvName.text = "用户名："
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // 调用viewModel中的方法，解除耦合性
        userViewModel.me()
    }
}
