package com.example.myapplication.ui.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.CalendarDay
import com.example.myapplication.databinding.ItemCalendarDayBinding

class CalendarAdapter(
    private val onDateSelected: (CalendarDay) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    private var days = listOf<CalendarDay>()

    fun submitList(newDays: List<CalendarDay>) {
        days = newDays
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarDayBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = days[position]
        holder.bind(item)
    }

    override fun getItemCount() = days.size

    inner class ViewHolder(private val binding: ItemCalendarDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CalendarDay) {
            binding.tvDayOfWeek.text = item.dayOfWeek
            binding.tvDayOfMonth.text = item.dayOfMonth

            // 选中状态的视觉反馈
            binding.llCalendarItem.isSelected = item.isSelected

            binding.root.setOnClickListener {
                // 更新选中逻辑
                days.forEach { it.isSelected = false }
                item.isSelected = true
                notifyDataSetChanged()

                // 触发回调给 ViewModel 刷新数据
                onDateSelected(item)
            }
        }
    }
}