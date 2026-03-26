package com.example.myapplication.fragment

import com.example.myapplication.adapter.HorizontalAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class HomeFragment : Fragment() {
    // Fragment 绘制界面的方法
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 1. 加载布局文件
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 2. 从当前 Fragment 的视图中查找 RecyclerView（列表）
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewHorizontal)

        // 3. 设置水平布局管理器（水平滚动）
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // 4. 准备模拟数据
        val data = mutableListOf<String>()
        for (i in 1..20) {
            data.add("Item $i")
        }

        // 5. 设置适配器（确保 HorizontalAdapter 已定义），把数据变成列表项
        recyclerView.adapter = HorizontalAdapter(data)

        // 6. 返回填充好的视图
        return view
    }
}