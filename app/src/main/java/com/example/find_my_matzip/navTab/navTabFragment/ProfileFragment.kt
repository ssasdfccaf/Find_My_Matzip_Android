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
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentMyPageBinding
import com.example.find_my_matzip.databinding.FragmentProfileBinding
import com.example.find_my_matzip.databinding.FragmentSearchReviewBinding
import com.example.find_my_matzip.model.FollowerDto
import com.example.find_my_matzip.model.ProfileDto
import com.example.find_my_matzip.navTab.adapter.BoardRecyclerAdapter
import com.example.find_my_matzip.navTab.adapter.BoardRecyclerAdapter2
import com.example.find_my_matzip.navTab.adapter.ProfileAdapter
import com.example.find_my_matzip.navTab.adapter.ProfileAdapter2
import com.example.find_my_matzip.utiles.SharedPreferencesManager
import com.example.find_my_matzip.utils.CustomDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    companion object {
        // ProfileFragment의 인스턴스를 생성하고, 전달할 데이터를 설정하는 메서드?
        fun newInstance(userId: String): ProfileFragment {
            // 1. ProfileFragment의 새로운 인스턴스를 생성합니다.
            val fragment = ProfileFragment()
            // 2. Bundle 객체를 만들어서 그 안에 "userId"라는 키로 전달받은 userId 값을 넣습니다.
            val args = Bundle()
            args.putString("userId", userId)
            // 3. ProfileFragment의 arguments 속성에 해당 Bundle을 설정합니다.
            fragment.arguments = args
            // 4. 설정된 프래그먼트를 반환합니다.
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
        // 보드 어댑터
        boardAdapter = BoardRecyclerAdapter2(this@ProfileFragment, emptyList())
        binding.boardRecyclerView.adapter = boardAdapter

// 전달된 userId 값 확인
        val pageUserId = arguments?.getString("userId")
        if (pageUserId != null) {
            // userId를 사용하여 프로필을 조회하거나 다른 작업 수행
            val userService = (context?.applicationContext as MyApplication).userService
            val profileList = userService.getProfile(pageUserId)

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
                        binding.pageUserId.text = profileDto.pageUserDto.userid
                        Log.d("ProfileFragment", "Page User ID: ${profileDto.pageUserDto.userid}")
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
                        // 프로필 어댑터 및 보드 어댑터 업데이트
                        ProfileAdapter2(this@ProfileFragment, listOf(profileDto.pageUserDto))
                        boardAdapter =
                            BoardRecyclerAdapter2(this@ProfileFragment, profileDto.boards.content)

                        binding.boardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.boardRecyclerView.adapter = boardAdapter

                    } // 팔로워 목록 클릭 시 다이얼로그로 팔로워 리스트 (리사이클러)
                    binding.follower.setOnClickListener {
                        val followerList: List<FollowerDto> =
                            profileDto?.followerDtoList ?: emptyList()
                        Log.d("MyPageFragment", "도착 확인6: followerDtoList $followerList")

                        // 다이얼로그 생성
                        val dialog = CustomDialog(requireContext(), followerList.map { it.id }, CustomDialog.DialogType.FOLLOWER)


                        // 다이얼로그 내용 설정
                        dialog.setOnClickListener(object : CustomDialog.OnDialogClickListener {
                            override fun onClicked(name: String) {
                                // 현재 사용자의 아이디 가져오기
                                // 현재 사용자(나)의경우 MyPageFragment로 이동 헸으면 좋겠는데..
                                val currentUserId = SharedPreferencesManager.getString("id", "")
                                Log.d("ProfileFragment", "현재 사용자 아이디 ID: $currentUserId, 페이지 사용자 ID: $pageUserId")

                                // 만약 현재 사용자가 페이지 사용자와 같다면
                                if (currentUserId == pageUserId) {
                                    Log.d("ProfileFragment", "마이페이지로 이동")
                                    // 마이페이지 프래그먼트로 이동
                                    navigateToMyPageFragment()

                                } else {
                                    // 이전 데이터를 클리어하거나 필요한 컴포넌트를 초기화 (필요 시)
                                    // 예를 들어, 어댑터 내의 이전 데이터를 클리어하거나 필요한 UI 컴포넌트를 초기화
                                    // 그 후 클릭한 팔로워의 프로필로 이동
                                    navigateToUserProfile(name)
                                    Log.d("CustomDialog", "클릭한 팔로워 아이디: $name")
                                }

                            }
                        })
                        // 다이얼로그 표시
                        dialog.showDialog()
                        // 다이얼로그 내용 설정
                        dialog.setContent()
                    }}


                override fun onFailure(call: Call<ProfileDto>, t: Throwable) {
                    t.printStackTrace()
                    call.cancel()
                    Log.e("MyPageFragment", " 통신 실패")
                }
            })
        }

        return binding.root
    }


    // MyPageFragment로 이동하는 메서드
    private fun navigateToMyPageFragment() {
        // MyPageFragment로 이동하는 코드를 추가
        val myPageFragment = MyPageFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, myPageFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    // 팔로워의 프로필로 이동하는 메서드
    private fun navigateToUserProfile(userId: String) {
        // 팔로워 해당 유저의 프로필로 이동하는 코드를 추가
        val userProfileFragment = ProfileFragment.newInstance(userId)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        // 추가된 부분
        if (!isStateSaved) {
            transaction.replace(R.id.fragmentContainer, userProfileFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        } else {
            // 상태가 저장된 경우에는 커밋을 허용하지 않고 로그를 출력
            Log.w("ProfileFragment", "Transaction not committed: Fragment state already saved")
        }
    }}