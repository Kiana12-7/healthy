package com.example.myapplication.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.home.plan.planDetail.PlanDetailActivity
import com.example.myapplication.ui.home.plan.planItem.MyPlanItemRecyclerViewAdapter
import com.example.myapplication.ui.home.plan.planItem.PlanItem

class SearchActivity : AppCompatActivity() {

    private var ivBack: ImageView? = null
    private var etSearchInput: EditText? = null
    private var tvSearchBtn: TextView? = null
    private var llRecommendSection: LinearLayout? = null
    private var tvListTitle: TextView? = null
    private var rvSearchResults: RecyclerView? = null
    private var tvEmptyState: TextView? = null

    private lateinit var adapter: MyPlanItemRecyclerViewAdapter

    // 数据源
    private val allPlanList: List<PlanItem> by lazy { PlanItem.getAllPlans() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()
        setupRecyclerView()
        initListeners()

        // 进入页面显示随机推荐
        showRandomRecommend()
    }

    private fun initView() {
        ivBack = findViewById(R.id.iv_back)
        etSearchInput = findViewById(R.id.et_search_input)
        tvSearchBtn = findViewById(R.id.tv_search_btn)
        llRecommendSection = findViewById(R.id.ll_recommend_section)
        tvListTitle = findViewById(R.id.tv_list_title)
        rvSearchResults = findViewById(R.id.rv_search_results)
        tvEmptyState = findViewById(R.id.tv_empty_state)
    }

    private fun setupRecyclerView() {
        rvSearchResults?.layoutManager = LinearLayoutManager(this)

        // 1. 初始化时传入 true，确保使用 item_search_plan 布局
        adapter = MyPlanItemRecyclerViewAdapter(mutableListOf(), true)
        rvSearchResults?.adapter = adapter

        // 2. 核心修正：设置点击监听，处理从搜索页到详情页的跳转
        adapter.setOnItemClickListener { planItem: PlanItem ->
            PlanDetailActivity.actionStart(this, planItem.id, planItem.name)
        }
    }

    private fun initListeners() {
        ivBack?.setOnClickListener { finish() }

        tvSearchBtn?.setOnClickListener {
            performSearch(etSearchInput?.text.toString().trim())
        }

        etSearchInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().trim()
                if (keyword.isEmpty()) {
                    showRandomRecommend()
                } else {
                    performSearch(keyword)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etSearchInput?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearchInput?.text.toString().trim())
                true
            } else { false }
        }
    }

    private fun performSearch(keyword: String) {
        if (keyword.isEmpty()) {
            showRandomRecommend()
            return
        }

        llRecommendSection?.visibility = View.GONE
        tvListTitle?.text = "搜索结果"

        val filteredResults = allPlanList.filter { plan ->
            plan.name.contains(keyword, ignoreCase = true) ||
                    plan.tagIds.any { tagId -> tagId.contains(keyword, ignoreCase = true) }
        }

        updateListUI(filteredResults)
    }

    private fun showRandomRecommend() {
        llRecommendSection?.visibility = View.VISIBLE
        tvListTitle?.text = "随机推荐"
        tvEmptyState?.visibility = View.GONE
        rvSearchResults?.visibility = View.VISIBLE

        // 随机取 6 个数据
        val randomSix = allPlanList.shuffled().take(6)
        adapter.updateData(randomSix)
    }

    private fun updateListUI(results: List<PlanItem>) {
        if (results.isEmpty()) {
            rvSearchResults?.visibility = View.GONE
            tvEmptyState?.visibility = View.VISIBLE
        } else {
            rvSearchResults?.visibility = View.VISIBLE
            tvEmptyState?.visibility = View.GONE
            adapter.updateData(results)
        }
    }
}