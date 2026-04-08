package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.personal.PersonalFragment
import com.example.myapplication.ui.today.TodayFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    // 底部导航栏控件实例（延迟初始化）
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 开启页面全屏模式
        enableEdgeToEdge()
        // 加载主页面布局
        setContentView(R.layout.activity_main)
        // 绑定布局中的底部导航栏控件
        bottomNav = findViewById(R.id.bottom_navigation)

        // 页面首次创建时
        if (savedInstanceState == null) {
            // 默认显示首页 Fragment
            replaceFragment(HomeFragment())
        }

        // 设置导航项选中监听
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.navigation_today -> {
                    replaceFragment(TodayFragment())
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

    /**
     * 替换页面中的 Fragment
     * @param fragment 需要显示的目标 Fragment
     */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // 替换容器内的 Fragment
            .commit() // 提交事务
    }
}