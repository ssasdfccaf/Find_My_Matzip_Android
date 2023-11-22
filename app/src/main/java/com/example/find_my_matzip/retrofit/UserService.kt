package com.example.find_my_matzip.retrofit

import com.example.find_my_matzip.model.LoginResponse
import com.example.find_my_matzip.model.UserListModel
import com.example.find_my_matzip.model.UsersFormDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// 스프링의 Controller 와 같은 구간으로 스프링과 안드로이드 연동을 위한 서비스 구간
interface UserService {

    // 회원가입
    @POST("users/new")
    fun newUsers(@Body user: UsersFormDto): Call<Unit>

    //로그인
    @POST("users/loginCheck")
    fun loginCheck(@Body usersFormDto: UsersFormDto): Call<LoginResponse>

    //회원 조회
    @GET("users/users")
    fun getAll(): Call<UserListModel>
}
