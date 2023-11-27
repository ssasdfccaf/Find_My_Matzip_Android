package com.example.find_my_matzip.retrofit

import com.example.find_my_matzip.model.BoardDtlDto
import com.example.find_my_matzip.model.MainBoardDto
import com.example.find_my_matzip.model.ProfileDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BoardService {
    @GET(" ")
    fun getAllBoards(): Call<List<MainBoardDto>>

    @GET("pagerbleMain")
    fun getAllBoardsPager(@Query("page") page: Int): Call<List<MainBoardDto>>

    @GET("board/{id}") // 게시글 상세페이지;
    fun getBoardDtl(@Path("id") id : String): Call<BoardDtlDto>

//    @GET("board/829") // 게시글 상세페이지;
//    fun getBoardDtl(): Call<BoardDtlDto>


}