package com.example.myapplication.ui.home.plan.planItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class MyPlanItemRecyclerViewAdapter(
    private var values: List<PlanItem>,
    private val isSearch: Boolean = false
) : RecyclerView.Adapter<MyPlanItemRecyclerViewAdapter.ViewHolder>() {

    // 1. 核心：定义点击事件回调
    private var onItemClickListener: ((PlanItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (PlanItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return if (isSearch) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 根据 viewType 加载对应的布局，首页是 item_plan，搜索页是 item_search_plan
        val layoutRes = if (viewType == 1) R.layout.item_search_plan else R.layout.item_plan
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        // 绑定公共属性（假设两套布局都有 tv_plan_name）
        holder.tvPlanName?.text = item.name

        holder.tvPlanTags?.apply {
            val tags = item.getDisplayTags()
            text = tags
            isVisible = tags.isNotBlank()
        }

        // 绑定图片
        if (item.imageResId != 0) {
            holder.ivPlanImage?.setImageResource(item.imageResId)
        }

        // 2. 核心：设置点击监听
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
    }

    override fun getItemCount(): Int = values.size

    fun updateData(newList: List<PlanItem>) {
        this.values = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        var ivPlanImage: ImageView? = null
        var tvPlanName: TextView? = null
        var tvPlanTags: TextView? = null

        init {
            if (viewType == 1) {
                // 搜索页布局 ID
                ivPlanImage = itemView.findViewById(R.id.iv_plan_icon)
                tvPlanName = itemView.findViewById(R.id.tv_plan_name)
                tvPlanTags = itemView.findViewById(R.id.tv_plan_tags)
            } else {
                // 首页布局 ID (item_plan)
                ivPlanImage = itemView.findViewById(R.id.iv_plan_image)
                tvPlanName = itemView.findViewById(R.id.tv_plan_name)
            }
        }
    }
}
