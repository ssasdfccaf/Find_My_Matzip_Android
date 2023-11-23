package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentHomeBinding
import com.example.find_my_matzip.model.MainBoardDto
import com.example.find_my_matzip.navTab.adapter.HomeRecyclerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class     HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var adapter : HomeRecyclerAdapter
//    lateinit var binding2:ViewPagerHomeColorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
//        binding2 = ViewPagerHomeColorBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.toFollowHome.setOnClickListener {
            // 클릭 시 HomeFollowFragment로 이동하는 코드
            val fragment = HomeFollowFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        val boardService = (context?.applicationContext as MyApplication).boardService

        val boardList = boardService.getAllBoards()
        Log.d("kkt", "boardList.enqueue 호출전 : ")
        boardList.enqueue(object : Callback<List<MainBoardDto>> {
            override fun onResponse(
                call: Call<List<MainBoardDto>>,
                response: Response<List<MainBoardDto>>
            ) {
                Log.d("kkt", "도착 확인1: ")
                val boardList = response.body()
                Log.d("kkt", "도착 확인2: restaurantList ${boardList}")
                if (boardList != null && boardList.isNotEmpty()) {
                    val firstBoard = boardList[0]
//                    val id:String,
//                    @SerializedName("board_title")
//                    val boardTitle:String,
//                    val content : String,
//                    val imgUrl : String,
//                    val score : Int
                    Log.d("kkt", "boardList의 id 값: ${firstBoard.id}")
                    Log.d("kkt", "boardList의 boardTitle 값: ${firstBoard.boardTitle}")
                    Log.d("kkt", "boardList의 content 값: ${firstBoard.content}")
                    Log.d("kkt", "boardList의 imgUrl 값: ${firstBoard.imgUrl}")
                    Log.d("kkt", "boardList의 score 값: ${firstBoard.score}")
                    Log.d("kkt", "Full Response: $firstBoard")

                    val layoutManager = LinearLayoutManager(requireContext())
                    binding.homeRecyclerView.layoutManager = layoutManager
                    adapter = HomeRecyclerAdapter(this@HomeFragment,boardList)
                    binding.homeRecyclerView.adapter = adapter
                } else {
                    Log.e("kkt", "Response body is null or empty.")

                }
            }

            override fun onFailure(call: Call<List<MainBoardDto>>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                Log.e("kkt", " 통신 실패")
            }
        })

        return binding.root
    }}


