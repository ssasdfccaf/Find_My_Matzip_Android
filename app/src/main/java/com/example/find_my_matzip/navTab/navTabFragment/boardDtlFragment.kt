package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentBoardDtlBinding
import com.example.find_my_matzip.model.BoardDtlDto
import com.example.find_my_matzip.navTab.adapter.BoardRecyclerAdapter
import com.example.find_my_matzip.navTab.adapter.ProfileAdapter
import com.example.find_my_matzip.retrofit.BoardService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class boardDtlFragment : Fragment() {
    lateinit var binding: FragmentBoardDtlBinding

    companion object {
        fun newInstance(boardId: String): boardDtlFragment {
            val fragment = boardDtlFragment()
            val args = Bundle()
            args.putString("boardId", boardId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBoardDtlBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardDtlBinding.inflate(layoutInflater, container, false)

        val boardService = (context?.applicationContext as MyApplication).boardService
        val boardDtl = arguments?.getString("boardId")?.let { boardService.getBoardDtl(it) }
//        val boardId = arguments?.getString("boardId")
        Log.d("boardDtlFragment", "boardDtl.enqueue 호출전 $boardDtl")

//        val boardDetail = boardService.getBoardDtl(boardId)

        boardDtl?.enqueue(object : Callback<BoardDtlDto> {
            override fun onResponse(call: Call<BoardDtlDto>, response: Response<BoardDtlDto>) {
                Log.d("kkt","데이터 도착 확인.")
                val boardDto = response.body()
                Log.d("kkt","데이터 도착 확인1. : BoardDtlDto $boardDto")
                Log.d("kkt","데이터 도착 확인2. : BoardDtlDto.board ${boardDto?.board}")
                Log.d("kkt","데이터 도착 확인3. : BoardDtlDto.restaurant ${boardDto?.restaurant}")
                Log.d("kkt","데이터 도착 확인4. : BoardDtlDto.users ${boardDto?.users}")
//                    Log.d("kkt","데이터 도착 확인5. : BoardDtlDto ${boardDto?.score}")
//                    Log.d("kkt","데이터 도착 확인6. : BoardDtlDto ${boardDto?.resId}")

                binding.boardDtlTitle.text = boardDto?.board?.boardTitle.toString()
                binding.boardDtlContent.text = boardDto?.board?.content.toString()
                binding.boardScore.text = boardDto?.board?.score.toString()
                binding.userName.text = boardDto?.users?.username.toString()
//                binding.userProfileImg 파이어베이스 설정 필요
                binding.resName.text = boardDto?.restaurant?.res_name.toString()
                binding.resAddress.text = boardDto?.restaurant?.res_address.toString()

//                binding.resScore.text = boardDto?.restaurant?.avgScore.toString()
                val formattedScore = String.format("%.1f", boardDto?.restaurant?.avgScore ?: 0.0)
                binding.resScore.text = formattedScore

                Log.d("kkt","바인딩완료")

                Glide.with(requireContext())
                    .load(boardDto?.board?.boardImgDtoList?.get(0)?.imgUrl)
                    .override(900, 900)
                    .into(binding.boardThumbnail)

                Log.d("MyPageFragment", "도착 확인2: res_thumbnail ${boardDto?.board?.boardImgDtoList?.get(0)?.imgUrl}")
//                binding.resThumbnail.
//                ProfileAdapter(this@MyPageFragment, listOf(profileDto.pageUserDto))
//
//                binding.boardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//                BoardRecyclerAdapter(this@MyPageFragment, profileDto.boards.content)
//                binding.boardRecyclerView.adapter = boardAdapter


                // toResDtl 클릭 이벤트 핸들러
                fun navigateToResDetail(resId: String?) {
                    if (resId.isNullOrEmpty()) {
                        Log.d("kkt", "resId is null or empty")
                        return
                    }

                    val fragment = RestaurantDtlFragment.newInstance(resId)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit()
                }

                binding.toResDtl.setOnClickListener {
                    Log.d("kkt", "식당가기 클릭됨")
                    Log.d("kkt", "resId: ${boardDto?.restaurant?.res_id}")
                    val resId = boardDto?.restaurant?.res_id
                    navigateToResDetail(resId)
                }
                // toResDtl 클릭 이벤트 핸들러


            }

            override fun onFailure(call: Call<BoardDtlDto>, t: Throwable) {
                // Handle failure
            }
        })

        return binding.root
    }

    private fun displayBoardDetails(boardDto: BoardDtlDto) {
    }
}