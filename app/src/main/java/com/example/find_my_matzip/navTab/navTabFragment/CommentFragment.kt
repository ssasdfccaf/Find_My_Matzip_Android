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
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentCommentBinding
import com.example.find_my_matzip.model.BoardDtlDto
import com.example.find_my_matzip.model.CommentDto
import com.example.find_my_matzip.model.ProfileDto
import com.example.find_my_matzip.navTab.adapter.CommentAdapter2
import com.example.find_my_matzip.utiles.SharedPreferencesManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentFragment : BottomSheetDialogFragment() {
    private var boardId: Long = 0L
    private var commentId: Long = 0L
    private var parentId: Long = 0L
    lateinit var binding: FragmentCommentBinding
    lateinit var adapter: CommentAdapter2
    private var loginUserId: String = ""
    var commentContents: EditText? = null
    var saveBtn: Button? = null
    private val TAG: String = "CommentFragment"

    companion object {

        fun newInstance(boardId: String?): CommentFragment {
            Log.d("syy", "게시판 아이디 잘받았나욥  . boardId: $boardId")
            val fragment = CommentFragment()
            val args = Bundle()
            if (boardId != null) {
                args.putString("boardId", boardId)
            } else {
                // boardId가 null인 경우에 대한 처리를 추가할 수 있습니다.
                // 예: 기본값을 설정하거나, 사용자에게 알림을 표시하는 등의 작업
                Log.e("CommentFragment", "boardId is null!")
            }
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
        boardId = arguments?.getLong("boardId") ?: 0L


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
        val boardDtl = arguments?.getString("boardId")?.let { boardService.getBoardDtl(it) }
        Log.d("syy", "잘받았나욥 레트로핏 . boardDtl: $boardDtl")



        boardDtl?.enqueue(object : Callback<BoardDtlDto> {
            override fun onResponse(call: Call<BoardDtlDto>, response: Response<BoardDtlDto>) {
                Log.d("syy", "서버 응답 코드(BoardDtlDto아래): ${response.code()}")
                if (response.isSuccessful) {
                    val boardDto = response.body()
                    Log.d("syy", "데이터 도착 확인.")
                    Log.d("syy", "게시판 아이디디디디 : ${boardDto?.board?.id}")

                    Log.d("syy", "게시판 아이디디디디 : ${boardId}")
                    // 댓글 목록 데이터 초기화

// 수정된 코드
                    val commentList: List<CommentDto> = boardDto?.commentsPage?.content.orEmpty()
                    val layoutManager = LinearLayoutManager(requireContext().applicationContext)
                    binding.commentRecyclerView.layoutManager = layoutManager
                    // CommentFragment에서 CommentAdapter2 초기화 부분
                    // 어댑터를 생성할 때 OnReplyClickListener를 전달

                    val adapter = CommentAdapter2(this@CommentFragment, commentList)

                    adapter.onReplyClick = { commentDto ->
                        Toast.makeText(requireContext(), "댓글을 클릭했습니다: ${commentDto}", Toast.LENGTH_SHORT).show()
                    }

                    // 리사이클러뷰에 어댑터 설정
                    binding.commentRecyclerView.adapter = adapter


                    binding.saveBtn.setOnClickListener {

                if (boardDto != null) {
                    val boardId = boardDto.board?.id!!
                    val commentWriter = SharedPreferencesManager.getString("id", "")
                    val commentContents = binding.commentContents.text.toString()
                    val commentService = (context?.applicationContext as MyApplication).commentService
                    // 안드로이드에서 CommentDto를 생성할 때 parentId를 null로 설정
                    // 댓글 등록에 필요한 정보를 담은 CommentDto 객체 생성
                    val commentDto = CommentDto(
                        commentWriter = commentWriter, // 작성자 아이디 또는 다른 정보
                        commentContents = commentContents, // 댓글 내용
                        boardId = boardId, // 게시글 ID
                        // parentId = parentId, // parentId는 대댓글을 작성할 때 사용
                        depth = 0, // 댓글 깊이, 일반 댓글은 0
                        commentCreatedTime = "", // 댓글 생성 시간 또는 다른 정보
                    )

                    // userImage = userImage, // CommentDto 전체에 대한 사용자 이미지 (선택적)
                    // children = null // 대댓글 목록 (대댓글을 작성할 때만 사용)

                    val commentList = commentService.save(commentDto)

                    commentList.enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {


                            Log.d("CommentFragment", "Unit아래응답코드: ${response.code()}")
                            if (response.isSuccessful) {
                                val save = response.body()
                                Log.e("syy", "savedComment: ${save}")
                                Toast.makeText(
                                    requireContext().applicationContext,
                                    "댓글작성되었습니다요^ㅡㅡ^",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // 현재의 프래그먼트를 제거
                                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                transaction.remove(this@CommentFragment).commit()

                                // 새로운 인스턴스를 생성하여 추가
                                val newFragment = CommentFragment.newInstance(boardId.toString())
                                val newTransaction = requireActivity().supportFragmentManager.beginTransaction()
                                newTransaction.add(R.id.fragmentContainer, newFragment)
                                newTransaction.addToBackStack(null)
                                newTransaction.commit()
                                // TODO: 댓글 작성 성공 시의 추가 처리
                            } else {
                                val errorBody = response?.errorBody()?.string()
                                Log.e("syy", "댓글 작성 실패. 에러 메시지: $errorBody")
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
                                Log.e("syy", "댓글 작성 실패: ${response.errorBody().toString()}")
                                Log.d("syy ", "댓글 작성 실패. 에러 메시지:  $errorBody.")

                            }
                        }

                        override fun onFailure(call: Call<Unit?>, t: Throwable) {
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
