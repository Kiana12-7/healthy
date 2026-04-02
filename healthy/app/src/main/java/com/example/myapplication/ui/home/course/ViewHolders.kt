package com.example.myapplication.ui.home.course

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.data.model.HomeItem
import com.example.myapplication.databinding.ItemVideoBinding

/**
 * 视频列表项的 ViewHolder
 * 负责将具体的视频数据（标题、作者、时长、标签、封面）绑定到 item_video 布局上
 */
class VideoViewHolder(
    private val binding: ItemVideoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeItem.Video, onVideoClick: (HomeItem.Video) -> Unit) {
        // 1. 设置文字内容
        binding.tvTitle.text = item.title
        binding.tvAuthor.text = item.author
        binding.tvDuration.text = item.duration
        binding.tvTag.text = item.tag

        // 2. 使用 Coil 加载封面图
        // 确保你的 HomeViewModel 提供的 coverUrl 是有效的
        binding.imgCover.load(item.coverUrl) {
            crossfade(true)
            // 如果加载本地视频封面，Coil 也能自动处理部分格式
        }

        // 3. 绑定整个条目的点击事件，用于跳转播放
        binding.root.setOnClickListener {
            onVideoClick(item)
        }
    }
}

// 注意：按照你“第二张图”的需求，BannerViewHolder 暂时不再被课程页列表使用
// 如果后续其他页面需要，可以保留；如果追求代码精简，可以移除。