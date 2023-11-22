package com.example.find_my_matzip.model

//로그인
data class LoginResponse(
    val token : String,
    val status: String,
    val message: String,
    val usersFormDto: UsersFormDto
)

//회원가입
data class RegisterResponse(val status: String, val message: String)

//회원정보 수정
data class UpdateResponse(
    val success: Boolean,
    val message: String
)