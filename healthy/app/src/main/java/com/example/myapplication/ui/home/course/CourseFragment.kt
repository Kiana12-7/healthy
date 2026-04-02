package com.example.myapplication.ui.home.course

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.myapplication.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.databinding.FragmentCourseBinding
import com.example.myapplication.ui.video.VideoDetailActivity

class CourseFragment : Fragment(R.layout.fragment_course) {

    private var _binding: FragmentCourseBinding? = null
    private val binding get() = _binding!!

    private var courseAdapter: CourseAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCourseBinding.bind(view)

        // 获取共享的 ViewModel
        val viewModel = ViewModelProvider(requireActivity())[CourseModel::class.java]

        // 1. 预先配置 RecyclerView
        setupRecyclerView()

        // 2. 观察数据
        viewModel.homeData.observe(viewLifecycleOwner) { videos ->
            if (!videos.isNullOrEmpty()) {
                // 【修复核心】：确保这里使用的 ID 与 XML 中的 android:id="@+id/tv_placeholder" 对应
                // ViewBinding 会自动把下划线命名的 ID 转换成驼峰命名
                binding.tvPlaceholder.visibility = View.GONE
                binding.rvVideoList.visibility = View.VISIBLE

                // 【性能优化】：如果 adapter 为空则创建，不为空则只更新数据
                if (courseAdapter == null) {
                    courseAdapter = CourseAdapter(videos) { video ->
                        val intent =
                            Intent(requireContext(), VideoDetailActivity::class.java).apply {
                                putExtra("VIDEO_URL", video.videoUrl)
                                putExtra("VIDEO_TITLE", video.title)
                            }
                        startActivity(intent)
                    }
                    binding.rvVideoList.adapter = courseAdapter
                } else {
                    // 如果你的 courseAdapter 里写了更新方法，调用它；
                    // 如果没写，暂时先这样，或者重新赋值一次
                    binding.rvVideoList.adapter = courseAdapter
                }

                binding.rvVideoList.requestLayout()
            } else {
                binding.tvPlaceholder.visibility = View.VISIBLE
                binding.rvVideoList.visibility = View.GONE
            }
        }

        // 加载数据
        viewModel.fetchHomeData()
    }

    private fun setupRecyclerView() {
        // 瀑布流布局
        val staggeredManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        // 关键：防止瀑布流 Item 乱跳
        staggeredManager.gapStrategy = 2

        binding.rvVideoList.apply {
            layoutManager = staggeredManager
            isNestedScrollingEnabled = false
            setHasFixedSize(false)
        }
    }
}