package com.example.find_my_matzip.model

//Content는 하나의 댓글
data class Content(
    val commentId: Long,
    val commentWriter: String,
    val commentContents: String,
    val boardId: Long,
    val depth: Int,
    val parentId: Long,
    val user_image: String,
    val commentCreatedTime: String,
    val children: List<Content>? = null


)
