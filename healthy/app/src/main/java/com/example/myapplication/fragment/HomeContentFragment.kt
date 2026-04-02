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

    // 提取 Adapter 为成员变量，避免重复创建
    private var homeAdapter: HomeAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeContentBinding.bind(view)

        // 1. 获取共享的 ViewModel
        val viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        // 2. 预先配置 RecyclerView
        setupRecyclerView()

        // 3. 观察数据
        viewModel.homeData.observe(viewLifecycleOwner) { videos ->
            if (!videos.isNullOrEmpty()) {
                binding.tvPlaceholder.visibility = View.GONE
                binding.rvVideoList.visibility = View.VISIBLE

                // 【核心改进】：如果 adapter 已存在则刷新，不存在则创建
                homeAdapter = HomeAdapter(videos) { video ->
                    val intent = Intent(requireContext(), VideoDetailActivity::class.java).apply {
                        putExtra("VIDEO_URL", video.videoUrl)
                        putExtra("VIDEO_TITLE", video.title)
                    }
                    startActivity(intent)
                }
                binding.rvVideoList.adapter = homeAdapter

                // 强制要求布局重绘，解决瀑布流不显示的问题
                binding.rvVideoList.requestLayout()
            } else {
                binding.tvPlaceholder.visibility = View.VISIBLE
                binding.rvVideoList.visibility = View.GONE
            }
        }

        // 4. 加载数据
        viewModel.fetchHomeData()
    }

    private fun setupRecyclerView() {
        val staggeredManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredManager.gapStrategy = 2
        binding.rvVideoList.apply {
            layoutManager = staggeredManager
            // 解决 NestedScrollView 嵌套导致的滑动和显示冲突
            isNestedScrollingEnabled = false
            setHasFixedSize(false) // 瀑布流高度不固定，设为 false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}