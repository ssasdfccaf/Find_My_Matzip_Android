package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.ProfileUpdateFragment
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentMyPageBinding
import com.example.find_my_matzip.model.ProfileDto
import com.example.find_my_matzip.navTab.adapter.BoardRecyclerAdapter
import com.example.find_my_matzip.navTab.adapter.ProfileAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMyPageBinding
    lateinit var adapter: ProfileAdapter

    //    lateinit var adapter2: BoardRecyclerAdapter
    lateinit var boardAdapter: BoardRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(layoutInflater, container, false)
        boardAdapter = BoardRecyclerAdapter(this@MyPageFragment, emptyList())
        binding.boardRecyclerView.adapter = boardAdapter

        binding.updateBtn.setOnClickListener {
//            profileUpdateFragment 회원수정창(타 프레그먼트로) 이동하는 코드
            val profileUpdateFragment = ProfileUpdateFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, profileUpdateFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }


        val userService = (context?.applicationContext as MyApplication).userService
        val profileList = userService.getProfile("matzip5")
        Log.d("MyPageFragment", "profileList.enqueue 호출전 : ")

        profileList.enqueue(object : Callback<ProfileDto> {
            override fun onResponse(call: Call<ProfileDto>, response: Response<ProfileDto>) {
                Log.d("MyPageFragment", "도착 확인: ")
                val profileDto = response.body()
                Log.d("MyPageFragment", "도착 확인1: profileList $profileDto")
                Log.d("MyPageFragment", "도착 확인2: profileList ${profileDto?.boards}")
                Log.d("MyPageFragment", "도착 확인3: countFromUser ${profileDto?.countFromUser}")
                Log.d("MyPageFragment", "도착 확인4: countToUser ${profileDto?.countToUser}")
                Log.d("MyPageFragment", "도착 확인5: followerDtoList ${profileDto?.followerDtoList}")
                Log.d("MyPageFragment", "도착 확인5: followCheck ${profileDto?.followCheck}")
                if (profileDto != null) {
                    // 팔로워 팔로우수
                    binding.countFromUser.text=profileDto.countFromUser.toString()
                    binding.countToUser.text=profileDto.countToUser.toString()
                    binding.countBoard.text=profileDto.countBoard.toString()
                    binding.followCheck.text=profileDto.followCheck.toString()

                    // 유저정보
                    binding.userId.text = profileDto.pageUserDto.userid
//                    binding.userName.text = profileDto.pageUserDto.username
//                    binding.userAddress.text = profileDto.pageUserDto.user_address
//                    binding.userPhone.text = profileDto.pageUserDto.userphone
//                    binding.userRole.text = profileDto.pageUserDto.user_role
//                    binding.gender.text = profileDto.pageUserDto.gender

                    boardAdapter = BoardRecyclerAdapter(this@MyPageFragment, profileDto.boards.content)


                    // 다른 필요한 데이터들도 마찬가지로 설정

                    Glide.with(requireContext())
                        .load(profileDto.pageUserDto.user_image)
                        .override(900, 900)
                        .into(binding.userImage)

                    Log.d("MyPageFragment", "도착 확인2: profileList ${profileDto?.boards}")
                    ProfileAdapter(this@MyPageFragment, listOf(profileDto.pageUserDto))

                    binding.boardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    BoardRecyclerAdapter(this@MyPageFragment, profileDto.boards.content)
                    binding.boardRecyclerView.adapter = boardAdapter



                } else {
                    Log.e("MyPageFragment", "Response body is null.")
                }
            }

            override fun onFailure(call: Call<ProfileDto>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                Log.e("MyPageFragment", " 통신 실패")
            }
        })

        return binding.root
    }
}
