package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.MyPlanItemRecyclerViewAdapter
import com.example.myapplication.manager.FilterManager
import com.example.myapplication.model.FilterTag
import com.example.myapplication.model.FilterType
import com.example.myapplication.model.PlanItem
import com.example.myapplication.widget.FilterPopupWindow

class PlanItemFragment : Fragment() {

    // 所有计划数据（PlanItem类型，和适配器完全匹配）
    private val fullPlanList = PlanItem.getAllPlans()
    // 所有筛选标签配置
    private val allFilterTags = PlanItem.getAllFilterTags()
    // 存储当前选中的标签
    private val selectedTagMap = mutableMapOf<FilterType, MutableList<FilterTag>>()

    private lateinit var planAdapter: MyPlanItemRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 加载修复后的布局
        val rootView = inflater.inflate(R.layout.fragment_item_list, container, false)

        // 初始化列表，传入PlanItem类型的列表，彻底解决类型报错
        val planRecyclerView = rootView.findViewById<RecyclerView>(R.id.rv_plan_list)
        planRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        planAdapter = MyPlanItemRecyclerViewAdapter(fullPlanList)
        planRecyclerView.adapter = planAdapter

        // 绑定筛选按钮点击事件
        initFilterButtons(rootView)

        return rootView
    }

    // 初始化筛选按钮
    private fun initFilterButtons(rootView: View) {
        // 目标筛选按钮
        rootView.findViewById<TextView>(R.id.btn_filter_goal).setOnClickListener {
            showFilterPopup(it, FilterType.GOAL)
        }
        // 部位筛选按钮
        rootView.findViewById<TextView>(R.id.btn_filter_part).setOnClickListener {
            showFilterPopup(it, FilterType.PART)
        }
        // 难度筛选按钮
        rootView.findViewById<TextView>(R.id.btn_filter_difficulty).setOnClickListener {
            showFilterPopup(it, FilterType.DIFFICULTY)
        }
        // 人群筛选按钮
        rootView.findViewById<TextView>(R.id.btn_filter_crowd).setOnClickListener {
            showFilterPopup(it, FilterType.CROWD)
        }
    }

    // 显示筛选弹窗
    private fun showFilterPopup(anchorView: View, filterType: FilterType) {
        val tagList = allFilterTags[filterType] ?: return

        // 恢复之前的选中状态
        val savedSelected = selectedTagMap[filterType] ?: emptyList()
        tagList.forEach { tag ->
            tag.isSelected = savedSelected.any { it.id == tag.id }
        }

        // 弹出筛选窗
        FilterPopupWindow(requireContext(), tagList) { selectedTags ->
            // 保存选中的标签
            selectedTagMap[filterType] = selectedTags.toMutableList()
            // 执行筛选并刷新列表
            val filteredList = FilterManager.filterPlans(fullPlanList, selectedTagMap)
            planAdapter.refreshData(filteredList)
            // 选中的按钮高亮显示
            anchorView.isSelected = selectedTags.isNotEmpty()
        }.show(anchorView)
    }
}