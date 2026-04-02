package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.HorizontalAdapter
import com.example.myapplication.R
import com.example.myapplication.adapter.PlanAdapter
import com.example.myapplication.model.PlanItem

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

        val fragment = fragmentCache.getOrPut(position) {
            pageConfig[position].second.newInstance()
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.HomeContentFragment, fragment)
            .commitAllowingStateLoss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val recyclerViewHorizontal = view.findViewById<RecyclerView>(R.id.recyclerViewHorizontal)
//
//        // 安全判断：如果找不到控件，直接跳过，不会崩溃
//        if (recyclerViewHorizontal != null) {
//            recyclerViewHorizontal.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//
//            val list = listOf("全部", "减脂", "增肌", "有氧", "舒缓")
//            val adapter = HorizontalAdapter(list)
//            recyclerViewHorizontal.adapter = adapter
//        }
    }
}