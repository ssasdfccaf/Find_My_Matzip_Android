package com.example.find_my_matzip.search

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentBoardSearchBinding
import com.example.find_my_matzip.databinding.FragmentNewHomeBinding
import com.example.find_my_matzip.databinding.FragmentProfileUpdateBinding
import com.example.find_my_matzip.model.NewMainBoardDto
import com.example.find_my_matzip.navTab.adapter.NewHomeRecyclerAdapter
import com.example.find_my_matzip.navTab.navTabFragment.MyPageFragment
import com.example.find_my_matzip.navTab.navTabFragment.ProfileFragment
import com.example.find_my_matzip.navTab.navTabFragment.boardDtlFragment
import com.example.find_my_matzip.utiles.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardSearchFragment : Fragment() {
    lateinit var binding: FragmentBoardSearchBinding
    lateinit var adapter: NewHomeRecyclerAdapter
    lateinit var boardList: Call<List<NewMainBoardDto>>

    private val TAG: String = "BoardSearchFragment"

    //페이징처리 1
    var currentPage = 0

    companion object {
        fun newInstance(text: String) =
            BoardSearchFragment().apply {
                arguments = Bundle().apply {
                    putString("text", text)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"BoardSearchFragment onCreate")
        super.onCreate(savedInstanceState)
        binding = FragmentBoardSearchBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("SdoLifeCycle","BoardSearchFragment onCreateView")
        binding = FragmentBoardSearchBinding.inflate(layoutInflater, container, false)

        //전달 받은 검색어
        val newText = arguments?.getString("text")
        Log.d(TAG, "newText : $newText")

        adapter = NewHomeRecyclerAdapter(requireContext()).apply {
            setOnItemClickListener { boardId ->
                navigateToBoardDetail(boardId)
            }
            setOnUserClickListener { userId ->
                navigateToUserProfile(userId)
            }
        }

        //아이템(게시글,유저) 클릭 이벤트
        adapter.setOnItemClickListener {boardId ->
            navigateToBoardDetail(boardId)
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.boardSearchRecyclerView.layoutManager = layoutManager

        binding.boardSearchRecyclerView.adapter = adapter


        binding.boardSearchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                Log.d(TAG, "visibleItemCount : $visibleItemCount")
                Log.d(TAG, "totalItemCount : $totalItemCount")
                Log.d(TAG, "firstVisibleItemPosition : $firstVisibleItemPosition")

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    currentPage++
                    Log.d(TAG, "currentPage : $currentPage")
                    loadNextPageData(currentPage)
                }
            }
        })
        loadNextPageData(currentPage)


        return binding.root
    }

    private fun navigateToBoardDetail(boardId: String) {
        val fragment = boardDtlFragment.newInstance(boardId)
        parentFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToUserProfile(userId: String?) {
        if (userId.isNullOrEmpty()) {
            Log.d(TAG, "userId is null or empty")
            return
        }

        val transaction = parentFragmentManager.beginTransaction()
        val userFrag: Fragment = if (SharedPreferencesManager.getString("id", "") == userId) {
            MyPageFragment.newInstance(userId)
        } else {
            ProfileFragment.newInstance(userId)
        }

        transaction.replace(R.id.fragmentContainer, userFrag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

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
    }//loadNextPageData

}