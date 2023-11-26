package com.example.find_my_matzip

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.find_my_matzip.databinding.ActivityJoinBinding
import com.example.find_my_matzip.model.UsersFormDto
import com.example.find_my_matzip.utils.PermissionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Date
import java.util.UUID

class JoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityJoinBinding
    private val TAG: String = "JoinActivity"


    // 갤러리에서 선택된 , 파일의 위치(로컬)
    lateinit var filePath : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //갤러리 접근권한 질문
        PermissionManager.checkPermission(this@JoinActivity)


        //갤러리에서 사진 선택 후
        //가져와서 처리하는 후처리 함수
        val requestLauncher = registerForActivityResult(
            // 갤러리에서, 사진을 선택해서 가져왔을 때, 수행할 함수.
            ActivityResultContracts.StartActivityForResult()
        ) {
            // it 이라는 곳에 사진 이미지가 있음.
            if(it.resultCode === android.app.Activity.RESULT_OK) {
                // 이미지 불러오는 라이브러리 glide 사용하기, 코루틴이 적용이되어서, 매우 빠름.
                // with(this) this 현재 액티비티 가리킴. 대신해서.
                // 1) applicationContext
                // 2) getApplicationContext()
                Glide
                    .with(getApplicationContext())
                    // 사진을 읽기.
                    .load(it.data?.data)
                    // 크기 지정 , 가로,세로
                    .apply(RequestOptions().override(250,200))
                    // 선택된 사진 크기 자동 조정
                    .centerCrop()
                    // 결과 뷰에 사진 넣기.
                    .into(binding.resultUserImage)

                // filePath, 갤러리에서 불러온 이미지 파일 정보 가져오기.
                // 커서에 이미지 파일이름이 등록이 되어 있음.
                val cursor = contentResolver.query(it.data?.data as Uri,
                    arrayOf<String>(MediaStore.Images.Media.DATA),null,
                    null,null);

                cursor?.moveToFirst().let {
                    filePath = cursor?.getString(0) as String
                }
                Log.d(TAG,"filePath : ${filePath}")
                Toast.makeText(this,"filePath : ${filePath}", Toast.LENGTH_LONG).show()

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


        //회원가입
        binding.buttonInsert.setOnClickListener {

            // 스토리지 접근 도구 ,인스턴스
            val storage = MyApplication.storage
            // 스토리지에 저장할 인스턴스
            val storageRef = storage.reference

            // 파일명 생성 : userid+현재시간
            val uuid = binding.userId.text.toString()+Date()+System.currentTimeMillis();

            // 이미지 저장될 위치 및 파일명(파이어베이스)
            val imgRef = storageRef.child("users_img/${binding.userId.text.toString()}.jpg")

            //이미지 url
            val imgStorageUrl = "https://firebasestorage.googleapis.com/v0/b/findmymatzip.appspot.com/o/users_img%2F${binding.userId.text.toString()}.jpg?alt=media"

            //회원가입 요청할 user객체 구성
            var usersFormDto = UsersFormDto(
                userid = binding.userId.text.toString(),
                user_pwd = binding.userPwd.text.toString(),
                username = binding.userName.text.toString(),
                user_address = binding.searchAddress.text.toString() + binding.userAddressDetail.text,
                user_role = "ADMIN",
                userphone = binding.userPhone.text.toString(),
                user_image = imgStorageUrl,
                gender = getValue(binding.testRadioGroup),
            )


            val userService = (applicationContext as MyApplication).userService
            val newUsers = userService.newUsers(usersFormDto)

            newUsers.enqueue(object: Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                    Log.d(TAG, "Request URL: ${call.request().url()}")
                    Log.d(TAG, "Request Body: ${call.request().body()}")
                    Log.d(TAG, "Response Code: ${response.code()}")
                    if(response.isSuccessful) {

                        Log.d(TAG, "성공(Users) :  ${usersFormDto}")
                        Log.d(TAG, "성공(newUsers_body) :  ${response.body().toString()}")


                        // 로컬에서 파일 불러오기.
                        val file = Uri.fromFile(File(filePath))

                        // 파이어베이스 스토리지에 업로드하는 함수.
                        imgRef.putFile(file)
                            // 업로드 후, 수행할 콜백 함수 정의. 실패했을 경우 콜백함수 정의
                            .addOnCompleteListener{
                                Toast.makeText(this@JoinActivity,"스토리지 업로드 완료",Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@JoinActivity,"스토리지 업로드 실패",Toast.LENGTH_SHORT).show()
                            }


                        val intent = Intent(this@JoinActivity, LoginActivity::class.java)

                        startActivity(intent)
                    }else {
                        // Log the raw response for debugging purposes
                        Log.d(TAG, "서버 응답 실패: ${response.code()}")
                        try {
                            val errorBody = response.errorBody()?.string()
                            Log.d(TAG, "Error Body: $errorBody")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d(TAG, "실패 ${t.message}")
                    call.cancel()
                }


            })
        }//회원가입

        //주소 검색후 결과값 받아오는 후처리 함수
        val getSearchResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val data = result.data!!.getStringExtra("data")
                    binding.searchAddress.setText(data)
                }
            }
        }


        //주소 검색
        binding.searchAddress.setOnClickListener{
            //주소 검색 화면으로 이동
            val intent = Intent(this@JoinActivity, SearchAddressActivity::class.java)
            getSearchResult.launch(intent)
        }


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