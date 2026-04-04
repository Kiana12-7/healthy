package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.home.course.CourseFragment
import com.example.myapplication.ui.home.plan.PlanContentFragment
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

                    // 【修复】：索引 1 对应“课程”，显示你要求的那个 Keep 风格布局
                    1 -> CourseFragment()

                    // 【修复】：索引 2 对应“计划”
                    2 -> PlanContentFragment()

                    else -> PlaceholderFragment.newInstance(tabTitles[position])
                }
            }
        }

        binding.homeViewPager.adapter = adapter

        // 【优化】：设置默认显示“课程”页（索引为 1）
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
 * 注意：需要在 fragment_home_content.xml 中确保有一个 ID 为 tv_placeholder 的 TextView，
 * 或者你随便找个存在的 TextView ID 替换掉。
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
        // 尝试寻找 TextView 并显示标题内容
        // 如果你的 fragment_home_content 里没有这个 ID，程序不会崩但文字不会变
        view.findViewById<TextView>(R.id.tv_placeholder)?.text =
            arguments?.getString("title") ?: "未命名页面"
    }
}