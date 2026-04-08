package com.example.myapplication.data.model

sealed class CourseItem {
    abstract val id: Int

    // 课程视频项
    data class TrainingVideo(
        override val id: Int,
        val title: String,
        val trainerName: String,    // 教练名
        val coverUrl: String,
        val videoUrl: String,
        val duration: String,
        val difficultyTag: String   // 难度标签：初级/中级/高级
    ) : CourseItem()
}