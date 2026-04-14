package com.example.myapplication.ui.home.topBar

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.example.myapplication.R
import com.example.myapplication.ui.home.SearchActivity

class topBarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_bar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 根据你提供的 XML，准确找到 ID 为 searchView 的控件
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        // 2. 处理点击逻辑
        // 因为 SearchView 内部有多个层级，为了保证点击哪里都能跳转，
        // 我们需要找到它内部的查询框并禁止它在当前页弹出键盘，而是直接跳转
        searchView?.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // 当搜索框获得焦点时，立即跳转到独立的 SearchActivity
                val intent = Intent(requireContext(), SearchActivity::class.java)
                startActivity(intent)

                // 跳转后清除焦点，防止返回时键盘还挂着
                searchView.clearFocus()
            }
        }

        // 兼容处理：点击图标也能跳转
        searchView?.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = topBarFragment()
    }
}