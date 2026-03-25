package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class CourseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 恢复成加载你原来的课程布局（如果你有专门的课程布局，把下面的改成 R.layout.fragment_course）
        return inflater.inflate(R.layout.fragment_home_content, container, false)
    }
}