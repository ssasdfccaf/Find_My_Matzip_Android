package com.example.find_my_matzip.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.find_my_matzip.HomeTabActivity
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.ActivitySearchBinding
import com.example.find_my_matzip.model.SearchDto
import com.example.find_my_matzip.navTab.adapter.RestaurantRecyclerAdapter
import com.example.find_my_matzip.search.adapter.SearchHistoryRecyclerViewAdapter
import com.example.find_my_matzip.utiles.SharedPreferencesManager
import com.example.find_my_matzip.utiles.SharedPreferencesManager.saveSearchHistory
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SearchActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchBinding
    lateinit var adapter: SearchHistoryRecyclerViewAdapter

    private var searchType = "default"
    private val TAG:String = "SearchActivity"

    //test data
    val testList: List<SearchDto> = listOf(
        SearchDto("Pizza", Date()),
        SearchDto("Sushi", Date()),
        SearchDto("Burger", Date()),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showResult()

        //home으로 이동
        binding.homeBtn.setOnClickListener(){
            val intent = Intent(this@SearchActivity, HomeTabActivity::class.java)
            startActivity(intent)
        }

        //searchView에서 검색클릭 -> text가지고 searchView exit
        binding.searchView
            .editText
            .setOnEditorActionListener {v,actionId,event ->
                binding.searchBar.setText(binding.searchView.text)
                binding.searchView.hide()

                //최근 검색어에 저장
                SharedPreferencesManager.saveSearchHistory(binding.searchBar.text.toString())

                showResult()
                false
            }


        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Tab이 선택되었을 때
                when (tab!!.position) {
                    0 -> {
                        searchType = "board"
                        showResult()
                    }
                    1 -> {
                        searchType = "user"
                        showResult()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Tab이 선택되지 않았을 때
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Tab이 다시 선택되었을 때
            }
        })



    }//onCreateView

    private fun showResult(){
        if(binding.searchView.text.isNullOrEmpty()){
            //최근 검색어 (default)
            searchType = "default"
            binding.currentView.visibility = View.VISIBLE
            binding.fragChange.visibility = View.GONE


            showSearchHistory()

        }else{
            when(searchType){
                "board" ->{
                    binding.currentView.visibility = View.GONE
                    binding.fragChange.visibility = View.VISIBLE
                    replaceFragment(BoardSearchFragment.newInstance(binding.searchBar.text.toString()))
                }
                "user" -> {
                    binding.currentView.visibility = View.GONE
                    binding.fragChange.visibility = View.VISIBLE
                    replaceFragment(UserSearchFragment.newInstance(binding.searchBar.text.toString()))
                }
                else -> searchType = "board"
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun showSearchHistory(){
        val existingSet = SharedPreferencesManager.getSearchHistory()

        //형변환(Hash -> ArrayList)
        val searchDtoList = existingSet?.map {
            val (text, dateString) = it.split(",")
            SearchDto(text, SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).parse(dateString))
        }

        val layoutManager = LinearLayoutManager(this)
        adapter = SearchHistoryRecyclerViewAdapter(this@SearchActivity, searchDtoList)

        binding.searchHistoryRecyclerView.layoutManager = layoutManager
        binding.searchHistoryRecyclerView.adapter = adapter
    }


}