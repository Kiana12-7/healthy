package com.example.myapplication

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_navigation)

        // 默认显示首页，默认加粗选中
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            updateMenuItemStyle(bottomNav.menu.getItem(0), true)
        }

        // 导航选中监听
        bottomNav.setOnNavigationItemSelectedListener { item ->
            // 先把所有项恢复正常样式
            for (i in 0 until bottomNav.menu.size()) {
                updateMenuItemStyle(bottomNav.menu.getItem(i), false)
            }
            // 当前选中项加粗
            updateMenuItemStyle(item, true)

            // 切换Fragment（和你的menu id完全匹配）
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.navigation_personal -> {
                    replaceFragment(PersonalFragment())
                    true
                }
                else -> false
            }
        }
    }

    // 替换Fragment的辅助方法
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