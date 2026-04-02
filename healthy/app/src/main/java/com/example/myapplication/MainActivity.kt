package com.example.myapplication

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.fragment.PersonalFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    // 类成员变量
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 【关键修改 1】：先通过 findViewById 给类成员变量赋值
        // 去掉了之前的 "val"，确保初始化的是类顶部的那个 bottomNav
        bottomNav = findViewById(R.id.bottom_navigation)

        // 【关键修改 2】：赋值完成后再进行逻辑判断
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            // 此时 bottomNav 已经初始化，不会再报 UninitializedPropertyAccessException
            updateMenuItemStyle(bottomNav.menu[0], true)
        }

        // 设置导航项选中监听
        bottomNav.setOnItemSelectedListener { item ->
            // 先把所有项恢复正常样式
            for (i in 0 until bottomNav.menu.size()) {
                updateMenuItemStyle(bottomNav.menu[i], false)
            }
            // 当前选中项加粗
            updateMenuItemStyle(item, true)

            // 切换Fragment
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
            spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        } else {
            spannableString.setSpan(StyleSpan(Typeface.NORMAL), 0, title.length, 0)
        }

        item.title = spannableString
    }
}