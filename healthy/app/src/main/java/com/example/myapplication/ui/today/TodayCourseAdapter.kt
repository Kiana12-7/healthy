package com.example.myapplication.ui.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.HomeItem
import com.example.myapplication.databinding.ItemTodayCourseBinding

class TodayCourseAdapter : RecyclerView.Adapter<TodayCourseAdapter.ViewHolder>() {

    private var list = listOf<HomeItem>()

    fun submitList(newList: List<HomeItem>) {
        this.list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTodayCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        // 关键点：判断类型并取值
        if (item is HomeItem.Video) {
            holder.binding.tvCourseName.text = item.title
            holder.binding.tvCourseInfo.text = "${item.author} · ${item.duration}"
            // 如果你有图片加载库（如 Coil 或 Glide）：
            // holder.binding.ivCourseCover.load(item.coverUrl)
        }
    }

    override fun getItemCount() = list.size

    class ViewHolder(val binding: ItemTodayCourseBinding) : RecyclerView.ViewHolder(binding.root)
}