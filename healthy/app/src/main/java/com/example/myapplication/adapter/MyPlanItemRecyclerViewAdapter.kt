package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.PlanItem

class MyPlanItemRecyclerViewAdapter(
    // 明确指定接收PlanItem列表，彻底解决类型不匹配
    private var planList: List<PlanItem>
) : RecyclerView.Adapter<MyPlanItemRecyclerViewAdapter.PlanViewHolder>() {

    // 内部ViewHolder
    inner class PlanViewHolder(val itemText: TextView) : RecyclerView.ViewHolder(itemText)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        // 用系统自带的简单布局，避免自定义布局报错
        val textView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return PlanViewHolder(textView)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = planList[position]
        holder.itemText.text = plan.name
        // 统一设置item样式，避免排版错乱
        holder.itemText.setPadding(40, 24, 40, 24)
        holder.itemText.textSize = 16f
    }

    override fun getItemCount(): Int = planList.size

    // 筛选后更新列表的方法
    fun refreshData(newList: List<PlanItem>) {
        this.planList = newList
        notifyDataSetChanged()
    }
}