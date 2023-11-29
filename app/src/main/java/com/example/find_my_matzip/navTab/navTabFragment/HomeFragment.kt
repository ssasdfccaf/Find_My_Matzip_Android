package com.example.find_my_matzip.navTab.navTabFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    lateinit var boardList: Call<List<MainBoardDto>>
    private val TAG: String = "HomeFragment"
    private var text:String? = null

    companion object {
        // HomeFragment 인스턴스 생성
        fun newInstance(text: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("text", text)
            Log.d("sjw", "내가 newInstance에서 넣은 text : $text")
            fragment.arguments = args
            return fragment
        }
    }



    //페이징처리 1
    var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("SdoLifeCycle","HomeFragment onCreate")
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("SdoLifeCycle","HomeFragment onCreateView")

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        
        //검색창에서 넘어왔다면 text넣어줌
        val newText = arguments?.getString("text")
        Log.d("sjw", "newText : $newText")
        if(newText != null){
            text = newText
        }


        binding.toFollowHome.setOnClickListener {
            // 클릭 시 HomeFollowFragment로 이동하는 코드
            val fragment = HomeFollowFragment()

            // 트랜잭션에 이름 부여
            val transaction = parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                //    .addToBackStack("HomeFragment")
                .commit()

            // 현재의 HomeFragment를 백 스택에서 제거
            parentFragmentManager.popBackStack("HomeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        adapter = HomeRecyclerAdapter(requireContext()).apply {
            setOnItemClickListener { boardId ->
                navigateToBoardDetail(boardId)
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.homeRecyclerView.layoutManager = layoutManager

        binding.homeRecyclerView.adapter = adapter

        binding.homeRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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


        //검색창 관리
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                // 검색 버튼 누를 때 호출
                if (!query.isNullOrBlank()) {
                    Log.d(TAG, "검색 버튼 클릭: $query")

                    //검색 수행
                    navigateSearchResult(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색창에서 글자가 변경이 일어날 때마다 호출
                //Log.d(TAG, "글자 변경 : $newText")


                return true
            }
        })



        return binding.root
    }

    @Override
    override fun onResume() {
        Log.d("SdoLifeCycle","HomeFragment onResume")
        super.onResume()
    }
    @Override
    override fun onPause() {
        Log.d("SdoLifeCycle","HomeFragment onPause")
        super.onPause()
    }
    @Override
    override fun onDestroy() {
        Log.d("SdoLifeCycle","HomeFragment onDestroy")
        super.onDestroy()
    }

    private fun navigateToBoardDetail(boardId: String) {
        val fragment = boardDtlFragment.newInstance(boardId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun loadNextPageData(page: Int) {
        val boardService = (context?.applicationContext as MyApplication).boardService

        if(text != null){
            // 검색 단어가 있을때
            Log.d(TAG, "검색중 $text")
            boardList = boardService.getSearchMainBoards("$text",page)
        }else{
            //전체 조회
            Log.d(TAG, "전체 조회")
            boardList = boardService.getAllBoardsPager(page)
        }

        boardList.enqueue(object : Callback<List<MainBoardDto>> {
            override fun onResponse(
                call: Call<List<MainBoardDto>>,
                response: Response<List<MainBoardDto>>
            ) {
                if (response.isSuccessful) {
                    val newBoardList = response.body()
                    newBoardList?.let {
                        adapter.addData(it)
                    }
                }
            }


            override fun onFailure(call: Call<List<MainBoardDto>>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                Log.d(TAG, " 통신 실패")
            }


        })
    }


    //fragment전환
    private fun navigateSearchResult(text:String) {
        val changeFragment = newInstance(text)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, changeFragment)
        .addToBackStack(null)
        transaction.commit()
    }


}


//페이징 처리전 코드
//        val boardList = boardService.getAllBoards()
//        Log.d("kkt", "boardList.enqueue 호출전 : ")
//        boardList.enqueue(object : Callback<List<MainBoardDto>> {
//            override fun onResponse(
//                call: Call<List<MainBoardDto>>,
//                response: Response<List<MainBoardDto>>
//            ) {
//                Log.d("kkt", "도착 확인1: ")
//                val boardList = response.body()
//                Log.d("kkt", "도착 확인2: restaurantList ${boardList}")
//                if (boardList != null && boardList.isNotEmpty()) {
//                    val firstBoard = boardList[0]
//                    val layoutManager = LinearLayoutManager(requireContext())
//                    binding.homeRecyclerView.layoutManager = layoutManager
//                    adapter = HomeRecyclerAdapter(this@HomeFragment,boardList)
//                    binding.homeRecyclerView.adapter = adapter
//                } else {
//                    Log.e("kkt", "Response body is null or empty.")
//
//                }
//            }

