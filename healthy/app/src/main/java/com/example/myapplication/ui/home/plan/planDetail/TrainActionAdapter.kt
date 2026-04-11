package com.example.myapplication.ui.home.plan.planDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemTrainActionBinding

class TrainActionAdapter : ListAdapter<TrainActionItem, TrainActionAdapter.ActionViewHolder>(ActionDiffCallback()) {

    // 点击看动作的回调
    private var onWatchActionClick: ((TrainActionItem) -> Unit)? = null

    // 暴露设置点击事件的方法
    fun setOnWatchActionClickListener(listener: (TrainActionItem) -> Unit) {
        onWatchActionClick = listener
    }

    // 对外提供设置数据的方法
    fun setData(data: List<TrainActionItem>) {
        submitList(data)
    }

    inner class ActionViewHolder(private val binding: ItemTrainActionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TrainActionItem) {
            // 绑定动作名称
            binding.tvActionName.text = item.actionName
            // 绑定组数次数
            binding.tvGroupDesc.text = item.groupDesc
            // 绑定休息时间
            binding.tvRestDesc.text = item.restDesc
            // 点击看动作按钮
            binding.tvWatchAction.setOnClickListener {
                onWatchActionClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val binding = ItemTrainActionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ActionDiffCallback : DiffUtil.ItemCallback<TrainActionItem>() {
        override fun areItemsTheSame(oldItem: TrainActionItem, newItem: TrainActionItem): Boolean {
            return oldItem.actionId == newItem.actionId
        }

        override fun areContentsTheSame(oldItem: TrainActionItem, newItem: TrainActionItem): Boolean {
            return oldItem == newItem
        }
    }
}