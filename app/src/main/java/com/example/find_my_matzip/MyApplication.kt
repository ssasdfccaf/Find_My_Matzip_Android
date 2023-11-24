package com.example.find_my_matzip

import android.app.Application
import com.example.find_my_matzip.retrofit.BoardService
import com.example.find_my_matzip.retrofit.RestaurantService
import com.example.find_my_matzip.retrofit.UserService
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {
    companion object {
        // 이미지 저장소 , 인스턴스 도구
        lateinit var storage: FirebaseStorage
    }

    //1)통신에 필요한 인스턴스 선언 & 초기화
    var userService: UserService
    val restaurantService: RestaurantService
    var boardService : BoardService

    //통신할 서버의 URL주소 등록
    val retrofit = Retrofit.Builder()
        // 학원 ip?
        .baseUrl("http://10.100.104.26/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create())) //형변환
        .build()

    init {
        userService = retrofit.create(UserService::class.java)
        restaurantService = retrofit.create(RestaurantService::class.java)
        boardService = retrofit.create(BoardService::class.java)
    }

    // 생명주기, 최초 1회 동작을 합니다.
    override fun onCreate() {
        super.onCreate()
        // 초기화
        storage = Firebase.storage

    }

}