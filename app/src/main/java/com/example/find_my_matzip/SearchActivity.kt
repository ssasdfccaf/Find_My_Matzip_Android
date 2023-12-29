package com.example.find_my_matzip

import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.find_my_matzip.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchBinding
    private val TAG:String = "SearchActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //검색기록 저장 스위치 이벤트
        binding.switchBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(applicationContext, "On", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Off", Toast.LENGTH_SHORT).show()
            }
        }


    }

}