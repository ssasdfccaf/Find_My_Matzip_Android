package com.example.find_my_matzip.retrofit

import com.example.find_my_matzip.model.RankingDto
import com.example.find_my_matzip.model.ResListModel
import com.example.find_my_matzip.model.ResWithScoreDto
import com.example.find_my_matzip.model.RestaurantDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RestaurantService {
    @GET("ranking")
//    fun getTop3RestaurantsByAvgScore(): Call<MutableList<RestaurantDto2>>
    fun getTop3RestaurantsByAvgScore(): Call<List<RankingDto>>

    @GET("reswithscore")
//    fun getAllRestaurantsByAvgScore(): Call<MutableList<RestaurantDto2>>
    fun getAllRestaurantsByAvgScore(): Call<List<ResWithScoreDto>>

    //우선은 게시글 상세정보만 끌어오고 끝나면 밑에 리뷰목록도 추가하기
    @GET("restaurant/{resId}")
    fun getRestaurantDtl(@Path("resId") resId : String): Call<RestaurantDto>

    @GET("restaurant/main")
    fun getAll(): Call<ResListModel>

    @GET("restaurant/main")
    fun getResList() : Call<List<RestaurantDto>>

}
