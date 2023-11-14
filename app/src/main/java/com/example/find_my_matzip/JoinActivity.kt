package com.example.find_my_matzip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.find_my_matzip.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {

    lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}