package com.example.myapplication.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.FilterTag

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
        view.findViewById<Button>(R.id.btn_reset).setOnClickListener {
            tags.forEach { it.isSelected = false }
            adapter.notifyDataSetChanged()
        }

        // 确定按钮
        view.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            onConfirm(tags.filter { it.isSelected })
            dismiss()
        }
    }

    fun show(anchor: View) {
        showAsDropDown(anchor)
    }

    // 标签适配器
    inner class FilterTagAdapter(private val tagList: List<FilterTag>) :
        RecyclerView.Adapter<FilterTagAdapter.TagViewHolder>() {

        inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tagButton: Button = itemView.findViewById(R.id.btn_tag)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_filter_tag, parent, false)
            return TagViewHolder(view)
        }

        override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
            val tag = tagList[position]
            holder.tagButton.text = tag.name

            // 清晰的白绿对比
            if (tag.isSelected) {
                // 选中：纯绿背景 + 白色文字（非常醒目）
                holder.tagButton.setBackgroundResource(R.drawable.bg_tag_selected)
                holder.tagButton.setTextColor(Color.WHITE)
            } else {
                // 未选中：纯白背景 + 深灰文字（干净清爽）
                holder.tagButton.setBackgroundResource(R.drawable.bg_tag_normal)
                holder.tagButton.setTextColor(Color.parseColor("#333333"))
            }

            // 点击切换选中状态
            holder.tagButton.setOnClickListener {
                tag.isSelected = !tag.isSelected
                notifyItemChanged(position)
            }
        }

        override fun getItemCount(): Int = tagList.size
    }
}