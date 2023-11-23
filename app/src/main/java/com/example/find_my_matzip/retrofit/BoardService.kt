package com.example.find_my_matzip.retrofit

import com.example.find_my_matzip.model.MainBoardDto
import retrofit2.Call
import retrofit2.http.GET

interface BoardService {
    @GET(" ")
    fun getAllBoards(): Call<List<MainBoardDto>>

}