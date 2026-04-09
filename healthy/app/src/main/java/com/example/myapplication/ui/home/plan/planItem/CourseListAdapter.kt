package com.example.myapplication.ui.home.plan.planItem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemCourseListBinding

class CourseListAdapter : ListAdapter<CourseItem, CourseListAdapter.CourseViewHolder>(CourseDiffCallback()) {

    // 课程点击事件回调
    private var onItemClickListener: ((CourseItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (CourseItem) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<CourseItem>) {
        submitList(data)
    }

    inner class CourseViewHolder(private val binding: ItemCourseListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(courseItem: CourseItem) {
            // 绑定课程名称
            binding.tvCourseName.text = courseItem.courseName
            // 绑定时长+难度
            binding.tvCourseDuration.text = "${courseItem.duration}分钟 · ${courseItem.difficulty}"
            // 绑定学习状态
            binding.tvLearnStatus.text = if (courseItem.isLearned) "已完成训练" else "还未进行过训练"
            // 点击事件
            itemView.setOnClickListener { onItemClickListener?.invoke(courseItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // 差分优化，列表刷新更流畅
    class CourseDiffCallback : DiffUtil.ItemCallback<CourseItem>() {
        override fun areItemsTheSame(oldItem: CourseItem, newItem: CourseItem): Boolean {
            return oldItem.courseId == newItem.courseId
        }

        override fun areContentsTheSame(oldItem: CourseItem, newItem: CourseItem): Boolean {
            return oldItem == newItem
        }
    }
}