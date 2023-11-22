package com.example.find_my_matzip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.find_my_matzip.databinding.ActivityJoinBinding
import com.example.find_my_matzip.model.UsersFormDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity : AppCompatActivity() {

    private val TAG: String = "JoinActivity"
    lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //회원가입
        binding.buttonInsert.setOnClickListener {

            //회원가입 요청할 user객체 구성
            var usersFormDto = UsersFormDto(
                userid = binding.userId.text.toString(),
                user_pwd = binding.userPwd.text.toString(),
                username = binding.userName.text.toString(),
                user_address = binding.userAddress.text.toString(),
                user_role = "ADMIN",
                userphone = binding.userPhone.text.toString(),
                user_image = "이미지 url입니다",
                gender = getValue(binding.testRadioGroup),
            )


            val userService = (applicationContext as MyApplication).userService
            val newUsers = userService.newUsers(usersFormDto)

            newUsers.enqueue(object: Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                    Log.d(TAG, "Request URL: ${call.request().url()}")
                    Log.d(TAG, "Request Body: ${call.request().body()}")
                    Log.d(TAG, "Response Code: ${response.code()}")
                    if(response.isSuccessful) {

                        //Users(userid=qwer, user_pwd=1234, username=1234, user_address=1234, user_role=ADMIN, userphone=1234, user_image=이미지 url입니다, gender=여자)
                        Log.d(TAG, "성공(Users) :  ${usersFormDto}")
                        Log.d(TAG, "성공(newUsers_body) :  ${response.body().toString()}")
                        //kotlin.Unit


                        val intent = Intent(this@JoinActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }else {
                        // Log the raw response for debugging purposes
                        Log.d(TAG, "서버 응답 실패: ${response.code()}")
                        try {
                            val errorBody = response.errorBody()?.string()
                            Log.d(TAG, "Error Body: $errorBody")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d(TAG, "실패 ${t.message}")
                    call.cancel()
                }


            })
        }//buttonInsert


    }

    //성별
    fun getValue(v: View?): String? {
        val male = binding.radio1
        val female = binding.radio2
        var pickValue: String? = null
        if (male.isChecked) {
            pickValue = male.text.toString()
        } else if (female.isChecked) {
            pickValue = female.text.toString()
        }
        return pickValue
    }
}