package com.example.myapplication.ui.today

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.CalendarDay
import com.example.myapplication.databinding.ItemCalendarDayBinding

class CalendarAdapter(private val onDateClick: (CalendarDay, Int) -> Unit) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    private var days = mutableListOf<CalendarDay>()

    fun submitList(newList: List<CalendarDay>) {
        days.clear()
        days.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(days[position], position)
    }

    override fun getItemCount() = days.size

    inner class ViewHolder(private val binding: ItemCalendarDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CalendarDay, position: Int) {
            // 1. 设置系统文字
            binding.tvDate.text = item.displayMonthDay

            if (item.isToday) {
                binding.tvWeek.text = "今日"
                binding.tvWeek.setTextColor(Color.parseColor("#00C48C")) // 今日文字变绿
            } else {
                binding.tvWeek.text = item.displayWeekDay
                binding.tvWeek.setTextColor(Color.parseColor("#999999"))
            }

            // 2. 强烈的选中视觉效果
            if (item.isSelected) {
                binding.llDateRoot.setBackgroundResource(R.drawable.bg_calendar_selected) // 使用我们刚写的绿底
                binding.tvWeek.setTextColor(Color.WHITE)
                binding.tvDate.setTextColor(Color.WHITE)
            } else {
                binding.llDateRoot.setBackgroundResource(R.drawable.bg_calendar_unselected)
                // 恢复默认颜色
                binding.tvDate.setTextColor(Color.parseColor("#333333"))
                if (!item.isToday) binding.tvWeek.setTextColor(Color.parseColor("#999999"))
            }

            // 3. 点击事件，把 position 也传出去用于滑动对齐
            binding.root.setOnClickListener {
                onDateClick(item, position)
            }
        }
    }
}