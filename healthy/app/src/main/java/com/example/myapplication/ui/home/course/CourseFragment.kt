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

/**
 * 课程页面 Fragment
 * 展示视频列表（瀑布流布局）
 */
class CourseFragment : Fragment(R.layout.fragment_course) {
    // ViewBinding 实例，使用可空变量防止内存泄漏
    private var _binding: FragmentCourseBinding? = null
    // 非空获取，简化调用
    private val binding get() = _binding!!

    // 视频列表适配器
    private var courseAdapter: CourseAdapter? = null

    /**
     * 视图创建完成后调用
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 绑定视图
        _binding = FragmentCourseBinding.bind(view)

        // 获取与 Activity 共享的 ViewModel（跨页面数据共享）
        val viewModel = ViewModelProvider(requireActivity())[CourseModel::class.java]

        // 配置 RecyclerView 基础布局
        setupRecyclerView()

        // 观察 ViewModel 中的视频数据，数据变化时更新 UI
        viewModel.homeData.observe(viewLifecycleOwner) { videos ->
            if (!videos.isNullOrEmpty()) {
                // 有数据 → 隐藏占位提示，显示列表
                binding.tvPlaceholder.visibility = View.GONE
                binding.rvVideoList.visibility = View.VISIBLE

                // 适配器为空则创建，不为空则直接使用
                if (courseAdapter == null) {
                    // 创建适配器，并设置条目点击事件
                    courseAdapter = CourseAdapter(videos) { video ->
                        // 点击条目 → 跳转到视频详情页
                        val intent =
                            Intent(requireContext(), VideoDetailActivity::class.java).apply {
                                // 传递视频地址和标题
                                putExtra("VIDEO_URL", video.videoUrl)
                                putExtra("VIDEO_TITLE", video.title)
                            }
                        startActivity(intent)
                    }
                    // 设置适配器
                    binding.rvVideoList.adapter = courseAdapter
                } else {
                    // 已有适配器，直接复用
                    binding.rvVideoList.adapter = courseAdapter
                }

                // 重新请求布局，刷新列表显示
                binding.rvVideoList.requestLayout()
            } else {
                // 无数据 → 显示占位提示，隐藏列表
                binding.tvPlaceholder.visibility = View.VISIBLE
                binding.rvVideoList.visibility = View.GONE
            }
        }

        // 触发 ViewModel 加载数据
        viewModel.fetchVideoListFromServer()
    }

    /**
     * 配置 RecyclerView（瀑布流布局）
     */
    private fun setupRecyclerView() {
        // 创建瀑布流布局管理器：2列，垂直方向
        val staggeredManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        // 设置间隙策略，防止列表条目乱序、跳动
        staggeredManager.gapStrategy = 2

        // 对 RecyclerView 进行配置
        binding.rvVideoList.apply {
            layoutManager = staggeredManager       // 设置布局管理器
            isNestedScrollingEnabled = false       // 关闭嵌套滚动，避免滑动冲突
            setHasFixedSize(false)                 // 不固定大小，支持瀑布流自适应
        }
    }

    /**
     * 页面销毁时释放 ViewBinding，防止内存泄漏
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}