package com.example.find_my_matzip

import android.app.Application
import com.example.find_my_matzip.retrofit.RestaurantService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {
    //1)통신에 필요한 인스턴스 선언 & 초기화

    val restaurantService: RestaurantService

    //통신할 서버의 URL주소 등록
    val retrofit = Retrofit.Builder()
//        .baseUrl("http://10.100.104.54:80/")
            .baseUrl("http://192.168.0.69:80/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create())) //형변환
        .build()

    init {
        restaurantService = retrofit.create(RestaurantService::class.java)
    }

}