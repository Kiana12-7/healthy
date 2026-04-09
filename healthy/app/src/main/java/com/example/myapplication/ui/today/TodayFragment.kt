package com.example.myapplication.ui.today

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentTodayBinding
import com.example.myapplication.ui.ai.AiChatActivity
import com.example.myapplication.data.model.CourseItem // 必须导入这个
// import com.example.myapplication.ui.video.VideoPlayActivity // 导入你的播放页面

class TodayFragment : Fragment(R.layout.fragment_today) {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarAdapter: CalendarAdapter
    private val viewModel: TodayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTodayBinding.bind(view)

        setupCalendar()
        setupRecyclerViews() // 这里面包含了点击逻辑
        initClickListeners()

        // 观察日历
        viewModel.calendarDays.observe(viewLifecycleOwner) { days ->
            calendarAdapter.submitList(days)
        }

        // 观察滚动
        // 在 TodayFragment.kt 的 onViewCreated 中
        viewModel.scrollToPosition.observe(viewLifecycleOwner) { position ->
            position?.let {
                // 使用 post 确保在界面渲染出来的第一时刻进行“闪现”滚动
                binding.rvCalendar.post {
                    val layoutManager = binding.rvCalendar.layoutManager as? LinearLayoutManager
                    // 关键：第一个参数是索引，第二个参数 0 表示把这个索引的对象对齐到屏幕最左侧
                    // 配合 ViewModel 里的 (todayIndex - 1)，就能实现“今天在第二位”
                    layoutManager?.scrollToPositionWithOffset(it, 0)
                }
            }
        }

        // 观察课程列表
        viewModel.courseList.observe(viewLifecycleOwner) { courses: List<CourseItem>? ->
            if (courses.isNullOrEmpty()) {
                binding.rvTodayCourses.visibility = View.GONE
                binding.llEmptyState.visibility = View.VISIBLE
            } else {
                binding.rvTodayCourses.visibility = View.VISIBLE
                binding.llEmptyState.visibility = View.GONE

                (binding.rvTodayCourses.adapter as? TodayCourseAdapter)?.submitList(courses)
                (binding.rvAllCourses.adapter as? TodayCourseAdapter)?.submitList(courses)
            }
        }
    }

    private fun setupRecyclerViews() {
        // 定义点击处理逻辑，解决 Cannot infer type 和 Unresolved reference 报错
        val courseAdapter = TodayCourseAdapter { course: CourseItem ->
            if (course is CourseItem.TrainingVideo) {
                // 这里的跳转逻辑你可以根据实际播放页面的类名修改
                // val intent = Intent(requireContext(), VideoPlayActivity::class.java).apply {
                //     putExtra("VIDEO_URL", course.videoUrl)
                //     putExtra("VIDEO_TITLE", course.title)
                // }
                // startActivity(intent)
            }
        }

        binding.rvTodayCourses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = courseAdapter
            isNestedScrollingEnabled = false
        }

        binding.rvAllCourses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TodayCourseAdapter { /* 下方列表的点击逻辑 */ }
            isNestedScrollingEnabled = false
        }
    }

    private fun setupCalendar() {
        calendarAdapter = CalendarAdapter { selectedDay, position ->
            viewModel.onDateSelected(selectedDay, position)
        }
        binding.rvCalendar.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = calendarAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun initClickListeners() {
        binding.llAiInput.setOnClickListener {
            startActivity(Intent(context, AiChatActivity::class.java).apply { putExtra("SOURCE", "search") })
        }
        binding.fabAiCoach.setOnClickListener {
            startActivity(Intent(context, AiChatActivity::class.java).apply { putExtra("SOURCE", "coach") })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}