package com.example.find_my_matzip.navTab.navTabFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.find_my_matzip.R
import kotlin.math.roundToInt


class ResInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res_info)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()
        initLayout()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun initLayout(){
        var width = 0
        var height = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API Level 30 버전
            val windowMetrics = windowManager.currentWindowMetrics
            width = windowMetrics.bounds.width()
            height = windowMetrics.bounds.height()
        } else { // API Level 30 이전 버전
            val display = windowManager.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getRealMetrics(displayMetrics)
            width = displayMetrics.widthPixels
            height = displayMetrics.heightPixels
        }
        // 바닥에 붙여서 나오게 설정
        window.setGravity(Gravity.BOTTOM)
        window.setLayout((width *0.99).roundToInt(), (height *0.3).roundToInt())
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))    // 배경화면 투명하게 하는 코드
    }
}