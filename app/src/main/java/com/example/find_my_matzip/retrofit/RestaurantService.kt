package com.example.find_my_matzip.retrofit

import com.example.find_my_matzip.model.RankingDto
import com.example.find_my_matzip.model.ResListModel
import com.example.find_my_matzip.model.ResWithScoreDto
import com.example.find_my_matzip.model.RestaurantDto
import retrofit2.Call
import retrofit2.http.GET


interface RestaurantService {
    @GET("ranking")
//    fun getTop3RestaurantsByAvgScore(): Call<MutableList<RestaurantDto2>>
    fun getTop3RestaurantsByAvgScore(): Call<List<RankingDto>>

    @GET("reswithscore")
//    fun getAllRestaurantsByAvgScore(): Call<MutableList<RestaurantDto2>>
    fun getAllRestaurantsByAvgScore(): Call<List<ResWithScoreDto>>



    @GET("restaurant/main")
    fun getAll(): Call<ResListModel>

    @GET("restaurant/main")
    fun getResList() : Call<List<RestaurantDto>>

}
