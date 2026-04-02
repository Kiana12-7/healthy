package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.PlanItem

class PlanAdapter(
    // 这里定义 planList，接收外部传入的数据
    private val planList: List<PlanItem>
) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    // 点击事件接口（预留可交互 + 后端接口）
    private var onItemClickListener: ((PlanItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (PlanItem) -> Unit) {
        onItemClickListener = listener
    }

    // 创建列表单项（加载 item_plan.xml）
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plan, parent, false)
        return PlanViewHolder(view)
    }

    // 绑定数据：图片 + 标题
    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = planList[position]

        // 设置计划名称
        holder.tvName.text = plan.name

        // 设置图片（你之前定义的图片资源）
        holder.ivImage.setImageResource(plan.imageResId)

        // 点击事件（可交互 + 预留后端接口）
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(plan)
        }
    }

    override fun getItemCount(): Int {
        return planList.size
    }

    // ViewHolder 定义
    inner class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.iv_plan_image)
        val tvName: TextView = itemView.findViewById(R.id.tv_plan_name)
    }


}