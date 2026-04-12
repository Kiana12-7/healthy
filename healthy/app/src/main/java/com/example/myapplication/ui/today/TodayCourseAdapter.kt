package com.example.myapplication.ui.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
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
                binding.tvCourseName.text = item.title
                val infoText = "${item.trainerName} · ${item.duration / 60}分钟"
                binding.tvCourseInfo.text = infoText
                binding.ivCourseCover.load(item.coverUrl) {
                    crossfade(true)
                }
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
