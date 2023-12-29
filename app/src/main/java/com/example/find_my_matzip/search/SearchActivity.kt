package com.example.find_my_matzip.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.find_my_matzip.HomeTabActivity
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchBinding
    private var searchType = "default"
    private val TAG:String = "SearchActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager

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

                showResult()
                false
            }

        binding.boardBtn.setOnClickListener {

            searchType = "board"
        }

        binding.userBtn.setOnClickListener {
            searchType = "user"
        }


    }//onCreateView

    private fun showResult(){
        if(binding.searchView.text.isNullOrEmpty()){
            //최근 검색어 Frag.(default)
            searchType = "default"
            binding.currentView.visibility = View.VISIBLE
            binding.fragChange.visibility = View.GONE
        }else{
            when(searchType){
                "board" ->{
                    binding.currentView.visibility = View.GONE
                    binding.fragChange.visibility = View.VISIBLE
                }
                "user" -> {
                    binding.currentView.visibility = View.GONE
                    binding.fragChange.visibility = View.VISIBLE
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


}