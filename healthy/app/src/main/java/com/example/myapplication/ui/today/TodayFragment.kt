package com.example.myapplication.ui.today

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentTodayBinding

class TodayFragment : Fragment(R.layout.fragment_today) {

    private var _binding: FragmentTodayBinding? = null
    // 使用这种方式访问 binding，确保 ID 能够被正确解析
    private val binding get() = _binding!!

    // 提示：确保 build.gradle 有 implementation("androidx.fragment:fragment-ktx:1.6.2")
    private val viewModel: TodayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 关键：绑定布局
        _binding = FragmentTodayBinding.bind(view)

        // 1. 初始化列表
        setupRecyclerViews()

        // 2. 观察 LiveData 动态切换 UI
        viewModel.courseList.observe(viewLifecycleOwner) { courses ->
            if (courses.isNullOrEmpty()) {
                // 如果没有数据，隐藏列表，显示缺省布局
                binding.rvTodayCourses.visibility = View.GONE
                binding.llEmptyState.visibility = View.VISIBLE
            } else {
                // 如果有数据，显示列表，隐藏缺省布局
                binding.rvTodayCourses.visibility = View.VISIBLE
                binding.llEmptyState.visibility = View.GONE
                (binding.rvTodayCourses.adapter as? TodayCourseAdapter)?.submitList(courses)
            }
        }

        // 3. 事件监听
        initClickListeners()

        // 4. 加载日历
        setupCalendar()
    }

    private fun setupRecyclerViews() {
        val courseAdapter = TodayCourseAdapter()
        binding.rvTodayCourses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = courseAdapter
        }

        // 如果你有第二个 RecyclerView (全部课程)
        binding.rvAllCourses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TodayCourseAdapter() // 或者你专门的 Adapter
        }
    }

    private fun initClickListeners() {
        // AI 提问区域点击
        binding.llAiInput.setOnClickListener {
            // 这里执行跳转或弹出对话框逻辑
        }

        // 定制计划按钮点击 (对应 XML 里的 btn_customize)
        binding.btnCustomize.setOnClickListener {
            // 点击后的操作
        }

        // 悬浮按钮点击
        binding.fabAiCoach.setOnClickListener {
            // AI 教练逻辑
        }
    }

    private fun setupCalendar() {
        binding.rvCalendar.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        // 此处等你有 CalendarAdapter 后再设置适配器
        // binding.rvCalendar.adapter = CalendarAdapter(...)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 必须置空以防内存泄漏
        _binding = null
    }
}