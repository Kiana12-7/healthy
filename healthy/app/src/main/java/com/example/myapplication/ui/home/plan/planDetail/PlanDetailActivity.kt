package com.example.myapplication.ui.home.plan.planDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityPlanDetailBinding

class PlanDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlanDetailBinding

    // 接收planId和planName
    private val planId: String by lazy {
        intent.getStringExtra(KEY_PLAN_ID) ?: "plan_fat_loss_default_001"
    }
    private val planName: String by lazy {
        intent.getStringExtra(KEY_PLAN_NAME) ?: "个性减脂计划"
    }

    // 训练数据
    private val fatLossCourseList by lazy {
        listOf(
            CourseItem(
                courseId = "${planId}_001",
                planId = planId,
                courseName = "全身激活燃脂循环",
                actionList = listOf(
                    TrainActionItem(
                        actionId = "action_001",
                        actionName = "开合跳",
                        groupDesc = "20次 × 4组",
                        restDesc = "组间休息：45秒",
                        videoUrl = "https://xxx.com/video1.mp4",
                        actionDesc = "【准备姿势】\n双脚并拢站立，双手自然下垂\n\n【动作过程】\n1. 双脚向两侧跳开，同时双手举过头顶\n2. 双脚跳回并拢，双手回落\n\n【呼吸节奏】\n跳起时吸气，回落时呼气\n\n【注意事项】\n膝盖微屈缓冲，不要锁死关节"
                    ),
                    TrainActionItem(
                        actionId = "action_002",
                        actionName = "高抬腿",
                        groupDesc = "30秒 × 4组",
                        restDesc = "组间休息：45秒",
                        videoUrl = "https://xxx.com/video2.mp4",
                        actionDesc = "【准备姿势】\n挺胸收腹，双脚与肩同宽\n\n【动作过程】\n1. 快速交替抬高大腿，抬至与地面平行\n2. 前脚掌落地，快速切换\n\n【呼吸节奏】\n保持均匀呼吸，不要憋气\n\n【注意事项】\n核心收紧，上半身不要晃动"
                    ),
                    TrainActionItem(
                        actionId = "action_003",
                        actionName = "勾脚跳",
                        groupDesc = "25次 × 4组",
                        restDesc = "组间休息：45秒",
                        videoUrl = "https://xxx.com/video3.mp4",
                        actionDesc = "【准备姿势】\n双脚并拢，双手放在身体两侧\n\n【动作过程】\n1. 双脚向前跳，同时勾脚尖，脚跟先落地\n2. 落地后快速回弹，重复动作\n\n【呼吸节奏】\n保持均匀呼吸\n\n【注意事项】\n膝盖微屈，避免冲击关节"
                    )
                ),
                duration = 20,
                difficulty = "易",
                isLearned = false,
                videoUrl = "https://xxx.com/course_video1.mp4"
            )
        )
    }

    companion object {
        const val KEY_PLAN_ID = "key_plan_id"
        const val KEY_PLAN_NAME = "key_plan_name" // 新增常量，传计划名称

        fun actionStart(context: Context, planId: String, planName: String) {
            val intent = Intent(context, PlanDetailActivity::class.java).apply {
                putExtra(KEY_PLAN_ID, planId)
                putExtra(KEY_PLAN_NAME, planName)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTopBar()
        initActionList()
    }

    private fun initTopBar() {
        binding.toolbar.title = planName
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    // 初始化课程列表
    private fun initActionList() {
        binding.rvCourseList.layoutManager = LinearLayoutManager(this)
        val courseAdapter = CourseListAdapter()
        courseAdapter.submitList(fatLossCourseList)

        // 点击课程 → 跳转到训练详情页
        courseAdapter.setOnItemClickListener { courseItem ->
            TrainingDetailActivity.actionStart(this@PlanDetailActivity, courseItem)
        }

        binding.rvCourseList.adapter = courseAdapter
    }
}