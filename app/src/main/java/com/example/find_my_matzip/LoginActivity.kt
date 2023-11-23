package com.example.find_my_matzip

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.find_my_matzip.databinding.ActivityLoginBinding
import com.example.find_my_matzip.model.LoginDto
import com.example.find_my_matzip.model.ResultDto
import com.example.find_my_matzip.retrofit.UserService
import com.example.find_my_matzip.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val TAG: String = "LoginActivity"
    lateinit var binding: ActivityLoginBinding
    lateinit var userService : UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 붙이기
        setSupportActionBar(binding.toolbar)

        //시스템에 있는 액션바에 업버튼 붙이기
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //로그인 버튼 클릭시
        binding.loginBtn.setOnClickListener {
            val loginId = binding.userId.text.toString()
            val loginPw = binding.userPwd.text.toString()

            if (loginId.isEmpty() || loginPw.isEmpty()) {
                showDialog("blank")
                return@setOnClickListener
            }

            //로그인 요청 정보 객체에 담기
            val loginRequest = LoginDto(
                id = loginId,
                pw = loginPw
            )

            //연결 요청
            userService = (applicationContext as MyApplication).userService
            //로그인 요청
            val call = userService.login(loginRequest)
            Log.d(TAG, "로그인 요청 - ID: ${loginRequest.id}, PW: ${loginRequest.pw}")

            //응답받은 정보 확인
            call.enqueue(object : Callback<ResultDto> {
                override fun onResponse(call: Call<ResultDto>, response: Response<ResultDto>) {
                    if (response.isSuccessful) {
                        // (jwt)API 요청 처리 성공
                        //val token = response.headers().get("token")
                        //val loginState = response.body()


//                        if (loginState != null) {
//                            if(token != null){
                        if (response.body()?.state.equals("success")) {
                            Log.d(TAG, "로그인 성공")
                            showDialog("success")

//                                val header = response.headers()
//                                Log.d(TAG, "===========response.toString()의 값 : $response")
//                                Log.d(TAG, "===========response.body()의 값 : ${response.body()}")

                            // 로그인된 유저 정보 저장
                            SharedPreferencesManager.setLoginInfo(this@LoginActivity,loginId,loginPw);

                        } else {
                            Log.d(TAG, "로그인 실패")
                            showDialog("fail")
                        }

                    } else {
                        // API 요청 실패 처리
                        Log.d(TAG, "API 요청 실패")
                        showDialog("fail")
                    }
                    Log.d(TAG, "통신 성공 - HTTP 상태 코드: ${response.code()}")
                    Log.d(TAG, "통신 성공 - 응답 메시지: ${response.body()}")
                }

                override fun onFailure(call: Call<ResultDto>, t: Throwable) {
                    // 통신 실패 처리
                    showDialog("fail")
                    Log.e(TAG, "통신 실패: ${t.message}")
                }

            })
        }

    }//onCreate

    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    private fun showDialog(type: String) {
        val dialogBuilder = AlertDialog.Builder(this)

        if (type == "success") {
            dialogBuilder.setTitle("로그인 성공")
            dialogBuilder.setMessage("로그인 성공!")
        } else if (type == "fail") {
            dialogBuilder.setTitle("로그인 실패")
            dialogBuilder.setMessage("아이디와 비밀번호를 확인해주세요")
        } else if (type == "blank") {
            dialogBuilder.setTitle("입력 필요")
            dialogBuilder.setMessage("아이디와 비밀번호를 입력해주세요")
        }

        val dialogListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    Log.d(TAG, "확인 버튼 클릭")
                    if (type == "success") {
                        // 로그인 성공 시 처리할 작업 수행
                        val intent = Intent(this@LoginActivity, HomeTabActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }

        dialogBuilder.setPositiveButton("확인", dialogListener)
        val dialog = dialogBuilder.create() // 다이얼로그 객체 생성
        dialog.show() // 다이얼로그 표시
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}