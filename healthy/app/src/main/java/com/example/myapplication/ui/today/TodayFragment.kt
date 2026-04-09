package com.example.myapplication.ui.today

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentTodayBinding
import com.example.myapplication.ui.today.TodayViewModel
/**
 * 今日课程页面：已同步 CourseItem 模型
 */
class TodayFragment : Fragment(R.layout.fragment_today) {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    // 确保导入了 androidx.fragment:fragment-ktx 依赖
    private val viewModel: TodayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTodayBinding.bind(view)

        // 1. 初始化列表 (设置 Adapter)
        setupRecyclerViews()

        // 2. 观察 LiveData (处理后端返回的真实数据)
        viewModel.courseList.observe(viewLifecycleOwner) { courses ->
            // courses 现在是 List<CourseItem> 类型
            if (courses.isNullOrEmpty()) {
                binding.rvTodayCourses.visibility = View.GONE
                binding.llEmptyState.visibility = View.VISIBLE
            } else {
                binding.rvTodayCourses.visibility = View.VISIBLE
                binding.llEmptyState.visibility = View.GONE

                // 关键点：使用我们合并冲突后的 TodayCourseAdapter
                // submitList 是 ListAdapter 的标准方法，不需要强转
                (binding.rvTodayCourses.adapter as? TodayCourseAdapter)?.submitList(courses)
                (binding.rvAllCourses.adapter as? TodayCourseAdapter)?.submitList(courses)
            }
        }

        // 3. 事件监听
        initClickListeners()

        // 4. 加载日历
        setupCalendar()
    }

    private fun setupRecyclerViews() {
        // 今日课程列表
        binding.rvTodayCourses.apply {
            layoutManager = LinearLayoutManager(context)
            // 确保这里实例化的是我们最新的 TodayCourseAdapter
            adapter = TodayCourseAdapter()
        }

        // 全部课程列表
        binding.rvAllCourses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TodayCourseAdapter()
        }
    }

    private fun initClickListeners() {
        binding.llAiInput.setOnClickListener {
            // 跳转 AI 助手页面逻辑
        }

        binding.btnCustomize.setOnClickListener {
            // 定制计划逻辑
        }

        binding.fabAiCoach.setOnClickListener {
            // 悬浮 AI 教练逻辑
        }
    }

    private fun setupCalendar() {
        // 设置水平滑动的日历
        binding.rvCalendar.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        // TODO: 待 CalendarAdapter 完成后在此绑定
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 释放绑定防止内存泄漏
    }
}