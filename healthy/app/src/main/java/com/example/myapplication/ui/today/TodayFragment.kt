package com.example.myapplication.ui.today

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.CourseItem
import com.example.myapplication.data.model.Result
import com.example.myapplication.data.remote.VitaDataSource
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentTodayBinding
import com.example.myapplication.ui.ai.AiChatActivity
import com.example.myapplication.ui.fitness.AbilityActivity
import com.example.myapplication.ui.fitness.WeightActivity
import com.example.myapplication.ui.home.plan.planDetail.CourseListAdapter
import com.example.myapplication.ui.home.plan.planDetail.CourseItem as PlanCourseItem
import com.example.myapplication.ui.home.plan.planDetail.TrainingDetailActivity
import com.example.myapplication.ui.video.VideoDetailActivity
import kotlinx.coroutines.launch

class TodayFragment : Fragment(R.layout.fragment_today) {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var healthAppPrefs: SharedPreferences
    private val vitaDataSource = VitaDataSource()
    private val viewModel: TodayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTodayBinding.bind(view)
        healthAppPrefs = requireContext().getSharedPreferences(
            AbilityActivity.HEALTH_APP_PREFS,
            Context.MODE_PRIVATE
        )

        setupCalendar()
        setupRecyclerViews() // 这里面包含了点击逻辑
        initClickListeners()

        // 观察日历
        viewModel.calendarDays.observe(viewLifecycleOwner) { days ->
            calendarAdapter.submitList(days)
        }

        viewModel.scrollToPosition.observe(viewLifecycleOwner) { position ->
            position?.let {
                binding.rvCalendar.post {
                    val layoutManager = binding.rvCalendar.layoutManager as? LinearLayoutManager
                    val itemWidth = (72 * resources.displayMetrics.density).toInt()
                    val offset = (binding.rvCalendar.width - itemWidth) / 2
                    layoutManager?.scrollToPositionWithOffset(it, offset)
                }
            }
        }

        viewModel.todayPlanList.observe(viewLifecycleOwner) { plans: List<PlanCourseItem>? ->
            if (plans.isNullOrEmpty()) {
                binding.rvTodayCourses.visibility = View.GONE
                binding.llEmptyState.visibility = View.VISIBLE
            } else {
                binding.rvTodayCourses.visibility = View.VISIBLE
                binding.llEmptyState.visibility = View.GONE
                (binding.rvTodayCourses.adapter as? CourseListAdapter)?.setData(plans)
            }
        }

        viewModel.courseList.observe(viewLifecycleOwner) { courses: List<CourseItem>? ->
            (binding.rvAllCourses.adapter as? TodayCourseAdapter)?.submitList(courses ?: emptyList())
        }
    }

    private fun setupRecyclerViews() {
        binding.rvTodayCourses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CourseListAdapter().apply {
                setOnItemClickListener { courseItem ->
                    TrainingDetailActivity.actionStart(requireContext(), courseItem)
                }
            }
            isNestedScrollingEnabled = false
        }

        binding.rvAllCourses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TodayCourseAdapter { course: CourseItem ->
                if (course is CourseItem.TrainingVideo) {
                    startActivity(
                        Intent(requireContext(), VideoDetailActivity::class.java).apply {
                            putExtra("VIDEO_ITEM", course)
                        }
                    )
                }
            }
            isNestedScrollingEnabled = false
        }
    }

    private fun setupCalendar() {
        calendarAdapter = CalendarAdapter { selectedDay, position ->
            viewModel.onDateSelected(selectedDay, position)
        }
        binding.rvCalendar.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = calendarAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun initClickListeners() {
        binding.llAiInput.setOnClickListener {
            startActivity(Intent(context, AiChatActivity::class.java).apply { putExtra("SOURCE", "search") })
        }
        binding.fabAiCoach.setOnClickListener {
            startActivity(Intent(context, AiChatActivity::class.java).apply { putExtra("SOURCE", "coach") })
        }
        binding.btnGenerateAiPlan.setOnClickListener {
            if (!healthAppPrefs.getBoolean(AbilityActivity.KEY_FITNESS_FORM_COMPLETED, false)) {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.dialog_fill_fitness_title))
                    .setMessage(getString(R.string.dialog_fill_fitness_message))
                    .setPositiveButton(getString(R.string.dialog_action_fill_now)) { _, _ ->
                        startFitnessFlow()
                    }
                    .setNegativeButton(getString(R.string.dialog_action_cancel), null)
                    .show()
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_generate_ai_plan_title))
                .setMessage(getString(R.string.dialog_generate_ai_plan_message))
                .setPositiveButton(getString(R.string.dialog_action_generate)) { _, _ ->
                    requestAiPlanGeneration()
                }
                .setNegativeButton(getString(R.string.dialog_action_cancel), null)
                .show()
        }
        binding.btnCustomize.setOnClickListener {
            if (healthAppPrefs.getBoolean(AbilityActivity.KEY_FITNESS_FORM_COMPLETED, false)) {
                AlertDialog.Builder(requireContext())
                    .setTitle("重新填写定制信息")
                    .setMessage("你已经填写过一次健身信息，是否重新填写并生成新的计划？")
                    .setPositiveButton("重新填写") { _, _ ->
                        startFitnessFlow()
                    }
                    .setNegativeButton("取消", null)
                    .show()
            } else {
                startFitnessFlow()
            }
        }
    }

    private fun startFitnessFlow() {
        startActivity(Intent(requireContext(), WeightActivity::class.java))
    }

    private fun requestAiPlanGeneration() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.btnGenerateAiPlan.isEnabled = false
            val originalText = binding.btnGenerateAiPlan.text
            binding.btnGenerateAiPlan.text = getString(R.string.btn_generate_ai_plan_loading)
            when (val result = vitaDataSource.generatePlan()) {
                is Result.Success -> {
                    binding.btnGenerateAiPlan.text = originalText
                    binding.btnGenerateAiPlan.isEnabled = true
                    viewModel.refreshCurrentDate()
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.dialog_generate_ai_plan_success_title))
                        .setMessage(getString(R.string.dialog_generate_ai_plan_success_message))
                        .setPositiveButton(getString(R.string.dialog_action_ok), null)
                        .show()
                }

                is Result.Error -> {
                    binding.btnGenerateAiPlan.text = originalText
                    binding.btnGenerateAiPlan.isEnabled = true
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.dialog_generate_ai_plan_failed_title))
                        .setMessage(result.exception.message ?: getString(R.string.dialog_generate_ai_plan_failed_message))
                        .setPositiveButton(getString(R.string.dialog_action_ok), null)
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
