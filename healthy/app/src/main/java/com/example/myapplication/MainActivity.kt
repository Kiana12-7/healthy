package com.example.myapplication

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.myapplication.fragment.HomeFragment
import com.example.myapplication.fragment.PersonalFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        bottomNav = findViewById(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            updateMenuItemStyle(bottomNav.menu.getItem(0), true)
        }

        bottomNav.setOnItemSelectedListener { item ->
            // 重置所有样式
            for (i in 0 until bottomNav.menu.size()) {
                updateMenuItemStyle(bottomNav.menu.getItem(i), false)
            }
            // 选中项加粗
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

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

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