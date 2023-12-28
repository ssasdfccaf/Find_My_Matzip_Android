package com.example.find_my_matzip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.find_my_matzip.databinding.ActivitySearchBinding
import com.google.android.material.search.SearchView


class SearchActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchBinding
    private val TAG:String = "SearchActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_search)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    navigateSearchResult(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text change here
                return true
            }
        })


    }

    private fun navigateSearchResult(query: String) {
        // Implement the navigation to the search result using the query
        // Add your code here
    }
}