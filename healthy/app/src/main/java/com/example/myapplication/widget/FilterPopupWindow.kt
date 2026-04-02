package com.example.myapplication.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.PopupWindow
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.home.plan.planItem.FilterTag

class FilterPopupWindow(
    context: Context,
    private val tags: List<FilterTag>,
    private val onConfirm: (List<FilterTag>) -> Unit
) : PopupWindow(context) {

    private val adapter = FilterTagAdapter(tags)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.popup_filter, null)
        contentView = view
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.WHITE))

        // 标签列表
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_tags)
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        recyclerView.adapter = adapter

        // 重置按钮
        view.findViewById<TextView>(R.id.tv_reset).setOnClickListener {
            tags.forEach { it.isSelected = false }
            adapter.notifyDataSetChanged()
        }

        // 确定按钮
        view.findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            onConfirm(tags.filter { it.isSelected })
            dismiss()
        }
    }

    fun show(anchor: View) {
        showAsDropDown(anchor)
    }

    // 标签适配器，适配TextView
    inner class FilterTagAdapter(private val tagList: List<FilterTag>) :
        RecyclerView.Adapter<FilterTagAdapter.TagViewHolder>() {

        inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tagText: TextView = itemView.findViewById(R.id.tv_tag)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_filter_tag, parent, false)
            return TagViewHolder(view)
        }

        override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
            val tag = tagList[position]
            holder.tagText.text = tag.name

            if (tag.isSelected) {
                // 选中：绿背景 + 白字
                holder.tagText.setBackgroundResource(R.drawable.bg_tag_selected)
                holder.tagText.setTextColor(Color.WHITE)
            } else {
                // 未选中：白背景 + 深灰字
                holder.tagText.setBackgroundResource(R.drawable.bg_tag_normal)
                holder.tagText.setTextColor(Color.parseColor("#333333"))
            }

            // 点击切换选中状态
            holder.tagText.setOnClickListener {
                tag.isSelected = !tag.isSelected
                notifyItemChanged(position)
            }
        }

        override fun getItemCount(): Int = tagList.size
    }
}