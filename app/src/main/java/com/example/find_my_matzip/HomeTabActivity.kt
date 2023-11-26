package com.example.find_my_matzip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.find_my_matzip.navTab.navTabFragment.HomeFragment
import com.example.find_my_matzip.navTab.navTabFragment.MapFragment
import com.example.find_my_matzip.navTab.navTabFragment.MyPageFragment
import com.example.find_my_matzip.navTab.navTabFragment.RankingFragment
import com.example.find_my_matzip.navTab.navTabFragment.SearchReviewFragment
import com.example.find_my_matzip.databinding.ActivityHomeTabBinding
import com.example.find_my_matzip.navTab.navTabFragment.RestaurantFragment
import com.example.find_my_matzip.utiles.SharedPreferencesManager

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

        //Drawer 네비게이션
        binding.mainDrawerView.setNavigationItemSelectedListener {
            if (it.title == "로그인"){
                Toast.makeText(this@HomeTabActivity,"로그인 화면 이동", Toast.LENGTH_SHORT).show()
                //로그인 화면으로 이동
                val intent = Intent(this@HomeTabActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            else if (it.title == "로그아웃") {
                Toast.makeText(this@HomeTabActivity,"로그아웃 화면 이동", Toast.LENGTH_SHORT).show()

                //로그인 정보 지우기
                SharedPreferencesManager.clearPreferences()

                // BackStack의 모든 데이터 삭제
                supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                //로그인 화면으로 이동
                val intent = Intent(this@HomeTabActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            else if (it.title == "메인가기") {
                Toast.makeText(this@HomeTabActivity,"메인가기 화면 이동", Toast.LENGTH_SHORT).show()

            }
            true
        }

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