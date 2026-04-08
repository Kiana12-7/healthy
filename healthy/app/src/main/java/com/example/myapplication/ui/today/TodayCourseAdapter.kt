package com.example.myapplication.ui.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.CourseItem
import com.example.myapplication.databinding.ItemTodayCourseBinding

class TodayCourseAdapter : ListAdapter<CourseItem, TodayCourseAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTodayCourseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item is CourseItem.TrainingVideo) {
            holder.bind(item)
        }
    }

    class ViewHolder(val binding: ItemTodayCourseBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CourseItem.TrainingVideo) {
            binding.tvCourseName.text = item.title

            binding.tvCourseInfo.text = itemView.context.getString(
                R.string.course_info,
                item.trainerName,
                item.duration
            )
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CourseItem>() {
        override fun areItemsTheSame(oldItem: CourseItem, newItem: CourseItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CourseItem, newItem: CourseItem): Boolean {
            return oldItem == newItem
        }
    }
}