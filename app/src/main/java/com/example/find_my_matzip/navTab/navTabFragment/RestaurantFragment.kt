package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.databinding.FragmentRestaurantBinding
import com.example.find_my_matzip.model.RestaurantDto
import com.example.find_my_matzip.navTab.adapter.RestaurantRecyclerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RestaurantFragment : Fragment() {
    lateinit var binding: FragmentRestaurantBinding
    lateinit var adapter: RestaurantRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentRestaurantBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantBinding.inflate(layoutInflater, container, false)

        val restaurantService = (context?.applicationContext as MyApplication).restaurantService
        val restaurantList = restaurantService.getResList()

        restaurantList.enqueue(object : Callback<List<RestaurantDto>> {
            override fun onResponse(
                call: Call<List<RestaurantDto>>,
                response: Response<List<RestaurantDto>>
            ) {
                val restaurantList = response.body()
                if (restaurantList != null && restaurantList.isNotEmpty()) {
                    val firstRestaurant = restaurantList[0]
                    val layoutManager = LinearLayoutManager(requireContext())
                    binding.resListRecyclerView.layoutManager = layoutManager
                    adapter = RestaurantRecyclerAdapter(this@RestaurantFragment,restaurantList)
//                    binding.rankingRecyclerView.adapter = RankingRecyclerAdapter(requireContext(), restaurantList)
                    binding.resListRecyclerView.adapter = adapter
                } else {

                }
            }
            override fun onFailure(call: Call<List<RestaurantDto>>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
            }
        })

        return binding.root
    }}