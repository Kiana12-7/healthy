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
            binding.tvDate.text = item.displayMonthDay

            if (item.isToday) {
                binding.tvWeek.text = "今日"
            } else {
                binding.tvWeek.text = item.displayWeekDay
            }

            if (item.isSelected) {
                binding.llDateRoot.setBackgroundResource(R.drawable.bg_calendar_selected)
                binding.tvWeek.setTextColor(Color.WHITE)
                binding.tvDate.setTextColor(Color.WHITE)
                binding.viewTodayDot.setBackgroundColor(Color.WHITE)
                binding.root.alpha = 1f
                binding.root.scaleX = 1.04f
                binding.root.scaleY = 1.04f
            } else if (item.isToday) {
                binding.llDateRoot.setBackgroundResource(R.drawable.bg_calendar_today)
                binding.tvWeek.setTextColor(Color.parseColor("#00B07B"))
                binding.tvDate.setTextColor(Color.parseColor("#154734"))
                binding.viewTodayDot.setBackgroundColor(Color.parseColor("#00C48C"))
                binding.root.alpha = 1f
                binding.root.scaleX = 1f
                binding.root.scaleY = 1f
            } else {
                binding.llDateRoot.setBackgroundResource(R.drawable.bg_calendar_unselected)
                binding.tvWeek.setTextColor(Color.parseColor("#8D95A3"))
                binding.tvDate.setTextColor(Color.parseColor("#2B3340"))
                binding.viewTodayDot.setBackgroundColor(Color.TRANSPARENT)
                binding.root.alpha = 0.92f
                binding.root.scaleX = 1f
                binding.root.scaleY = 1f
            }

            binding.root.setOnClickListener {
                onDateClick(item, position)
            }
        }
    }
}
