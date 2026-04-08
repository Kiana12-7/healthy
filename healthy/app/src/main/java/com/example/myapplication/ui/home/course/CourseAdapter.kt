package com.example.myapplication.ui.home.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.CourseItem
import com.example.myapplication.databinding.ItemVideoBinding

/**
 * 课程视频列表适配器
 * 作用：为 RecyclerView 提供数据绑定、条目创建和点击事件处理
 */
class CourseAdapter(
    // 视频数据列表
    private val list: List<CourseItem.TrainingVideo>,
    // 条目点击事件回调
    private val onVideoClick: (CourseItem.TrainingVideo) -> Unit
) : RecyclerView.Adapter<VideoViewHolder>() {

    /**
     * 创建 ViewHolder 实例
     * 负责加载条目布局，返回可复用的条目容器
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        // 使用 ViewBinding 绑定 item_video 布局文件
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        // 返回创建好的 ViewHolder
        return VideoViewHolder(binding)
    }

    /**
     * 绑定数据到条目视图
     */
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, onVideoClick)
    }

    /**
     * 获取列表总条目数量
     * 返回视频数据集合的大小
     */
    override fun getItemCount(): Int = list.size
}