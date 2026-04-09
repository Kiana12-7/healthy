package com.example.myapplication.ui.today

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.data.remote.VitaDataSource
import com.example.myapplication.databinding.FragmentTodayBinding
import com.example.myapplication.ui.ai.AiChatActivity
import com.example.myapplication.ui.today.TodayViewModel
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * 今日课程页面：已同步 CourseItem 模型
 */
class TodayFragment : Fragment(R.layout.fragment_today) {

    private var _binding: FragmentTodayBinding? = null
    private var vitaDataSource = VitaDataSource()
    // 使用这种方式访问 binding，确保 ID 能够被正确解析
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
        // 1. AI 搜索框提问区域
        binding.llAiInput.setOnClickListener {
            val intent = Intent(context, AiChatActivity::class.java).apply {
                putExtra("SOURCE", "search")
            }
            startActivity(intent)
        }

        // 2. 悬浮按钮 - AI 教练
        binding.fabAiCoach.setOnClickListener {
            val intent = Intent(context, AiChatActivity::class.java).apply {
                putExtra("SOURCE", "coach")
            }
            startActivity(intent)
//            lifecycleScope.launch {
//                try {
//                    val response = RetrofitClient.vitaService.generatePlan()
//                    Log.d("TodayFragment", "zhixing")
//                    if (response.isSuccessful) {
//                        // 请求成功，可以更新 UI 或提示用户
//                        Toast.makeText(requireContext(), "计划生成成功", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(requireContext(), "服务器错误: ${response.code()}", Toast.LENGTH_SHORT).show()
//                    }
//                } catch (e: IOException) {
//                    Toast.makeText(requireContext(), "网络异常，请检查网络连接", Toast.LENGTH_SHORT).show()
//                } catch (e: Exception) {
//                    Toast.makeText(requireContext(), "生成失败: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
        }

        // 3. 之前的定制计划等点击事件保持不变...
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