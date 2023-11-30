package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentRestaurantBinding
import com.example.find_my_matzip.model.MainBoardDto
import com.example.find_my_matzip.model.ResWithScoreDto
import com.example.find_my_matzip.navTab.adapter.RankingRecyclerAdapter
import com.example.find_my_matzip.navTab.adapter.RestaurantRecyclerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RestaurantFragment : Fragment() {
    lateinit var binding: FragmentRestaurantBinding
    lateinit var adapter: RestaurantRecyclerAdapter
    lateinit var restaurantList: Call<List<ResWithScoreDto>>
    lateinit var avgScoreList: Call<List<ResWithScoreDto>>
    private var text:String? = null

    companion object {
        fun newInstance(text: String): RestaurantFragment {
            val fragment = RestaurantFragment()
            val args = Bundle()
            args.putString("text", text)
            Log.d("RestaurantFragment", "내가 newInstance에 넣은 text : $text")
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("SdoLifeCycle","RestaurantFragment onCreate")
        super.onCreate(savedInstanceState)
        binding = FragmentRestaurantBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("SdoLifeCycle","RestaurantFragment onCreateView")
        binding = FragmentRestaurantBinding.inflate(layoutInflater, container, false)

        //검색창에서 넘어왔다면 text넣어줌
        val newText = arguments?.getString("text")
        Log.d("RestaurantFragment", "newText : $newText")
        if(newText != null){
            text = newText
        }

        val restaurantService = (context?.applicationContext as MyApplication).restaurantService

        if(text != null){
            //검색 단어가 있을 때
            Log.d("RestaurantFragment", "검색중 $text")
            restaurantList = restaurantService.getSearchRestaurantsByAvgScore(text!!)
            avgScoreList = restaurantService.getSearchRestaurantsByAvgScore(text!!)

        }else{
            //전체 조회
            Log.d("RestaurantFragment", "전체 조회")
            restaurantList = restaurantService.getAllRestaurantsByAvgScore()
            avgScoreList = restaurantService.getAllRestaurantsByAvgScore()
        }


        restaurantList.enqueue(object : Callback<List<ResWithScoreDto>> {
            override fun onResponse(
                call: Call<List<ResWithScoreDto>>,
                response: Response<List<ResWithScoreDto>>
            ) {
                val restaurantList = response.body()
                if (restaurantList != null && restaurantList.isNotEmpty()) {
                    val firstRestaurant = restaurantList[0]
                    val layoutManager = LinearLayoutManager(requireContext())
                    binding.resListRecyclerView.layoutManager = layoutManager
                    adapter = RestaurantRecyclerAdapter(this@RestaurantFragment,restaurantList)
                    Log.d("sdotest", "${firstRestaurant.res_thumbnail}")
//                    binding.rankingRecyclerView.adapter = RankingRecyclerAdapter(requireContext(), restaurantList)
                    binding.resListRecyclerView.adapter = adapter
                } else {

                }
            }
            override fun onFailure(call: Call<List<ResWithScoreDto>>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
            }
        })

        //검색창
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                // 검색 버튼 누를 때 호출
                if (!query.isNullOrBlank()) {
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

    //fragment전환
    private fun navigateSearchResult(text:String) {
        val changeFragment = RestaurantFragment.newInstance(text)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, changeFragment)
        .addToBackStack(null)
        transaction.commit()
    }


    @Override
    override fun onResume() {
        Log.d("SdoLifeCycle","RestaurantFragment onResume")
        super.onResume()
    }
    @Override
    override fun onPause() {
        Log.d("SdoLifeCycle","RestaurantFragment onPause")
        super.onPause()
    }
    @Override
    override fun onDestroy() {
        Log.d("SdoLifeCycle","RestaurantFragment onDestroy")
        super.onDestroy()
    }
}