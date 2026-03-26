package com.example.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.adapter.MyPlanItemRecyclerViewAdapter

class PlanItemFragment : Fragment() {

    // 计划数据
    private val planList = listOf(
        "个性减脂计划",
        "告别肚腩计划",
        "学生党·全身增肌计划",
        "大正爱跑步·轻松拿捏5公里…",
        "个性跑步计划",
        "定制大体重计划",
        "瘦腹减围·型男打造计划",
        "10天冲刺·极速燃脂计划",
        "高质量睡眠计划",
        "轻松燃脂·个性跑步计划",
        "告别脂肪胸计划",
        "全身突击燃脂计划",
        "全身增肌·型男打造计划",
        "大正爱跑步·全力奔跑5公里",
        "腹肌撕裂计划",
        "热汗瑜伽·减脂塑形计划",
        "单车智能计划",
        "跳绳·高效燃脂计划",
        "告别疼痛·肩颈改善计划",
        "肩臂·强化增肌计划",
        "7天冲刺·全身燃脂计划",
        "定制瘦身计划",
        "经典胸肩·强效增肌计划",
        "7天瘦全身·晚安燃脂计划",
        "学生专属·瘦全身计划",
        "肩臂减脂计划",
        "科林滚滚·搏击有氧计划",
        "学生升学生涯减脂计划",
        "哑铃上肢增肌计划",
        "马甲线控制·会员塑造计划",
        "学生燃脂计划",
        "优质增肌雕刻计划",
        "21天健康体质增强计划",
        "7天体能恢复计划",
        "快燃元气·宫吊漫跑计划",
        "跳绳燃脂计划",
        "高效燃脂·保持健康计划",
        "备战体测·高原模拟马拉松备赛"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 加载列表布局
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = MyPlanItemRecyclerViewAdapter(planList)
            }
        }
        return view
    }
}