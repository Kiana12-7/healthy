package com.example.myapplication.ui.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.ui.home.plan.planDetail.PlanDetailActivity
import com.example.myapplication.ui.home.plan.planItem.MyPlanItemRecyclerViewAdapter
import com.example.myapplication.ui.home.plan.planItem.PlanItem
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var etSearchInput: EditText
    private lateinit var tvSearchBtn: TextView
    private lateinit var recommendSection: View
    private lateinit var chipRecommendations: ChipGroup
    private lateinit var tvListTitle: TextView
    private lateinit var tvListHint: TextView
    private lateinit var rvSearchResults: RecyclerView
    private lateinit var progressSearch: CircularProgressIndicator
    private lateinit var emptyState: View
    private lateinit var tvEmptyTitle: TextView
    private lateinit var tvEmptyDesc: TextView

    private lateinit var adapter: MyPlanItemRecyclerViewAdapter

    private var allPlanList: List<PlanItem> = emptyList()
    private var recommendedPlanList: List<PlanItem> = emptyList()
    private var pendingSearchJob: Job? = null
    private var isBootstrapping = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()
        setupRecyclerView()
        initListeners()
        loadPlanRecommendations()
    }

    private fun initView() {
        ivBack = findViewById(R.id.iv_back)
        etSearchInput = findViewById(R.id.et_search_input)
        tvSearchBtn = findViewById(R.id.tv_search_btn)
        recommendSection = findViewById(R.id.ll_recommend_section)
        chipRecommendations = findViewById(R.id.chip_recommendations)
        tvListTitle = findViewById(R.id.tv_list_title)
        tvListHint = findViewById(R.id.tv_list_hint)
        rvSearchResults = findViewById(R.id.rv_search_results)
        progressSearch = findViewById(R.id.progress_search)
        emptyState = findViewById(R.id.layout_empty_state)
        tvEmptyTitle = findViewById(R.id.tv_empty_title)
        tvEmptyDesc = findViewById(R.id.tv_empty_desc)
    }

    private fun setupRecyclerView() {
        rvSearchResults.layoutManager = LinearLayoutManager(this)
        adapter = MyPlanItemRecyclerViewAdapter(emptyList(), true)
        rvSearchResults.adapter = adapter
        adapter.setOnItemClickListener { planItem ->
            PlanDetailActivity.actionStart(this, planItem.id, planItem.name)
        }
    }

    private fun initListeners() {
        ivBack.setOnClickListener { finish() }

        tvSearchBtn.setOnClickListener {
            triggerImmediateSearch()
        }

        etSearchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().trim()
                pendingSearchJob?.cancel()

                if (keyword.isEmpty()) {
                    showRecommendationState()
                    return
                }

                pendingSearchJob = lifecycleScope.launch {
                    delay(280)
                    searchPlans(keyword)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        etSearchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                triggerImmediateSearch()
                true
            } else {
                false
            }
        }
    }

    private fun triggerImmediateSearch() {
        val keyword = etSearchInput.text.toString().trim()
        pendingSearchJob?.cancel()

        if (keyword.isEmpty()) {
            showRecommendationState()
            return
        }

        pendingSearchJob = lifecycleScope.launch {
            searchPlans(keyword)
        }
    }

    private fun loadPlanRecommendations() {
        isBootstrapping = true
        showLoadingState(
            title = getString(R.string.search_curated_title),
            hint = getString(R.string.search_loading_hint),
            showRecommendations = false
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.workoutPlanService.getWorkoutPlanList()
                allPlanList = response.map(PlanItem::fromWorkoutPlanDto)
                recommendedPlanList = allPlanList.shuffled().take(6)
                renderRecommendationChips(recommendedPlanList.take(5))
            } catch (e: Exception) {
                e.printStackTrace()
                allPlanList = emptyList()
                recommendedPlanList = emptyList()
                renderRecommendationChips(emptyList())
            } finally {
                isBootstrapping = false
            }

            if (etSearchInput.text.toString().trim().isNotEmpty()) {
                return@launch
            }

            if (recommendedPlanList.isNotEmpty()) {
                showRecommendationState()
            } else {
                showErrorState(
                    title = getString(R.string.search_error_title),
                    desc = getString(R.string.search_error_desc)
                )
            }
        }
    }

    private suspend fun searchPlans(keyword: String) {
        showLoadingState(
            title = getString(R.string.search_results_title),
            hint = getString(R.string.search_keyword_hint, keyword),
            showRecommendations = false
        )

        try {
            val response = RetrofitClient.workoutPlanService.getWorkoutPlanList(keyword)
            val results = response.map(PlanItem::fromWorkoutPlanDto)
            renderSearchResultState(keyword, results)
        } catch (e: Exception) {
            e.printStackTrace()

            val cachedResults = allPlanList.filter { it.matchesKeyword(keyword) }
            if (cachedResults.isNotEmpty()) {
                Toast.makeText(this, R.string.search_cache_fallback, Toast.LENGTH_SHORT).show()
                renderSearchResultState(keyword, cachedResults)
                return
            }

            showErrorState(
                title = getString(R.string.search_error_title),
                desc = getString(R.string.search_error_desc)
            )
        }
    }

    private fun showRecommendationState() {
        if (isBootstrapping) {
            showLoadingState(
                title = getString(R.string.search_curated_title),
                hint = getString(R.string.search_loading_hint),
                showRecommendations = false
            )
            return
        }

        recommendSection.isVisible = recommendedPlanList.isNotEmpty()
        tvListTitle.text = getString(R.string.search_curated_title)
        tvListHint.text = getString(R.string.search_curated_subtitle)

        if (recommendedPlanList.isEmpty()) {
            showEmptyState(
                title = getString(R.string.search_empty_title),
                desc = getString(R.string.search_empty_desc)
            )
            return
        }

        progressSearch.isVisible = false
        emptyState.isVisible = false
        rvSearchResults.isVisible = true
        adapter.updateData(recommendedPlanList)
    }

    private fun renderSearchResultState(keyword: String, results: List<PlanItem>) {
        recommendSection.isVisible = false
        tvListTitle.text = getString(R.string.search_result_count, results.size)
        tvListHint.text = getString(R.string.search_keyword_hint, keyword)

        if (results.isEmpty()) {
            showEmptyState(
                title = getString(R.string.search_empty_title),
                desc = getString(R.string.search_empty_desc)
            )
            return
        }

        progressSearch.isVisible = false
        emptyState.isVisible = false
        rvSearchResults.isVisible = true
        adapter.updateData(results)
    }

    private fun showLoadingState(title: String, hint: String, showRecommendations: Boolean) {
        recommendSection.isVisible = showRecommendations && recommendedPlanList.isNotEmpty()
        tvListTitle.text = title
        tvListHint.text = hint
        progressSearch.isVisible = true
        emptyState.isVisible = false
        rvSearchResults.isVisible = false
    }

    private fun showErrorState(title: String, desc: String) {
        recommendSection.isVisible = false
        tvListTitle.text = getString(R.string.search_results_title)
        tvListHint.text = getString(R.string.search_error_hint)
        showEmptyState(title, desc)
    }

    private fun showEmptyState(title: String, desc: String) {
        progressSearch.isVisible = false
        rvSearchResults.isVisible = false
        emptyState.isVisible = true
        tvEmptyTitle.text = title
        tvEmptyDesc.text = desc
    }

    private fun renderRecommendationChips(planList: List<PlanItem>) {
        chipRecommendations.removeAllViews()

        planList.forEach { plan ->
            val chip = Chip(this).apply {
                text = plan.name
                isCheckable = false
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.search_chip_bg)
                )
                setTextColor(ContextCompat.getColor(context, R.color.search_chip_text))
                chipCornerRadius = resources.getDimension(R.dimen.search_chip_corner_radius)
                chipMinHeight = resources.getDimension(R.dimen.search_chip_min_height)
                setEnsureMinTouchTargetSize(false)
                setOnClickListener {
                    etSearchInput.setText(plan.name)
                    etSearchInput.setSelection(plan.name.length)
                }
            }
            chipRecommendations.addView(chip)
        }
    }
}
