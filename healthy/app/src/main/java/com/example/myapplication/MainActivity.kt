package com.example.myapplication

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.fragment.HomeFragment
import com.example.myapplication.fragment.PersonalFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.view.size
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 全屏模式
        setContentView(R.layout.activity_main) // 加载页面布局

        // 默认显示首页，默认加粗选中
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            updateMenuItemStyle(bottomNav.menu[0], true)
        }

        // 找到底部导航
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        // 设置导航项选中监听
        bottomNav.setOnItemSelectedListener { item ->
            // 先把所有项恢复正常样式
            for (i in 0 until bottomNav.menu.size) {
                updateMenuItemStyle(bottomNav.menu[i], false)
            }
            // 当前选中项加粗
            updateMenuItemStyle(item, true)

            // 切换Fragment
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
                    true // 允许选中，按钮会高亮、切换页面
                }
                R.id.navigation_personal -> {
                    replaceFragment(PersonalFragment())
                    true
                }
                else -> false
            }
        }
    }

    // 替换 Fragment 的辅助方法
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // 文字加粗/取消加粗的样式控制
    private fun updateMenuItemStyle(item: MenuItem, isSelected: Boolean) {
        val title = item.title.toString()
        val spannableString = SpannableString(title)

        if (isSelected) {
            // 选中状态：加粗
            spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        } else {
            // 未选中状态：正常
            spannableString.setSpan(StyleSpan(Typeface.NORMAL), 0, title.length, 0)
        }

        item.title = spannableString
    }
}