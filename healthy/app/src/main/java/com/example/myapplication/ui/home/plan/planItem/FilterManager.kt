package com.example.myapplication.ui.home.plan.planItem

object FilterManager {

    // 核心筛选方法
    fun filterPlans(
        allPlans: List<PlanItem>,
        selectedTags: Map<FilterType, List<FilterTag>>
    ): List<PlanItem> {
        return allPlans.filter { plan ->
            // 对每个维度进行检查，所有维度都满足才算通过
            selectedTags.all { (type, tags) ->
                if (tags.isEmpty()) {
                    // 如果该维度没有选中标签，直接通过
                    true
                } else {
                    // 如果该维度有选中标签，检查计划的tagIds是否包含其中任意一个
                    val selectedTagIds = tags.map { it.id }
                    plan.tagIds.intersect(selectedTagIds).isNotEmpty()
                }
            }
        }
    }
}