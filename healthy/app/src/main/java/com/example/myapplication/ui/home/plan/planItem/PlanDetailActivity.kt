package com.example.myapplication.ui.home.plan.planItem

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityPlanDetailBinding
import com.example.myapplication.ui.video.VideoDetailActivity

class PlanDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanDetailBinding
    private lateinit var courseAdapter: CourseListAdapter

    private var planId: String = ""
    private var planName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            planId = it.getString("plan_id", "")
            planName = it.getString("plan_name", "")
        }

        initToolbar()
        initRecyclerView()
        loadPlanDetailData()
    }

    private fun initToolbar() {
        binding.toolbar.title = planName
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initRecyclerView() {
        courseAdapter = CourseListAdapter()
        binding.rvCourseList.apply {
            layoutManager = LinearLayoutManager(this@PlanDetailActivity)
            adapter = courseAdapter
        }

        courseAdapter.setOnItemClickListener { courseItem ->
            val intent = Intent(this, VideoDetailActivity::class.java).apply {
                putExtra("course_id", courseItem.courseId)
                putExtra("course_name", courseItem.courseName)
                putExtra("video_url", courseItem.videoUrl)
            }
            startActivity(intent)
        }
    }

    private fun loadPlanDetailData() {
        val mockPlanDetail = getMockPlanDetail(planId)
        binding.tvTotalCourse.text = "${mockPlanDetail.totalCourseCount} 节课程"
        binding.tvTotalUser.text = "${mockPlanDetail.totalUserCount} 人已想练"
        courseAdapter.setData(mockPlanDetail.courseList)
    }

    private fun getMockPlanDetail(planId: String): PlanDetail {
        val planItem = PlanItem.getAllPlans().firstOrNull { it.id == planId }
        val planName = planItem?.name ?: "训练计划"

        val mockCourseList = listOf(
            CourseItem("${planId}_001", planId, "无跳无器械·30分钟宿舍暴汗减脂瘦身", 30, "K1", false, "https://xxx.com/video1.mp4"),
            CourseItem("${planId}_002", planId, "30分钟无蹲跳，细腰提臀改善驼背", 31, "K3", false, "https://xxx.com/video2.mp4"),
            CourseItem("${planId}_003", planId, "每周3次彻底瘦身:40分钟无跳跃进阶", 41, "K3", false, "https://xxx.com/video3.mp4"),
            CourseItem("${planId}_004", planId, "每周4次暴汗瘦身塑腰臀比·无蹲跳", 30, "K3", false, "https://xxx.com/video4.mp4"),
            CourseItem("${planId}_005", planId, "无跳不伤膝40min暴汗操·高效减脂", 41, "K3", false, "https://xxx.com/video5.mp4")
        )

        return PlanDetail(
            planId = planId,
            planName = planName,
            totalCourseCount = mockCourseList.size,
            totalUserCount = (100..5000).random(),
            courseList = mockCourseList
        )
    }

    companion object {
        fun actionStart(context: android.content.Context, planId: String, planName: String) {
            val intent = Intent(context, PlanDetailActivity::class.java).apply {
                putExtra("plan_id", planId)
                putExtra("plan_name", planName)
            }
            context.startActivity(intent)
        }
    }
}