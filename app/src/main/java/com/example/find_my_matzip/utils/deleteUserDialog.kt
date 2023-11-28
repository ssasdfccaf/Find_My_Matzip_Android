package com.example.find_my_matzip.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.example.find_my_matzip.R

class deleteUserDialog
    constructor(context: Context) : Dialog(context){

    init {
        //외부 화면을 터치할 때 다이얼로그 종료되지 않게 설정
        setCanceledOnTouchOutside(false)
        //배경 투명하게 설정
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_password)

        //Dialog 크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

    }

}