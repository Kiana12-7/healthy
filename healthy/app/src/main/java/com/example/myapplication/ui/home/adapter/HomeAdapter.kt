package com.example.myapplication.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.models.HomeItem
import com.example.myapplication.databinding.ItemVideoBinding

/**
 * 视频列表适配器
 * 确定了使用 HomeItem.Video 数据模型。
 */
class HomeAdapter(
    private val list: List<HomeItem.Video>,
    private val onVideoClick: (HomeItem.Video) -> Unit
) : RecyclerView.Adapter<VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        // 使用 ViewBinding 加载 item_video 布局
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val item = list[position]

        // 调用 ViewHolders.kt 中的 bind 方法进行数据填充和点击事件绑定
        holder.bind(item, onVideoClick)

        // 【加固逻辑】：确保整个条目都是可以点击的
        holder.itemView.setOnClickListener {
            onVideoClick(item)
        }
    }

    override fun getItemCount(): Int = list.size
}