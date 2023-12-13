package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentNewHomeBinding
import com.example.find_my_matzip.model.NewMainBoardDto
import com.example.find_my_matzip.navTab.adapter.NewHomeRecyclerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewHomeFragment : Fragment() {
    lateinit var binding : FragmentNewHomeBinding
    lateinit var adapter: NewHomeRecyclerAdapter
    lateinit var boardList: Call<List<NewMainBoardDto>>
    private val TAG : String = "NewHomeFragment"
    //페이징처리 1
    var currentPage = 0

    //식당의 게시글 띄우는 로직
    private var resId:String? = null
    companion object {
        // HomeFragment 인스턴스 생성
        fun newInstance(text: String, resId:String): NewHomeFragment {
            val fragment = NewHomeFragment()
            val args = Bundle()
            args.putString("text", text)
            args.putString("resId", resId)
            Log.d("HomeFragment", "내가 newInstance에서 넣은 text : $text")
            Log.d("HomeFragment", "내가 newInstance에서 넣은 resId : $resId")
            fragment.arguments = args
            return fragment
        }
    }

        override fun onCreate(savedInstanceState: Bundle?) {
            Log.d(TAG,"NewHomeFragment onCreate")
        super.onCreate(savedInstanceState)
            binding = FragmentNewHomeBinding.inflate(layoutInflater)
    }// 온크리트의 끝

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"NewHomeFragment onCreateView")

        binding = FragmentNewHomeBinding.inflate(layoutInflater,container,false)
//        val view = binding.root


        adapter = NewHomeRecyclerAdapter(requireContext()).apply {
            setOnItemClickListener { boardId ->
                navigateToBoardDetail(boardId)
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.newHomeRecyclerView.layoutManager = layoutManager

        binding.newHomeRecyclerView.adapter = adapter

        binding.newHomeRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    currentPage++
                    loadNextPageData(currentPage)
                }
            }
        })
        loadNextPageData(currentPage)

        return binding.root
    }//온크리트뷰의 끝

    private fun navigateToBoardDetail(boardId: String) {
        val fragment = boardDtlFragment.newInstance(boardId)
        parentFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, fragment)
            .addToBackStack("HomeFragment")
            .commit()
    }//navigateToBoardDetail 끝
    private fun loadNextPageData(page: Int) {
        val boardService = (context?.applicationContext as MyApplication).boardService
        boardList = boardService.getNewAllBoardsPager(page)
        boardList.enqueue(object : Callback<List<NewMainBoardDto>> {
            override fun onResponse(call: Call<List<NewMainBoardDto>>, response: Response<List<NewMainBoardDto>>) {
                if (response.isSuccessful) {
                    val newBoardList = response.body()
                    newBoardList?.let {
                        adapter.addData(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<NewMainBoardDto>>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                Log.d(TAG, " 통신 실패")
            }
        })
    } //loadNextPageData의 끝

}//프래그먼트의 끝