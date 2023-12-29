package com.example.find_my_matzip.retrofit

import com.example.find_my_matzip.model.CommentDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


// 스프링의 Controller 와 같은 구간으로 스프링과 안드로이드 연동을 위한 서비스 구간
interface CommentService {

    @POST("comment/save")
    fun save(@Body commentDto: CommentDto?): Call<Unit>

    @POST("comment/saveReply/{parentId}")
    fun saveReply(
        @Body commentDto: CommentDto?,
        @Path("parentId") parentId: Long?
    ): Call<Unit>
}



