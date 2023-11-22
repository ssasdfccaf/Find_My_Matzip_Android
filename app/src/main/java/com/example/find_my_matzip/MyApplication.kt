package com.example.find_my_matzip

import android.app.Application
import com.example.find_my_matzip.retrofit.RestaurantService
import com.example.find_my_matzip.retrofit.UserService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {
    //1)통신에 필요한 인스턴스 선언 & 초기화

    var userService: UserService
    val restaurantService: RestaurantService

    //통신할 서버의 URL주소 등록
    val retrofit = Retrofit.Builder()
        // 학원 ip?
        .baseUrl("http://10.100.104.16:80/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create())) //형변환
        .build()

    init {
        userService = retrofit.create(UserService::class.java)
        restaurantService = retrofit.create(RestaurantService::class.java)
    }

}