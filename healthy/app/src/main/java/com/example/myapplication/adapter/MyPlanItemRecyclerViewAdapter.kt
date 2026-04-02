package com.example.myapplication.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.databinding.FragmentPlanItemBinding
import com.example.myapplication.model.PlanItem

class MyPlanItemRecyclerViewAdapter(
    // 使用 var 允许数据更新，类型统一为正式的 PlanItem
    private var values: List<PlanItem>
) : RecyclerView.Adapter<MyPlanItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentPlanItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
    }

    override fun getItemCount(): Int = values.size

    // 【关键】供筛选功能调用，刷新列表
    fun updateData(newList: List<PlanItem>) {
        this.values = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: FragmentPlanItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
    }
}