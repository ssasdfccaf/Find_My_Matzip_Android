package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.databinding.FragmentCommentBinding
import com.example.find_my_matzip.model.BoardDtlDto
import com.example.find_my_matzip.model.CommentDto
import com.example.find_my_matzip.model.Content
import com.example.find_my_matzip.model.ProfileDto
import com.example.find_my_matzip.navTab.adapter.CommentAdapter2
import com.example.find_my_matzip.utiles.SharedPreferencesManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentCommentBinding
    lateinit var adapter: CommentAdapter2
    private var loginUserId: String = ""
    var user_image: String? = null
    var commentContents: EditText? = null
    var saveBtn: Button? = null
    private val TAG: String = "CommentFragment"


    companion object {
        fun newInstance(boardId: String?): CommentFragment {
            Log.d("syy", "게시판 아이디 잘받았나욥  . boardId: $boardId")
            val fragment = CommentFragment()
            val args = Bundle()
            args.putString("boardId", boardId)
            fragment.arguments = args
            return fragment
        }

        // 유저 아이디를 전달하는 newInstance2 메서드
        fun newInstance2(userId: String): CommentFragment {
            val fragment = CommentFragment()
            val args = Bundle()
            args.putString("userId", userId)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("SdoLifeCycle", "boardDtlFragment onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentBinding.inflate(layoutInflater, container, false)
        loginUserId = SharedPreferencesManager.getString("id", "")
        commentContents = binding.commentContents
        saveBtn = binding.saveBtn
        val boardIdString = arguments?.getString("boardId")
        val boardId = if (!boardIdString.isNullOrBlank()) {
            boardIdString.toLong()
        } else {
            0L
        }


        // 로그인한 사용자의 아이디를 가져와서 해당 사용자의 프로필 정보를 서버에서 조회
        val userId = SharedPreferencesManager.getString("id", "")
        val userService = (context?.applicationContext as MyApplication).userService

        val profileList = userService.getProfile(userId, 5)

        Log.d("syy", "profileList.enqueue 호출전 : ")

        profileList.enqueue(object : Callback<ProfileDto> {
            override fun onResponse(call: Call<ProfileDto>, response: Response<ProfileDto>) {
                Log.d("syy", "도착 확인=================================================== ")
                val profileDto = response.body()

                Log.d("syy", "로그인 된 유저 확인 userId : $userId")

                if (profileDto != null) {
                    // 사용자 이미지 URL이 있다면 Glide로 이미지 로드

                    // 사용자 이미지 URL이 있다면 Glide로 이미지 로드
                    val user_image = profileDto.loginUserDto?.user_image
                    if (!user_image.isNullOrBlank()) {
                        Glide.with(requireContext())
                            .load(user_image)
                            .override(900, 900)
                            .into(binding.userImage)
                    }


                } else {
                    Log.e("WriteReviewFragment", "유저 정보를 받아오지 못했습니다.")
                }
            }
            // 통신 실패 시 로그 출력
            override fun onFailure(call: Call<ProfileDto>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                Log.e("syy", " 통신 실패")
            }
        })


        val boardService = (context?.applicationContext as MyApplication).boardService
        val boardDtl = boardIdString?.let { boardService.getBoardDtl(it) }
        Log.d("syy", "잘받았나욥 레트로핏 . boardDtl: $boardDtl")



        boardDtl?.enqueue(object : Callback<BoardDtlDto> {
            override fun onResponse(call: Call<BoardDtlDto>, response: Response<BoardDtlDto>) {
                Log.d("syy", "서버 응답 코드: ${response.code()}")
                if (response.isSuccessful) {
                    val boardDto = response.body()
                    // 이제 boardDto에서 실제 응답 데이터를 확인할 수 있습니다.

                    Log.d("syy", "데이터 도착 확인.")
                    Log.d("syy", "게시판 아이디디디디 : ${boardDto?.board?.id}")


                    // 댓글 목록 데이터 초기화
                    val commentList: List<Content> = boardDto?.commentsPage?.content.orEmpty()

                    // 어댑터 초기화
                    adapter = CommentAdapter2(this@CommentFragment, commentList)
//                    adapter2 = ChildrenAdapter(this@CommentFragment, commentList)

                    val layoutManager = LinearLayoutManager(requireContext().applicationContext)
                    binding.commentRecyclerView.layoutManager = layoutManager
                    // 리사이클러뷰에 어댑터 설정
                    binding.commentRecyclerView.adapter = adapter



                    binding.saveBtn.setOnClickListener {
                        val boardIdString = arguments?.getString("boardId")
                        val boardId = if (!boardIdString.isNullOrBlank()) {
                            boardIdString.toLong()
                        } else {
                            0L
                        }
                        val boardService =
                            (context?.applicationContext as MyApplication).boardService
                        val boardDtl = boardIdString?.let { boardService.getBoardDtl(it) }
                        val commentWriter = SharedPreferencesManager.getString("id", "")
                        val commentContents = binding.commentContents.text.toString()

                        val commentDto = CommentDto(
                            content = listOf(
                                Content(
                                    commentId = 0,
                                    commentWriter = commentWriter,
                                    commentContents = commentContents,
                                    boardId = boardId,
                                    user_image = user_image ?: "",
                                    depth = 0,
                                    parentId = 0,
                                    commentCreatedTime = "",
                                )
                            ),
                            children = emptyList(),
                            userImage = user_image
                        )

                        val commentService =
                            (context?.applicationContext as MyApplication).commentService
                        val commentList = commentService.save(commentDto)
                        Log.d("syy ", "boardId확인2:  $boardId.")
                        Log.d("syy", " boardDtl확인2 :$boardDtl.")
                        Log.d("syy", " commentWriter확인2 :$commentWriter.")
                        Log.d("syy", " commentContents확인2 :$commentContents.")
                        Log.d("syy", " user_image :$user_image.")
                        Log.d("syy", "user_image inside CommentDto: ${commentDto.userImage}")

                        commentList.enqueue(object : Callback<CommentDto?> {

                            override fun onResponse(
                                call: Call<CommentDto?>,
                                response: Response<CommentDto?>
                            ) {


                                Log.d("CommentFragment", "onResponse: ${response.code()}")

                                if (response.isSuccessful) {
                                    val savedComment = response.body()
                                    Log.e("syy", "savedComment: ${savedComment}")
                                    Toast.makeText(
                                        requireContext().applicationContext,
                                        "댓글 작성 성공",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // TODO: 댓글 작성 성공 시의 추가 처리
                                } else {
                                    val errorBody = response?.errorBody()?.string()
                                    // TODO: 댓글 작성 실패 시의 처리
                                    Toast.makeText(
                                        requireContext().applicationContext,
                                        "댓글 작성 실패. 에러 메시지: $errorBody",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Toast.makeText(
                                        requireContext().applicationContext,
                                        "댓글 작성 실패",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.e("syy", "댓글 작성 실패: ${response.errorBody()}")
                                    Log.d("syy ", "댓글 작성 실패. 에러 메시지:  $errorBody.")

                                }
                            }

                            override fun onFailure(call: Call<CommentDto?>, t: Throwable) {
                                // TODO: 네트워크 오류 또는 예외 발생 시의 처리
                                Toast.makeText(
                                    requireContext().applicationContext,
                                    "댓글 작성 실패",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("syy", "댓글 작성 실패: ${t.message}", t)
                            }
                        })
                    }
                }
            }

            override fun onFailure(call: Call<BoardDtlDto>, t: Throwable) {
                // TODO: 네트워크 오류 또는 예외 발생 시의 처리
                Toast.makeText(
                    requireContext().applicationContext,
                    "댓글 작성 실패",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("syy", "댓글 작성 실패: ${t.message}", t)
            }
        })
        return binding.root
    }
}
