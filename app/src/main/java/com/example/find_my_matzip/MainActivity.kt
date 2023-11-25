package com.example.find_my_matzip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.find_my_matzip.databinding.ActivityMainBinding
import com.example.find_my_matzip.navTab.TabActivity
import com.example.find_my_matzip.utiles.SharedPreferencesManager

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //자동로그인 설정돼있다면
        val autoLogin = SharedPreferencesManager.getBoolean("autoLogin",false)
        if(autoLogin){
            Toast.makeText(this@MainActivity,"자동 로그인", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, HomeTabActivity::class.java)
            startActivity(intent)
        }


        binding.joinBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, JoinActivity::class.java)
            startActivity(intent)
        }
        binding.loginBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.moveHome.setOnClickListener {
            val intent = Intent(this@MainActivity, TabActivity::class.java)
            startActivity(intent)
        }
    }
}