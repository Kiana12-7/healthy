package com.example.myapplication.ui.home.plan.planItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class MyPlanItemRecyclerViewAdapter(
    private var values: List<PlanItem>
) : RecyclerView.Adapter<MyPlanItemRecyclerViewAdapter.ViewHolder>() {

    private var onItemClickListener: ((PlanItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (PlanItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.tvPlanName.text = item.name
        holder.ivPlanImage.setImageResource(item.imageResId)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
    }

    override fun getItemCount(): Int = values.size

    fun updateData(newList: List<PlanItem>) {
        this.values = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPlanImage: ImageView = itemView.findViewById(R.id.iv_plan_image)
        val tvPlanName: TextView = itemView.findViewById(R.id.tv_plan_name)
    }
}