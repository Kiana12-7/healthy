package com.example.myapplication.ui.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.CourseItem
import com.example.myapplication.databinding.ItemTodayCourseBinding

/**
 * 课程适配器
 * 已修正 ID 以匹配你的 XML 布局
 */
class TodayCourseAdapter(
    private val onItemClick: (CourseItem) -> Unit
) : ListAdapter<CourseItem, TodayCourseAdapter.CourseViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemTodayCourseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class CourseViewHolder(private val binding: ItemTodayCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CourseItem) {
            if (item is CourseItem.TrainingVideo) {
                // 1. 对应你 XML 里的 @+id/tv_course_name
                binding.tvCourseName.text = item.title

                // 2. 对应你 XML 里的 @+id/tv_course_info
                // 这里我们把教练名和时长拼在一起显示
                val infoText = "${item.trainerName} · ${item.duration / 60}分钟"
                binding.tvCourseInfo.text = infoText

                // 3. 对应你 XML 里的 @+id/iv_course_cover
                // 如果你有图片加载库（如 Glide），可以在这里加载封面
                // Glide.with(binding.ivCourseCover.context).load(item.coverUrl).into(binding.ivCourseCover)

                // 设置点击事件
                binding.root.setOnClickListener { onItemClick(item) }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CourseItem>() {
        override fun areItemsTheSame(oldItem: CourseItem, newItem: CourseItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CourseItem, newItem: CourseItem): Boolean =
            oldItem == newItem
    }
}