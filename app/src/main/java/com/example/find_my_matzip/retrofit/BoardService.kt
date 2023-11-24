package com.example.find_my_matzip.retrofit

import com.example.find_my_matzip.model.MainBoardDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BoardService {
    @GET(" ")
    fun getAllBoards(): Call<List<MainBoardDto>>

    @GET("pagerbleMain")
    fun getAllBoardsPager(@Query("page") page: Int): Call<List<MainBoardDto>>

//    @GET("pagerbleMain")
//    fun getAllBoardsPager(): Call<List<MainBoardDto>>

}