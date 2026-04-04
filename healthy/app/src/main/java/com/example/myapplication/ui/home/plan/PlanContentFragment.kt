package com.example.myapplication.ui.home.plan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentPlanContentBinding
import com.example.myapplication.ui.home.course.CourseAdapter

class PlanContentFragment : Fragment(R.layout.fragment_plan_content) {
    private var _binding: FragmentPlanContentBinding? = null
    private val binding get() = _binding!!

    private var homeAdapter: CourseAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPlanContentBinding.bind(view)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}