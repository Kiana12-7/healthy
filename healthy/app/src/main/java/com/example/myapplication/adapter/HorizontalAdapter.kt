package com.example.myapplication.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R  //
class HorizontalAdapter(
    private val dataList: List<String>,
    private var selectedPosition: Int = 0
) : RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {

    // 标签点击回调
    var onTabClick: ((Int) -> Unit)? = null

    // ViewHolder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horizontal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 绑定标签文字
        holder.textView.text = dataList[position]

        // 选中状态：加粗/普通样式
        holder.textView.typeface = if (position == selectedPosition) {
            Typeface.DEFAULT_BOLD
        } else {
            Typeface.DEFAULT
        }

        // 点击事件：优化重复点击，提升性能
        holder.itemView.setOnClickListener {
            val oldPos = selectedPosition
            if (oldPos != position) {  // 避免重复点击同一位置时无效刷新
                selectedPosition = position
                notifyItemChanged(oldPos)
                notifyItemChanged(position)
                onTabClick?.invoke(position)
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    // 保留方法：在Fragment中调用后，"never used"警告自动消失
    fun setSelectPosition(position: Int) {
        val oldPos = selectedPosition
        this.selectedPosition = position
        // 若要彻底消除性能警告，可替换为细粒度刷新（需确保oldPos正确）
        notifyItemChanged(oldPos)
        notifyItemChanged(position)
    }
}