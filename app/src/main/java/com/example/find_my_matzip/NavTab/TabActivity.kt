package com.example.find_my_matzip.NavTab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.find_my_matzip.NavTab.NavTabFragment.HomeFragment
import com.example.find_my_matzip.NavTab.NavTabFragment.MapFragment
import com.example.find_my_matzip.NavTab.NavTabFragment.MyPageFragment
import com.example.find_my_matzip.NavTab.NavTabFragment.RankingFragment
import com.example.find_my_matzip.NavTab.NavTabFragment.SearchReviewFragment
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.ActivityTabBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class TabActivity : AppCompatActivity() {
    lateinit var binding: ActivityTabBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tabLayout = binding.bottomNavigationView


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tab1 -> {
                    replaceFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab2 -> {
                    replaceFragment(MapFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab3 -> {
                    replaceFragment(SearchReviewFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab4 -> {
                    replaceFragment(RankingFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab5 -> {
                    replaceFragment(MyPageFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                else -> false
            }
        }


        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}