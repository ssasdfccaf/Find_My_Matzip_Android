package com.example.find_my_matzip.navTab.navTabFragment.near

import com.example.find_my_matzip.model.ResWithScoreDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantApiService {

    // ... (기존 코드 생략)

    @GET("restaurants/nearby")
    fun getNearbyRestaurants(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius") radius: Int
    ): Call<List<ResWithScoreDto>>
}