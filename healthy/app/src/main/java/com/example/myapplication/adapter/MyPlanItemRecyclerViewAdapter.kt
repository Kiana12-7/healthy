package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// 把构造函数参数改成 List<String>
class MyPlanItemRecyclerViewAdapter(
    private val values: List<String>
) : RecyclerView.Adapter<MyPlanItemRecyclerViewAdapter.ViewHolder>() {

    // ViewHolder 直接用系统的简单布局，或者你自己的 item 布局
    inner class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 这里用了一个最简单的系统自带的 TextView 作为 Item，你也可以换成你自己的布局
        val textView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 直接显示计划名称
        holder.textView.text = values[position]
    }

    override fun getItemCount(): Int = values.size
}