package com.example.myapplication.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.databinding.FragmentPlanItemBinding

// 1. 我们自己随便建一个简单的类，替代被删掉的 PlaceholderItem
data class SimplePlanItem(val id: String, val content: String)

class MyPlanItemRecyclerViewAdapter(
    private val values: List<SimplePlanItem>
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
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentPlanItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
    }
}