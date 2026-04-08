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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.fitness.WeightActivity
import com.example.myapplication.widget.FilterPopupWindow

class PlanItemFragment : Fragment() {

    private var columnCount = 1
    private val fullPlanList = PlanItem.getAllPlans()
    private val allFilterTags = PlanItem.getAllFilterTags()
    private val selectedTagMap = mutableMapOf<FilterType, MutableList<FilterTag>>()

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
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_item_list, container, false)


        val bannerPlan = rootView.findViewById<ImageView>(R.id.iv_banner_plan)
        bannerPlan.setOnClickListener {
            bannerPlan.setOnClickListener {
                val intent = Intent(requireContext(), WeightActivity::class.java)
                startActivity(intent)
            }
        }


        val rvPlanList = rootView.findViewById<RecyclerView>(R.id.rv_plan_list)
        rvPlanList.layoutManager = LinearLayoutManager(requireContext())
        val fullPlanList = PlanItem.getAllPlans()
        planAdapter = MyPlanItemRecyclerViewAdapter(fullPlanList)
        rvPlanList.adapter = planAdapter

        planAdapter.setOnItemClickListener { plan ->
            Toast.makeText(requireContext(), "点击了：${plan.name}", Toast.LENGTH_SHORT).show()
        }

        initFilterButtons(rootView)
        return rootView
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

        tagList.forEach { tag ->
            tag.isSelected = savedSelected.any { it.id == tag.id }
        }

        FilterPopupWindow(requireContext(), tagList) { selectedTags ->
            selectedTagMap[filterType] = selectedTags.toMutableList()
            val filteredList = FilterManager.filterPlans(fullPlanList, selectedTagMap)
            planAdapter.updateData(filteredList)
            anchorView.isSelected = selectedTags.isNotEmpty()
        }.show(anchorView)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        @JvmStatic
        fun newInstance(columnCount: Int) = PlanItemFragment().apply {
            arguments = Bundle().apply { putInt(ARG_COLUMN_COUNT, columnCount) }
        }
    }
}