package com.example.myapplication.ui.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.CourseItem
import com.example.myapplication.databinding.ItemTodayCourseBinding

/**
 * 适配器：已同步最新的 CourseItem 数据模型
 */
class TodayCourseAdapter : ListAdapter<CourseItem, TodayCourseAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTodayCourseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // getItem(position) 可能返回 null (如果在 PagedList 中)，这里做安全性处理
        val item = getItem(position) ?: return

        // 匹配最新的 CourseItem.TrainingVideo 类型
        if (item is CourseItem.TrainingVideo) {
            holder.bind(item)
        }
    }

    class ViewHolder(val binding: ItemTodayCourseBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CourseItem.TrainingVideo) {
            binding.tvCourseName.text = item.title

            // 保持 trainerName 和 duration 的对应关系
            // 请确保 strings.xml 中的 course_info 是 "%1$s · %2$d分钟" 之类的格式
            binding.tvCourseInfo.text = itemView.context.getString(
                R.string.course_info,
                item.trainerName,
                item.duration
            )

            // 如果你有封面图，可以在这里添加加载逻辑，例如使用 Coil:
            // binding.ivCourseCover.load(item.coverUrl)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CourseItem>() {
        override fun areItemsTheSame(oldItem: CourseItem, newItem: CourseItem): Boolean {
            // 使用抽象类中定义的 id 进行比对
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CourseItem, newItem: CourseItem): Boolean {
            // data class 的 == 比较会检查所有属性是否一致
            return oldItem == newItem
        }
    }
}