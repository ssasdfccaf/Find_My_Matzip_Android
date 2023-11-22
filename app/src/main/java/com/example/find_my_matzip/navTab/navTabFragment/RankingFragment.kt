package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.databinding.FragmentRankingBinding
import com.example.find_my_matzip.model.RankingDto
import com.example.find_my_matzip.navTab.adapter.RankingRecyclerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankingFragment : Fragment() {
    lateinit var binding :FragmentRankingBinding
    lateinit var adapter: RankingRecyclerAdapter
//    lateinit var restaurantService: RestaurantService
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = FragmentRankingBinding.inflate(layoutInflater)
//        restaurantService =  (requireContext().applicationContext as MyApplication).restaurantService
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankingBinding.inflate(layoutInflater, container, false)

//        val restaurantService =
//            (requireContext().applicationContext as MyApplication).restaurantService
        val restaurantService = (context?.applicationContext as MyApplication).restaurantService

        val restaurantList = restaurantService.getTop3RestaurantsByAvgScore()
        Log.d("syy", "restaurantList.enqueue 호출전 : ")
        restaurantList.enqueue(object : Callback<List<RankingDto>> {
            override fun onResponse(
                call: Call<List<RankingDto>>,
                response: Response<List<RankingDto>>
            ) {
                Log.d("syy", "도착 확인1: ")
                val restaurantList = response.body()
                Log.d("syy", "도착 확인2: restaurantList ${restaurantList}")
                if (restaurantList != null && restaurantList.isNotEmpty()) {
                    val firstRestaurant = restaurantList[0]
                    Log.d("syy", "restaurantList의 값: ${firstRestaurant.resId}")
                    Log.d("syy", "restaurantList의 값: ${firstRestaurant.avgScore}")
                    Log.d("syy", "restaurantList의 값: ${firstRestaurant.resName}")
                    Log.d("syy", "restaurantList의 값: ${firstRestaurant.resThumbnail}")
                    Log.d("syy", "Full Response: $restaurantList")

                    val layoutManager = LinearLayoutManager(requireContext())
                    binding.rankingRecyclerView.layoutManager = layoutManager
                    adapter = RankingRecyclerAdapter(this@RankingFragment,restaurantList)
//                    binding.rankingRecyclerView.adapter = RankingRecyclerAdapter(requireContext(), restaurantList)
                    binding.rankingRecyclerView.adapter = adapter
                } else {
                    Log.e("syy", "Response body is null or empty.")

                }
            }

            override fun onFailure(call: Call<List<RankingDto>>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                Log.e("syy", " 통신 실패")
            }
        })

        return binding.root
    }}