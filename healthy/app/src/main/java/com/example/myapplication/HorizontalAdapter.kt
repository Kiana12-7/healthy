package com.example.myapplication

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HorizontalAdapter(
    private val dataList: List<String>,
    private var selectedPosition: Int = 0
) : RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {

    var onTabClick: ((Int) -> Unit)? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horizontal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataList[position]

        // 加粗逻辑
        holder.textView.typeface = if (position == selectedPosition) {
            Typeface.DEFAULT_BOLD
        } else {
            Typeface.DEFAULT
        }

        holder.itemView.setOnClickListener {
            val oldPos = selectedPosition
            selectedPosition = position
            notifyItemChanged(oldPos)
            notifyItemChanged(selectedPosition)
            onTabClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun setSelectPosition(position: Int) {
        this.selectedPosition = position
        notifyDataSetChanged()
    }
}