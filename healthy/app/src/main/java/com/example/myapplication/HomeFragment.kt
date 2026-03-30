package com.example.myapplication

import HorizontalAdapter
import com.example.myapplication.data.service.UserViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.mutableListOf

class HomeFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 1. 加载布局文件
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        // 2. 从当前 Fragment 的视图中查找 RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewHorizontal)

        // 3. 设置水平布局管理器
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        // 4. 准备数据
        val data = mutableListOf<String>()
        for (i in 1..20) {
            data.add("Item $i")
        }

        // 5. 设置适配器（确保 HorizontalAdapter 已定义）
        val adapter = HorizontalAdapter(data)
        recyclerView.adapter = adapter

        userViewModel.me()
        // 6. 返回填充好的视图
        return view
    }
}