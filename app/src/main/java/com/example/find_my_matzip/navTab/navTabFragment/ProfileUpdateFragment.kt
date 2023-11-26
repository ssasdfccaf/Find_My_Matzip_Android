package com.example.find_my_matzip.navTab.navTabFragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.databinding.FragmentProfileUpdateBinding
import com.example.find_my_matzip.model.UsersFormDto
import com.example.find_my_matzip.utiles.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileUpdateFragment : Fragment() {
    lateinit var binding: FragmentProfileUpdateBinding

    //editview 밖의 공간 클릭시 키보드 내리기 기능 구현
    //설정 1)
    private lateinit var imm: InputMethodManager
    private val TAG: String = "ProfileUpdateFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileUpdateBinding.inflate(layoutInflater)
        //설정 2)
        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileUpdateBinding.inflate(layoutInflater, container, false)


        //로그인 정보
        val userId = SharedPreferencesManager.getString("id","")
        val userPwd = SharedPreferencesManager.getString("pw","")

        val userService = (context?.applicationContext as MyApplication).userService
        val userProfile = userService.findbyId(userId)

        Log.d(TAG, "userProfile.enqueue 호출전")

        userProfile.enqueue(object : Callback<UsersFormDto> {
            override fun onResponse(call: Call<UsersFormDto>, response: Response<UsersFormDto>) {
                val userFormDto = response.body()
                Log.d(TAG, "API 요청 처리 성공 (response.body()) : ${response.body()}")

                if (userFormDto != null) {
                    Log.d(TAG, "회원정보 가져오기 성공 (userFormDto) : ${userFormDto.toString()}")

                    // 가져온 회원정보 변수에 저장
                    val originUserId = userFormDto.userid
                    val originUsername = userFormDto.username
                    val originUserAddr = userFormDto.user_address
                    val originUserRole = userFormDto.user_role
                    val originUserPhone = userFormDto.userphone
                    val originUserImg = userFormDto.user_image
                    val originUserGender = userFormDto.gender


                    binding.userId.text = Editable.Factory.getInstance().newEditable(originUserId)
                    binding.userPwd.text = Editable.Factory.getInstance().newEditable(userPwd) //pw는 못가져와서 가지고 있는 정보 사용
                    binding.userName.text = Editable.Factory.getInstance().newEditable(originUsername)
                    binding.userAddress.text = Editable.Factory.getInstance().newEditable(originUserAddr)
                    binding.userPhone.text = Editable.Factory.getInstance().newEditable(originUserPhone)


                    Glide.with(requireContext())
                        .load(userFormDto.user_image)
                        .override(900, 900)
                        .into(binding.myProfileImg)
                }
            }

            override fun onFailure(call: Call<UsersFormDto>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                Log.e(TAG, " 통신 실패")
            }
        })



        binding.updateBtn.setOnClickListener {
            
        }

        //취소버튼 -> mypage로 이동
        binding.cancelBtn.setOnClickListener {
            //현재 fragment 아예 지우고, 이전 fragment 띄우기
            requireActivity().supportFragmentManager.popBackStack()
        }


        //설정 3)
        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Hide the keyboard when the user clicks outside of the EditText
                val view = requireActivity().currentFocus
                if (view != null) {
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            false
        }


        return binding.root
    }
}
