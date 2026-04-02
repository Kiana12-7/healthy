package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HorizontalAdapter
import com.example.myapplication.R

class HomeFragment : Fragment() {


    private val pageConfig = listOf(

        "计划" to PlanItemFragment::class.java,  // 计划Tab -> 计划页面
        "课程" to CourseFragment::class.java,      // 课程Tab -> 课程页面

    )

    private val tabNames = pageConfig.map { it.first }
    // 缓存Fragment，避免重复创建
    private val fragmentCache = mutableMapOf<Int, Fragment>()

    // Fragment 绘制界面的方法
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerViewHorizontal)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val adapter = HorizontalAdapter(tabNames)
        recyclerView.adapter = adapter

        // 默认显示第0个
        switchFragment(0)
        adapter.setSelectPosition(0)

        adapter.onTabClick = { position ->
            switchFragment(position)
        }

        return rootView
    }

    private fun switchFragment(position: Int) {
        // 从缓存取，如果没有就new一个
        val fragment = fragmentCache.getOrPut(position) {
            pageConfig[position].second.newInstance()
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.HomeContentFragment, fragment)
            .commitAllowingStateLoss()
    }
}