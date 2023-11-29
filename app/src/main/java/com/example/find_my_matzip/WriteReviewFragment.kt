package com.example.find_my_matzip

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.find_my_matzip.databinding.FragmentWriteReviewBinding
import com.example.find_my_matzip.model.BoardFormDto
import com.example.find_my_matzip.model.BoardImgDto
import com.example.find_my_matzip.model.ProfileDto
import com.example.find_my_matzip.model.UsersFormDto
import com.example.find_my_matzip.navTab.navTabFragment.HomeFragment
import com.example.find_my_matzip.navTab.navTabFragment.RestaurantDtlFragment
import com.example.find_my_matzip.retrofit.BoardService
import com.example.find_my_matzip.utiles.SharedPreferencesManager
import com.example.find_my_matzip.utils.LoadingDialog
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class WriteReviewFragment : Fragment() {
    lateinit var binding : FragmentWriteReviewBinding

    private val TAG: String = "WriteReviewFragment"

    // 갤러리에서 선택된 , 파일의 위치(로컬)
    lateinit var filePath : String

    private var imgCounter = 1

    lateinit var selectImgBtn1: Button
    lateinit var selectImgBtn2: Button
    lateinit var selectImgBtn3: Button
    lateinit var selectImgBtn4: Button
    lateinit var addImg: Button
    lateinit var deleteImg2: Button
    lateinit var deleteImg3: Button
    lateinit var deleteImg4: Button
    lateinit var reviewImg1: ImageView
    lateinit var reviewImg2: ImageView
    lateinit var reviewImg3: ImageView
    lateinit var reviewImg4: ImageView
    lateinit var imgUploadLayout2: LinearLayout
    lateinit var imgUploadLayout3: LinearLayout
    lateinit var imgUploadLayout4: LinearLayout
    lateinit var boardImgDtoList: MutableList<BoardImgDto>

    companion object {
        fun newInstance(resId: String): WriteReviewFragment {
            val fragment = WriteReviewFragment()
            val args = Bundle()
            args.putString("resId", resId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteReviewBinding.inflate(layoutInflater,container,false)
        // 이전 프래그먼트에서 전달된 resId 가져오기
        val resId = arguments?.getString("resId")

        // 로그인한 사용자의 아이디를 가져와서 해당 사용자의 프로필 정보를 서버에서 조회
        val userId = SharedPreferencesManager.getString("id","")
        val userService = (context?.applicationContext as MyApplication).userService
        val profileList = userService.getProfile(userId,5)

        Log.d("MyPageFragment", "profileList.enqueue 호출전 : ")

        profileList.enqueue(object : Callback<ProfileDto> {
            override fun onResponse(call: Call<ProfileDto>, response: Response<ProfileDto>) {
                Log.d("WriteReviewFragment", "도착 확인=================================================== ")
                val profileDto = response.body()
                Log.d("WriteReviewFragment", "로그인 된 유저 확인 userId : $userId")
                if (profileDto != null) {
                    // 유저정보 확인하기
                    binding.userId.text = userId
                }
                else {
                    Log.e("WriteReviewFragment", "유저 정보를 받아오지 못했습니다.")
                }
            }


            // 통신 실패 시 로그 출력
            override fun onFailure(call: Call<ProfileDto>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                Log.e("MyPageFragment", " 통신 실패")
            }
        })

        val loadingDialog = LoadingDialog(requireContext())

        boardImgDtoList = mutableListOf<BoardImgDto>()

        selectImgBtn1 = binding.selectImgBtn1
        selectImgBtn2 = binding.selectImgBtn2
        selectImgBtn3 = binding.selectImgBtn3
        selectImgBtn4 = binding.selectImgBtn4

        addImg = binding.addImg

        deleteImg2 = binding.deleteImg2
        deleteImg3 = binding.deleteImg3
        deleteImg4 = binding.deleteImg4

        reviewImg1 = binding.reviewImg1
        reviewImg2 = binding.reviewImg2
        reviewImg3 = binding.reviewImg3
        reviewImg4 = binding.reviewImg4

        imgUploadLayout2 = binding.imgUploadLayout2
        imgUploadLayout3 = binding.imgUploadLayout3
        imgUploadLayout4 = binding.imgUploadLayout4



        selectImgBtn1.setOnClickListener {
            // 갤러리 열기 인텐트 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startGalleryForImage(1)
        }
        selectImgBtn2.setOnClickListener {
            // 갤러리 열기 인텐트 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startGalleryForImage(2)
        }
        selectImgBtn3.setOnClickListener {
            // 갤러리 열기 인텐트 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startGalleryForImage(3)
        }
        selectImgBtn4.setOnClickListener {
            // 갤러리 열기 인텐트 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startGalleryForImage(4)
        }

        addImg.setOnClickListener {
            if (imgCounter < 4) {
                when (imgCounter) {
                    1 -> imgUploadLayout2.visibility = View.VISIBLE
                    2 -> imgUploadLayout3.visibility = View.VISIBLE
                    3 -> imgUploadLayout4.visibility = View.VISIBLE
                }
                imgCounter++
                Log.d("kkt","imgCounter의 값 : $imgCounter")
            } else {
                Log.d("kkt","이미지는 총4개 까지만 가능")
            }
        }

        deleteImg2.setOnClickListener {
            imgUploadLayout2.visibility = View.GONE
            removeImage(reviewImg2)
            imgCounter--
            updateLayoutVisibility()
        }
        deleteImg3.setOnClickListener {
            imgUploadLayout3.visibility = View.GONE
            removeImage(reviewImg3)
            imgCounter--
            updateLayoutVisibility()
        }
        deleteImg4.setOnClickListener {
            imgUploadLayout4.visibility = View.GONE
            removeImage(reviewImg4)
            imgCounter--
            updateLayoutVisibility()
        }

        fun uploadImageToFirebaseStorage(imageUri: Uri, fileName: String) {
            val storage = MyApplication.storage
            val storageRef = storage.reference
            val imgRef = storageRef.child("board_img_img/$fileName.jpg")

            imgRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    // 업로드 성공 시 처리
                    val downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl // 이미지 다운로드 URL
                    Log.d(TAG, "이미지 업로드 성공. 다운로드 URL: $downloadUrl")
                    // 성공적으로 업로드되었을 때의 추가 작업 수행

                    //이미지 url
                    val imgStorageUrl =
                        "https://firebasestorage.googleapis.com/v0/b/findmymatzip.appspot.com/o/board_img_img%2F${fileName}.jpg?alt=media"
                    val uploadedImg = BoardImgDto(
                        id = 0, // 이미지 ID는 서버에서 생성되므로 0으로 설정하거나 다른 값으로 임시 설정해주세요.
                        imgName = fileName, // 이미지 파일명
                        oriImgName = fileName, // 원본 이미지명
                        imgUrl = imgStorageUrl, // 이미지 URL
                        repImgYn = if (boardImgDtoList.isEmpty()) "Y" else "N" // 첫 번째 이미지인 경우 'Y', 그 외에는 'N'으로 설정
                    )
                    boardImgDtoList.add(uploadedImg) // 이미지 정보를 리스트에 추가
                }
                .addOnFailureListener { exception ->
                    // 업로드 실패 시 처리
                    Log.e(TAG, "이미지 업로드 실패: ${exception.message}")
                    // 실패했을 때의 추가 작업 수행
                }
        }
        fun uploadImagesToFirebaseStorage(fileName: String) {
            if (reviewImg1.drawable != null) {
                val imageUri1: Uri? = (reviewImg1.drawable as? BitmapDrawable)?.bitmap?.let { bitmap ->
                    // 이미지 파일을 캐시 디렉토리에 저장하여 Uri를 얻는 방법
                    val imagesFolder = File(requireContext().cacheDir, "uploadImg")
                    if (!imagesFolder.exists()) imagesFolder.mkdirs()

                    val file = File(imagesFolder, "$fileName-1.jpg")
                    val fileOutputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()

                    FileProvider.getUriForFile(requireContext(),"com.example.find_my_matzip", file)                }
                val fileName1 = "$fileName-1" // 이미지 파일명
                if (imageUri1 != null) {
                    uploadImageToFirebaseStorage(imageUri1, fileName1)
                }
            }

            if (reviewImg2.drawable != null) {
                val imageUri2: Uri? = (reviewImg2.drawable as? BitmapDrawable)?.bitmap?.let { bitmap ->
                    // 이미지 파일을 캐시 디렉토리에 저장하여 Uri를 얻는 방법
                    val imagesFolder = File(requireContext().cacheDir, "uploadImg")
                    if (!imagesFolder.exists()) imagesFolder.mkdirs()

                    val file = File(imagesFolder, "$fileName-2.jpg")
                    val fileOutputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()

//                    FileProvider.getUriForFile(requireContext(), requireContext().applicationContext.packageName, file)
                    FileProvider.getUriForFile(requireContext(),"com.example.find_my_matzip", file)
                }
                val fileName2 = "$fileName-2" // 이미지 파일명
                if (imageUri2 != null) {
                    uploadImageToFirebaseStorage(imageUri2, fileName2)
                }
            }

            if (reviewImg3.drawable != null) {
                val imageUri3: Uri? = (reviewImg3.drawable as? BitmapDrawable)?.bitmap?.let { bitmap ->
                    // 이미지 파일을 캐시 디렉토리에 저장하여 Uri를 얻는 방법
                    val imagesFolder = File(requireContext().cacheDir, "uploadImg")
                    if (!imagesFolder.exists()) imagesFolder.mkdirs()

                    val file = File(imagesFolder, "$fileName-3.jpg")
                    val fileOutputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()

                    FileProvider.getUriForFile(requireContext(),"com.example.find_my_matzip", file)                }
                val fileName3 = "$fileName-3" // 이미지 파일명
                if (imageUri3 != null) {
                    uploadImageToFirebaseStorage(imageUri3, fileName3)
                }
            }

            if (reviewImg4.drawable != null) {
                val imageUri4: Uri? = (reviewImg4.drawable as? BitmapDrawable)?.bitmap?.let { bitmap ->
                    // 이미지 파일을 캐시 디렉토리에 저장하여 Uri를 얻는 방법
                    val imagesFolder = File(requireContext().cacheDir, "uploadImg")
                    if (!imagesFolder.exists()) imagesFolder.mkdirs()

                    val file = File(imagesFolder, "$fileName-4.jpg")
                    val fileOutputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()

                    FileProvider.getUriForFile(requireContext(),"com.example.find_my_matzip", file)                }
                val fileName4 = "$fileName-4" // 이미지 파일명
                if (imageUri4 != null) {
                    uploadImageToFirebaseStorage(imageUri4, fileName4)
                }
            }
        }



        //게시글 작성버튼
        binding.submitBtn.setOnClickListener {
            //문자열을 정수로 변환. 평점란에 숫자가 아닌 다른것이 들어간다면 3을 기본값으로 설정
            val scoreText = binding.boardScore.text.toString()
            val score = scoreText.toIntOrNull()?:3

            if (reviewImg1.drawable == null) {
                Toast.makeText(requireContext(), "이미지는 최소 1장 업로드가 필요합니다", Toast.LENGTH_SHORT).show()
                //score에 1~5사이의 입력값이 정상적으로 들어 왔을 경우
            } else if(score in 1..5){
                //로딩창 띄우기
                loadingDialog.show()

                // 스토리지 접근 도구 ,인스턴스
                val storage = MyApplication.storage
                // 스토리지에 저장할 인스턴스
                val storageRef = storage.reference

                // 파일명 생성 : uuid+현재시간
                val currentTime = System.currentTimeMillis().toString()
                val uuid = UUID.randomUUID().toString()
                val fileName = "$uuid-$currentTime"


//            //이미지 url
//            val imgStorageUrl =
//                "https://firebasestorage.googleapis.com/v0/b/findmymatzip.appspot.com/o/board_img_img%2F${fileName}.jpg?alt=media"
//
//                val uploadedImg = BoardImgDto(
//                    id = 0, // 이미지 ID는 서버에서 생성되므로 0으로 설정하거나 다른 값으로 임시 설정해주세요.
//                    imgName = fileName, // 이미지 파일명
//                    oriImgName = fileName, // 원본 이미지명
//                    imgUrl = imgStorageUrl, // 이미지 URL
//                    repImgYn = if (boardImgDtoList.isEmpty()) "Y" else "N" // 첫 번째 이미지인 경우 'Y', 그 외에는 'N'으로 설정
//                )
//                boardImgDtoList.add(uploadedImg) // 이미지 정보를 리스트에 추가

                Log.d("kkt", "====================================================현재 유저 아이디 : userId : $userId")

                val boardFormDto = BoardFormDto(
                    userId = userId,
                    boardViewStatus = "VIEW",
                    boardTitle = binding.boardTitle.text.toString(),
                    content = binding.boardContent.text.toString(),
                    // 정수 변화는 검색.
                    score = score,
                    boardImgDtoList
                )

//                val resId = "123"

                val boardService = (context?.applicationContext as MyApplication).boardService

                val call = resId?.let { it1 -> boardService.createBoard2(it1, boardFormDto) }
                call?.enqueue(object : Callback<Unit> {
                    override fun onResponse(
                        call: Call<Unit>,
                        response: Response<Unit>
                    ) {

                        Log.d(TAG, "Request URL: ${call.request().url()}")
                        Log.d(TAG, "Request Body: ${call.request().body()}")
                        Log.d(TAG, "Response Code: ${response.code()}")
                        if (response.isSuccessful) {
                            Log.d(TAG, "성공(Board) :  ${boardFormDto}")
                            Log.d(TAG, "성공(createBoard_body) :  ${response.body().toString()}")
                            //파이어베이스업로드
                            uploadImagesToFirebaseStorage(fileName)
                            Toast.makeText(requireContext(),"스토리지/DB 업로드 완료", Toast.LENGTH_SHORT).show()

                            val newFragment = HomeFragment() // 전환할 새로운 프래그먼트 인스턴스 생성

                            // 프래그먼트 트랜잭션 시작
                            requireActivity().supportFragmentManager.beginTransaction().apply {
                                replace(R.id.homeFragment, newFragment) // R.id.container에는 새 프래그먼트가 표시될 컨테이너의 ID를 지정하세요.
                                addToBackStack(null) // 이전 상태로 돌아갈 수 있도록 back stack에 추가
                                commit()
                            }
                            //로딩창 지우기
                            loadingDialog.dismiss()

                        } else {
                            Log.d(TAG, "서버 응답 실패: ${response.code()}")
                            //로딩창 지우기
                            loadingDialog.dismiss()
                            try {
                                val errorBody = response.errorBody()?.string()

                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("message", "Unknown Error")

                                Log.d(TAG, "Error Body: $errorBody")
                                Log.d(TAG, "Error errorMessage: ${errorMessage}")

                                if (errorMessage.equals("게시글등록실패")) {
                                    Toast.makeText(requireContext(), "게시글등록실패.", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Log.d(TAG, "실패 ${t.message}")
                        //로딩창 지우기
                        loadingDialog.dismiss()
                        call.cancel()
                    }

                })
                //score에 비정상 입력값이 들어왔을경우
            }else{
                Toast.makeText(requireContext(), "평점은 1~5 사이의 숫자만 입력할 수 있습니다", Toast.LENGTH_SHORT).show()
            }
        }//게시글작성버튼


        return binding.root
    }

    // 갤러리 열기 및 이미지 선택 요청
    private fun startGalleryForImage(imageIndex: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, imageIndex)
    }

    fun updateLayoutVisibility() {
        when (imgCounter) {
            1 -> {
                imgUploadLayout2.visibility = View.GONE
                imgUploadLayout3.visibility = View.GONE
                imgUploadLayout4.visibility = View.GONE
            }
            2 -> {
                imgUploadLayout2.visibility = View.VISIBLE
                imgUploadLayout3.visibility = View.GONE
                imgUploadLayout4.visibility = View.GONE
            }
            3 -> {
                imgUploadLayout2.visibility = View.VISIBLE
                imgUploadLayout3.visibility = View.VISIBLE
                imgUploadLayout4.visibility = View.GONE
            }
            4 -> {
                imgUploadLayout2.visibility = View.VISIBLE
                imgUploadLayout3.visibility = View.VISIBLE
                imgUploadLayout4.visibility = View.VISIBLE
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            when (requestCode) {
                1 -> loadImage(imageUri, reviewImg1)
                2 -> loadImage(imageUri, reviewImg2)
                3 -> loadImage(imageUri, reviewImg3)
                4 -> loadImage(imageUri, reviewImg4)
            }
        }
    }
    private fun loadImage(imageUri: Uri?, imageView: ImageView) {
        Glide.with(this)
            .load(imageUri)
            .apply(RequestOptions().override(900, 900))
            .centerCrop()
            .into(imageView)
        imageView.visibility = View.VISIBLE

        imageUri?.let { uri ->
            val cursor = requireContext().contentResolver.query(
                uri,
                arrayOf(MediaStore.Images.Media.DATA),
                null,
                null,
                null
            )

            cursor?.use { c ->
                val columnIndex = c.getColumnIndex(MediaStore.Images.Media.DATA)
                if (columnIndex != -1 && c.moveToFirst()) {
                    filePath = c.getString(columnIndex)
                    Log.d(TAG, "갤러리 filePath : $filePath")
                } else {
                    Log.e(TAG, "컬럼 인덱스를 찾을 수 없거나 데이터를 가져올 수 없습니다.")
                }
            }
            cursor?.close()
        }
    }

    private fun removeImage(imageView: ImageView) {
        Glide.with(this)
            .clear(imageView)
        imageView.setImageDrawable(null)
        imageView.visibility = View.GONE
    }




}//프래그먼트의 끝


//라디오 설정할때 참고할것
//fun getValue(v: View?): String? {
//    val male = binding.radio1
//    val female = binding.radio2
//    var pickValue: String? = null
//    if (male.isChecked) {
//        pickValue = male.text.toString()
//    } else if (female.isChecked) {
//        pickValue = female.text.toString()
//    }
//    return pickValue
//}