package com.example.myapplication.ui.home.plan.planItem

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.ui.fitness.WeightActivity
import com.example.myapplication.ui.home.plan.planDetail.PlanDetailActivity
import com.example.myapplication.widget.FilterPopupWindow
import kotlinx.coroutines.launch

class PlanItemFragment : Fragment() {

    private var columnCount = 1
    // 类成员变量，供筛选使用
    private var fullPlanList: List<PlanItem> = emptyList()
    private val allFilterTags = PlanItem.getAllFilterTags()
    private val selectedTagMap = mutableMapOf<FilterType, MutableList<FilterTag>>()

    // 类成员变量，供筛选更新数据
    private lateinit var planAdapter: MyPlanItemRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_item_list, container, false)

        // ========== 定制计划Banner点击：跳转到 WeightActivity ==========
        val bannerPlan = rootView.findViewById<ImageView>(R.id.iv_banner_plan)
        bannerPlan.setOnClickListener {
            val intent = Intent(requireContext(), WeightActivity::class.java)
            startActivity(intent)
        }

        // 计划列表初始化
        val rvPlanList = rootView.findViewById<RecyclerView>(R.id.rv_plan_list)
        rvPlanList.layoutManager = LinearLayoutManager(requireContext())

        // 直接赋值给类成员变量，不重复定义
        planAdapter = MyPlanItemRecyclerViewAdapter(emptyList())
        rvPlanList.adapter = planAdapter

        // ========== 计划点击跳转到详情页 ==========
        // 明确指定参数类型为 (planItem: PlanItem)
        planAdapter.setOnItemClickListener { planItem: PlanItem ->
            PlanDetailActivity.actionStart(requireContext(), planItem.id, planItem.name)
        }

        initFilterButtons(rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadWorkoutPlans()
    }

    private fun loadWorkoutPlans() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.workoutPlanService.getWorkoutPlanList()
                fullPlanList = response.map(PlanItem::fromWorkoutPlanDto)
                applyFilters()
            } catch (e: Exception) {
                e.printStackTrace()
                fullPlanList = emptyList()
                planAdapter.updateData(emptyList())
                Toast.makeText(requireContext(), "计划列表加载失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initFilterButtons(rootView: View) {
        rootView.findViewById<TextView>(R.id.btn_filter_goal)?.setOnClickListener { showFilterPopup(it, FilterType.GOAL) }
        rootView.findViewById<TextView>(R.id.btn_filter_part)?.setOnClickListener { showFilterPopup(it, FilterType.PART) }
        rootView.findViewById<TextView>(R.id.btn_filter_difficulty)?.setOnClickListener { showFilterPopup(it, FilterType.DIFFICULTY) }
        rootView.findViewById<TextView>(R.id.btn_filter_crowd)?.setOnClickListener { showFilterPopup(it, FilterType.CROWD) }
    }

    private fun showFilterPopup(anchorView: View, filterType: FilterType) {
        val tagList = allFilterTags[filterType] ?: return
        val savedSelected = selectedTagMap[filterType] ?: emptyList()

        // 明确 for 循环，消除重载歧义
        for (tag in tagList) {
            tag.isSelected = savedSelected.any { it.id == tag.id }
        }

        FilterPopupWindow(requireContext(), tagList) { selectedTags ->
            selectedTagMap[filterType] = selectedTags.toMutableList()
            applyFilters()
            anchorView.isSelected = selectedTags.isNotEmpty()
        }.show(anchorView)
    }

    private fun applyFilters() {
        val filteredList = FilterManager.filterPlans(fullPlanList, selectedTagMap)
        planAdapter.updateData(filteredList)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) = PlanItemFragment().apply {
            arguments = Bundle().apply { putInt(ARG_COLUMN_COUNT, columnCount) }
        }
    }
}
