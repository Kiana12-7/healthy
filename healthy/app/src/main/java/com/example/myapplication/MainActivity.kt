package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.personal.PersonalFragment
import com.example.myapplication.ui.today.TodayFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_SELECT_TODAY = "select_today"
    }

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

        // 设置导航项选中监听
        bottomNav.setOnItemSelectedListener { menuItem ->
            val targetFragment = createFragment(menuItem.itemId) ?: return@setOnItemSelectedListener false
            replaceFragment(targetFragment)
            true
        }

        if (savedInstanceState == null) {
            selectDestinationFromIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        selectDestinationFromIntent(intent)
    }

    private fun selectDestinationFromIntent(intent: Intent?) {
        val targetItemId = if (intent?.getBooleanExtra(EXTRA_SELECT_TODAY, false) == true) {
            R.id.navigation_today
        } else {
            R.id.navigation_home
        }

        if (bottomNav.selectedItemId == targetItemId) {
            replaceFragment(createFragment(targetItemId) ?: return)
        } else {
            bottomNav.selectedItemId = targetItemId
        }
    }

    private fun createFragment(itemId: Int): Fragment? {
        return when (itemId) {
            R.id.navigation_home -> HomeFragment()
            R.id.navigation_today -> TodayFragment()
            R.id.navigation_personal -> PersonalFragment()
            else -> null
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
