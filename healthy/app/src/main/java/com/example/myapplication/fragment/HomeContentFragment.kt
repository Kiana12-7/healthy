package com.example.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeContentBinding
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.ui.home.adapter.HomeAdapter
import com.example.myapplication.ui.video.VideoDetailActivity

class HomeContentFragment : Fragment(R.layout.fragment_home_content) {

    private var _binding: FragmentHomeContentBinding? = null
    private val binding get() = _binding!!

    private var homeAdapter: HomeAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeContentBinding.bind(view)

        // 获取共享的 ViewModel
        val viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

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
                if (homeAdapter == null) {
                    homeAdapter = HomeAdapter(videos) { video ->
                        val intent = Intent(requireContext(), VideoDetailActivity::class.java).apply {
                            putExtra("VIDEO_URL", video.videoUrl)
                            putExtra("VIDEO_TITLE", video.title)
                        }
                        startActivity(intent)
                    }
                    binding.rvVideoList.adapter = homeAdapter
                } else {
                    // 如果你的 HomeAdapter 里写了更新方法，调用它；
                    // 如果没写，暂时先这样，或者重新赋值一次
                    binding.rvVideoList.adapter = homeAdapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}