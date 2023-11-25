package com.example.find_my_matzip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.example.find_my_matzip.navTab.navTabFragment.HomeFragment
import com.example.find_my_matzip.navTab.navTabFragment.MapFragment
import com.example.find_my_matzip.navTab.navTabFragment.MyPageFragment
import com.example.find_my_matzip.navTab.navTabFragment.RankingFragment
import com.example.find_my_matzip.navTab.navTabFragment.SearchReviewFragment
import com.example.find_my_matzip.databinding.ActivityHomeTabBinding
import com.example.find_my_matzip.navTab.navTabFragment.RestaurantFragment

class HomeTabActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeTabBinding
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 , 업버튼
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toggle = ActionBarDrawerToggle(
            this@HomeTabActivity, binding.drawer, R.string.open, R.string.close
        )

        // 드로워 열어주기
        toggle.syncState()

        // 탭 레이아웃
        val tabLayout = binding.bottomNavigationView

        tabLayout.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tab1 -> {replaceFragment(HomeFragment())}
                R.id.tab2 -> {replaceFragment(RestaurantFragment())}
                R.id.tab3 -> {replaceFragment(MapFragment())}
                R.id.tab4 -> {replaceFragment(RankingFragment())}
                R.id.tab5 -> {replaceFragment(MyPageFragment())}
                else -> false
            }
            return@setOnNavigationItemSelectedListener true
        }
        replaceFragment(HomeFragment())

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}