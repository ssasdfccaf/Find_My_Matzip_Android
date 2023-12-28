package com.example.find_my_matzip.model

//댓글 리스트를 갖는 데이터 클래스입니다. children 속성은 해당 댓글의 하위 댓글들을 나타냄
data class CommentDto(
    val content : List<Content>,
    val children: List<Content> , // 자식 댓글을 포함한 CommentDto 목록
    val userImage: String? = null
//    val users : List<UsersFormDto>,
)

