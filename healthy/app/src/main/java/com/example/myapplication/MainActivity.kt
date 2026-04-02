package com.example.myapplication

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.example.myapplication.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.myapplication.ui.personal.PersonalFragment


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
            // 将第一个导航项设置为选中加粗样式
            updateMenuItemStyle(bottomNav.menu[0], true)
        }

        // 设置导航项选中监听
        bottomNav.setOnItemSelectedListener { item ->
            // 先将所有导航项恢复为普通文字样式
            for (i in 0 until bottomNav.menu.size) {
                updateMenuItemStyle(bottomNav.menu[i], false)
            }
            // 当前选中项加粗
            updateMenuItemStyle(item, true)
            // 根据选中的导航项 ID 切换对应 Fragment
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

    /**
     * 替换页面中的 Fragment
     * @param fragment 需要显示的目标 Fragment
     */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // 替换容器内的 Fragment
            .commit() // 提交事务
    }

    /**
     * 更新底部导航栏菜单项的文字样式
     * @param item 要修改的导航菜单项
     * @param isSelected true=选中加粗，false=未选中正常
     */
    private fun updateMenuItemStyle(item: MenuItem, isSelected: Boolean) {
        // 获取菜单项的原始文字
        val title = item.title.toString()
        // 创建可设置样式的字符串
        val spannableString = SpannableString(title)
        // 根据选中状态设置文字粗细
        if (isSelected) {
            // 加粗
            spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        } else {
            // 正常
            spannableString.setSpan(StyleSpan(Typeface.NORMAL), 0, title.length, 0)
        }
        // 将设置好样式的文字重新设置给菜单项
        item.title = spannableString
    }
}