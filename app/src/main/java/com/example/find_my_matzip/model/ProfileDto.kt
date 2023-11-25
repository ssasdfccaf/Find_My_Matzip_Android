package com.example.find_my_matzip.model

data class ProfileDto(
    val boards : BoardsDto,
    val pageUserDto : PageUserDto,
    val followerDtoList : List<FollowerDto>,
    val followingDtoList: List<FollowingDto>,
    val countBoard: Int,
    val loginUserDto: UsersFormDto,
    val countFromUser: Int,
    val countToUser: Int,
    val followCheck: Boolean,

    )
