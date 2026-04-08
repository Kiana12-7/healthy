package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.home.course.CourseFragment
import com.example.myapplication.ui.home.plan.planItem.PlanItemFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // 删除推荐，只保留课程、计划（索引0、1）
    private val tabTitles = listOf("课程", "计划")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        // ViewPager2适配器，只加载课程和计划
        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = tabTitles.size

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> CourseFragment() // 索引0：课程
                    1 -> PlanItemFragment() // 索引1：计划
                    else -> PlanItemFragment()
                }
            }
        }

        binding.homeViewPager.adapter = adapter

        // 默认显示“计划”页（索引为1），方便测试
        binding.homeViewPager.setCurrentItem(1, false)

        // 3. 将 TabLayout 和 ViewPager2 绑定（完全保留你原来的代码）
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/**
 * 通用的占位 Fragment
 */
class PlaceholderFragment : Fragment(R.layout.fragment_plan_content) {
    companion object {
        fun newInstance(title: String): PlaceholderFragment {
            val fragment = PlaceholderFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tv_placeholder)?.text =
            arguments?.getString("title") ?: "未命名页面"
    }
}