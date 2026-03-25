package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private val tabList = listOf("计划", "课程")
    private val planFragment by lazy { PlanItemFragment() }
    private val courseFragment by lazy { CourseFragment() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerViewHorizontal)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val adapter = HorizontalAdapter(tabList)
        recyclerView.adapter = adapter

        // 默认显示第0个（计划Tab）
        switchFragment(0)
        adapter.setSelectPosition(0)

        adapter.onTabClick = { position ->
            switchFragment(position)
        }

        return rootView
    }

    private fun switchFragment(position: Int) {
        val target = if (position == 0) {
            courseFragment // 计划Tab现在显示原来的课程内容
        } else {
            planFragment   // 课程Tab现在显示原来的计划内容
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.HomeContentFragment, target)
            .commitAllowingStateLoss()
    }
}