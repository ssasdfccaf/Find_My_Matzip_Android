package com.example.find_my_matzip.navTab.navTabFragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.R
import com.example.find_my_matzip.SearchAddressActivity
import com.example.find_my_matzip.databinding.FragmentProfileUpdateBinding
import com.example.find_my_matzip.model.UsersFormDto
import com.example.find_my_matzip.utiles.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Date

class ProfileUpdateFragment : Fragment() {
    lateinit var binding: FragmentProfileUpdateBinding

    // 갤러리에서 선택된 , 파일의 위치(로컬)
    private var filePath : String? = null
    
    //비밀번호 확인 여부
    private var pwCheck = false

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
        val userPwd = SharedPreferencesManager.getString("pw","") //pw는 서버에서 못가져와서 가지고 있는 정보 사용

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
                    //val originUserRole = userFormDto.user_role
                    val originUserPhone = userFormDto.userphone
                    val originUserImg = userFormDto.user_image
                    val originUserGender = userFormDto.gender


                    binding.userId.text = Editable.Factory.getInstance().newEditable(originUserId)
                    binding.userName.text = Editable.Factory.getInstance().newEditable(originUsername)
                    binding.userAddress.text = Editable.Factory.getInstance().newEditable(originUserAddr)
                    binding.userPhone.text = Editable.Factory.getInstance().newEditable(originUserPhone)


                    if(originUserGender.equals("남성")){
                        binding.radio1.isChecked = true
                    }else if(originUserGender.equals("여성")){
                        binding.radio2.isChecked = true
                    }


                    Glide.with(requireContext())
                        .load(originUserImg)
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


        //비밀번호 확인 버튼 클릭
        binding.pwCheckBtn.setOnClickListener{
            val inputPw = binding.userPwd.text

            Log.e(TAG, " userPwd : $userPwd")
            Log.e(TAG, " inputPw.text : ${inputPw}")

            //pw가 로그인된 사용자 정보와 일치하다면
            if(inputPw.toString()==userPwd){
                pwCheck = true
                binding.userPwd.setBackgroundResource(R.drawable.radius_edittext_green)
                Toast.makeText(requireContext(),"비밀번호 확인되었습니다..", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"잘못된 비밀번호입니다.", Toast.LENGTH_SHORT).show()
                inputPw.clear()
                binding.userPwd.hint = "비밀번호 입력"
            }
        }

        //업데이트 버튼 클릭
        binding.updateBtn.setOnClickListener {
            if(pwCheck){
                //비밀번호가 확인됐다면 회원정보 변경 가능

                // 스토리지 접근 도구 ,인스턴스
                val storage = MyApplication.storage
                // 스토리지에 저장할 인스턴스
                val storageRef = storage.reference

                // 파일명 생성 : userid+현재시간
                //val uuid = binding.userId.text.toString()+ Date() +System.currentTimeMillis();

                // 이미지 저장될 위치 및 파일명(파이어베이스)
                val imgRef = storageRef.child("users_img/${userId}.jpg")

                //이미지 url
                val imgStorageUrl = "https://firebasestorage.googleapis.com/v0/b/findmymatzip.appspot.com/o/users_img%2F${userId}.jpg?alt=media"

                // 변경된 회원정보 변수에 저장
                val editUsername = binding.userName.text.toString()
                val editUserAddr = binding.userAddress.text.toString() + binding.userAddressDetail.text.toString()
                val editUserPhone = binding.userPhone.text.toString()
                val editUserGender = getValue(binding.testRadioGroup)

                Log.e(TAG, " editUsername : $editUsername")
                Log.e(TAG, " editUserAddr : $editUserAddr")
                Log.e(TAG, " editUserPhone : $editUserPhone")
                Log.e(TAG, " editUserImg : $imgStorageUrl")
                Log.e(TAG, " editUserGender : $editUserGender")

                //수정 요청할 data
                var usersFormDto = UsersFormDto(
                    userid = userId,
                    user_pwd = userPwd,
                    username = editUsername,
                    user_address = editUserAddr,
                    user_role = "USER",
                    userphone = editUserPhone,
                    user_image = imgStorageUrl,
                    gender = editUserGender,
                )

                val call = userService.updateUsers(usersFormDto)

                Log.d(TAG, "userProfile.enqueue 호출전")

                call.enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "API 요청 처리 성공 (response.body()) : ${response.body()}")

                            val resultState = response.body()
                            if (resultState != null) {
                                Log.d(TAG, "회원 수정 성공")

                                //이미지 변동 있었을 경우에만 이미지 저장
                                if(filePath != null){
                                    //firebase에 이미지 저장
                                    val file = Uri.fromFile(File(filePath))

                                    // 파이어베이스 스토리지에 업로드하는 함수.
                                    imgRef.putFile(file)
                                        // 업로드 후, 수행할 콜백 함수 정의. 실패했을 경우 콜백함수 정의
                                        .addOnCompleteListener{task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(requireContext(),"스토리지 업로드 완료",Toast.LENGTH_SHORT).show()
                                                //UI다시 로드
                                                //handleUploadComplete()
                                            }else {
                                                Toast.makeText(requireContext(), "스토리지 업로드 실패", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            Toast.makeText(requireContext(),"스토리지 업로드 실패 : ${exception.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }

                            }

                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        t.printStackTrace()
                        call.cancel()
                        Log.e(TAG, " 통신 실패")
                    }
                })

            }else{
                Toast.makeText(requireContext(),"비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
            }
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

        //주소 검색후 결과값 받아오는 후처리 함수
        val getSearchResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data!!.getStringExtra("data")
                    binding.userAddress.setText(data)
                    binding.userAddressDetail.visibility =View.VISIBLE
                }
            }
        }

        //주소 검색
        binding.userAddress.setOnClickListener{
            //주소 검색 화면으로 이동
            val intent = Intent(requireContext(), SearchAddressActivity::class.java)
            getSearchResult.launch(intent)
        }


        //갤러리에서 사진 선택 후
        //가져와서 처리하는 후처리 함수
        val requestLauncher = registerForActivityResult(
            // 갤러리에서, 사진을 선택해서 가져왔을 때, 수행할 함수.
            ActivityResultContracts.StartActivityForResult()
        ) {
            // it 이라는 곳에 사진 이미지가 있음.
            if(it.resultCode === android.app.Activity.RESULT_OK) {
                if (it.data?.data != null) {
                // 이미지 불러오는 라이브러리 glide 사용하기, 코루틴이 적용이되어서, 매우 빠름.
                // with(this) this 현재 액티비티 가리킴. 대신해서.
                // 1) applicationContext
                // 2) getApplicationContext()
                Glide
                    .with(requireContext())
                    // 사진을 읽기.
                    .load(it.data?.data)
                    // 크기 지정 , 가로,세로
                    .apply(RequestOptions().override(250,200))
                    // 선택된 사진 크기 자동 조정
                    .centerCrop()
                    // 결과 뷰에 사진 넣기.
                    .into(binding.myProfileImg)

                // filePath, 갤러리에서 불러온 이미지 파일 정보 가져오기.
                // 커서에 이미지 파일이름이 등록이 되어 있음.
                val cursor = requireContext().contentResolver.query(it.data?.data as Uri,
                    arrayOf<String>(MediaStore.Images.Media.DATA),null,
                    null,null);

                cursor?.moveToFirst().let {
                    filePath = cursor?.getString(0) as String
                }
                Log.d(TAG,"filePath : ${filePath}")
                Toast.makeText(requireContext(),"filePath : ${filePath}", Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
                }

            } // 조건문 닫는 블록
        }

        //1) 버튼 이용해서, 갤러리 (2번째 앱) 호출해서, 사진 선택 후 작업하기.
        binding.galleryBtn.setOnClickListener {
            // 샘플코드 처럼, 갤러리 호출, 호출된 이미지 선택 부분,
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*"
            )
            requestLauncher.launch(intent)
        }


        return binding.root
    }

    //성별
    fun getValue(v: View?): String? {
        val male = binding.radio1
        val female = binding.radio2
        var pickValue: String? = null
        if (male.isChecked) {
            pickValue = male.text.toString()
        } else if (female.isChecked) {
            pickValue = female.text.toString()
        }
        return pickValue
    }


}
