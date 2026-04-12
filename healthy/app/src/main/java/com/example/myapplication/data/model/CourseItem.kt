package com.example.myapplication.data.model

import java.io.Serializable

// 给父类实现Serializable，确保子类可序列化
sealed class CourseItem : Serializable {
    abstract val id: Int

    // 课程视频项
    data class TrainingVideo(
        override val id: Int,
        val title: String,
        val trainerName: String,    // 教练名
        val coverUrl: String,
        val videoUrl: String,       // 视频地址
        val duration: Int,
        val difficultyTag: String,  // 【修复】末尾加上逗号
        val preparePose: String,
        val actionProcess: String,
        val breathRhythm: String,
        val attention: String
    ) : CourseItem()
}