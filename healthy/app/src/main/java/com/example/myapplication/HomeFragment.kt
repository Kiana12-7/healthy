package com.example.myapplication

import HorizontalAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 1. 加载布局文件（完全保留你原来的逻辑）
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 2. 从当前 Fragment 的视图中查找 RecyclerView（完全保留）
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewHorizontal)

        // 3. 设置水平布局管理器（完全保留，滑动效果就在这）
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        // 4. 【唯一修改的地方】准备数据：把原来的 Item 1-20 换成你要的4个名字
        val data = listOf("推荐", "计划", "课程", "社区")

        // 5. 设置适配器（完全保留你原来的 HorizontalAdapter）
        val adapter = HorizontalAdapter(data)
        recyclerView.adapter = adapter

        // 6. 返回填充好的视图（完全保留）
        return view
    }
}