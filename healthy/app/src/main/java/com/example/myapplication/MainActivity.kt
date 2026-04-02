package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.myapplication.fragment.HomeFragment
import com.example.myapplication.fragment.PersonalFragment

class MainActivity : AppCompatActivity() {

    // 声明 late init 变量
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)


        bottomNav = findViewById(R.id.bottom_navigation)


        // 默认加载首页
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }


        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
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

    // Fragment切换方法
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitAllowingStateLoss()
    }
}