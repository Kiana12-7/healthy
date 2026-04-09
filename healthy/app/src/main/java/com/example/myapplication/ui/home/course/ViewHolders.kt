package com.example.myapplication.ui.home.course

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.data.model.CourseItem
import com.example.myapplication.databinding.ItemVideoBinding

/**
 * 视频列表项的 ViewHolder
 * 负责将具体的视频数据（标题、作者、时长、标签、封面）绑定到 item_video 布局上
 */
class VideoViewHolder(
    private val binding: ItemVideoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CourseItem.TrainingVideo, onVideoClick: (CourseItem.TrainingVideo) -> Unit) {
        // 1. 设置文字内容
        binding.tvTitle.text = item.title
        binding.tvAuthor.text = item.trainerName
        binding.tvDuration.text = item.duration.toString()
        binding.tvTag.text = item.difficultyTag

        binding.tvDuration.text = formatDuration(item.duration)

        // 2. 使用 Coil 加载封面图
        binding.imgCover.load(item.coverUrl) {
            crossfade(true)
        }

        // 3. 绑定整个条目的点击事件，用于跳转播放
        binding.root.setOnClickListener {
            onVideoClick(item)
        }
    }

    // 秒数 转 00:15 秒
    private fun formatDuration(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return "%02d:%02d 秒".format(min, sec)
    }
}
