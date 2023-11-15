package com.example.find_my_matzip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.find_my_matzip.databinding.ActivityMyPageBinding
import com.example.find_my_matzip.databinding.FragmentHomeBinding

class MyPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}