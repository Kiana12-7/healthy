package com.example.myapplication.ui.today

import android.view.LayoutInflater
import android.view.View // 必须手动导入，否则 ViewHolder 无法识别
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.HomeItem
import com.example.myapplication.databinding.ItemTodayCourseBinding

class TodayCourseAdapter : RecyclerView.Adapter<TodayCourseAdapter.ViewHolder>() {

    // 显式指定类型以防推断失败
    private var list: List<HomeItem> = emptyList()

    fun submitList(newList: List<HomeItem>) {
        this.list = newList
        // 解决 "Unresolved reference 'notifyDataSetChanged'"
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTodayCourseBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        if (item is HomeItem.Video) {
            holder.binding.tvCourseName.text = item.title

            // 解决 "Do not concatenate text" 警告
            // 建议：在 strings.xml 定义 <string name="course_info_format">%1$s · %2$s</string>
            // 然后使用：holder.itemView.context.getString(R.string.course_info_format, item.author, item.duration)
            // 临时修复（使用字符串模板）：
            val info = "${item.author} · ${item.duration}"
            holder.binding.tvCourseInfo.text = info
        }
    }

    override fun getItemCount(): Int = list.size

    // 确保这里的 ViewHolder 继承自 RecyclerView.ViewHolder
    // 并且显式传入 binding.root
    class ViewHolder(val binding: ItemTodayCourseBinding) : RecyclerView.ViewHolder(binding.root)
}