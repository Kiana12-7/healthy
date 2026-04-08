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

    // 1. 定义标签页（索引分别为 0, 1, 2）
    private val tabTitles = listOf("推荐", "课程", "计划")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        // 2. 设置 ViewPager2 的适配器
        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = tabTitles.size

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> PlaceholderFragment.newInstance("推荐页面建设中...")

                    1 -> CourseFragment()

                    2 -> PlanItemFragment()

                    else -> PlaceholderFragment.newInstance(tabTitles[position])
                }
            }
        }

        binding.homeViewPager.adapter = adapter

        // 设置默认显示“课程”页（索引为 1）
        binding.homeViewPager.setCurrentItem(1, false)

        // 3. 将 TabLayout 和 ViewPager2 绑定
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
 * 计划页面：目前复用 fragment_home_content 布局
 */
class PlanFragment : Fragment(R.layout.fragment_plan_content) {
    // 后续可以在这里通过 binding 初始化计划页面的特有逻辑
}

/**
 * 通用的占位 Fragment：用于还没开发的页面（如“推荐”）
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