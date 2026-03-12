package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // 设置默认显示的 Fragment（例如首页）
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        // 设置导航项选中监听
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {  // 注意这里的 ID 必须与菜单文件中一致
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.page_2 -> {
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
}