package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.databinding.FragmentMyPageBinding
import com.example.find_my_matzip.databinding.FragmentProfileBinding
import com.example.find_my_matzip.databinding.FragmentSearchReviewBinding
import com.example.find_my_matzip.model.ProfileDto
import com.example.find_my_matzip.navTab.adapter.BoardRecyclerAdapter
import com.example.find_my_matzip.navTab.adapter.BoardRecyclerAdapter2
import com.example.find_my_matzip.navTab.adapter.ProfileAdapter
import com.example.find_my_matzip.navTab.adapter.ProfileAdapter2
import com.example.find_my_matzip.utiles.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    companion object {
        // newInstance 메서드 정의
        fun newInstance(userId: String): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString("userId", userId)
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var binding: FragmentProfileBinding
    lateinit var adapter: ProfileAdapter2
    lateinit var boardAdapter: BoardRecyclerAdapter2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        // 보드 어댑터
        boardAdapter = BoardRecyclerAdapter2(this@ProfileFragment, emptyList())
        binding.boardRecyclerView.adapter = boardAdapter

        // 전달된 userId 값 확인
        val userId = arguments?.getString("userId")
        if (userId != null) {
            // userId를 사용하여 프로필을 조회하거나 다른 작업 수행
            val userService = (context?.applicationContext as MyApplication).userService
            val profileList = userService.getProfile(userId)

            profileList.enqueue(object : Callback<ProfileDto> {
                override fun onResponse(call: Call<ProfileDto>, response: Response<ProfileDto>) {
                    val profileDto = response.body()
                    if (profileDto != null) {
                        // 팔로워 팔로우수
                        binding.countFromUser.text = profileDto.countFromUser.toString()
                        binding.countToUser.text = profileDto.countToUser.toString()
                        binding.countBoard.text = profileDto.countBoard.toString()
//                    binding.followCheck.text=profileDto.followCheck.toString()

                        // 유저정보
                        binding.userId.text = profileDto.pageUserDto.userid
//                    binding.userName.text = profileDto.pageUserDto.username
//                    binding.userAddress.text = profileDto.pageUserDto.user_address
//                    binding.userPhone.text = profileDto.pageUserDto.userphone
//                    binding.userRole.text = profileDto.pageUserDto.user_role
//                    binding.gender.text = profileDto.pageUserDto.gender


                        // 다른 필요한 데이터들도 마찬가지로 설정

                        Glide.with(requireContext())
                            .load(profileDto.pageUserDto.user_image)
                            .override(900, 900)
                            .into(binding.userImage)

                        Log.d("MyPageFragment", "도착 확인2: profileList ${profileDto?.boards}")

                        ProfileAdapter2(this@ProfileFragment, listOf(profileDto.pageUserDto))
                        boardAdapter =
                            BoardRecyclerAdapter2(this@ProfileFragment, profileDto.boards.content)

                        binding.boardRecyclerView.layoutManager =
                            LinearLayoutManager(requireContext())
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
        }

        return binding.root
    }

    fun ShowMessage(title: String?, Message: String?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(Message)
        builder.show()
    }
}