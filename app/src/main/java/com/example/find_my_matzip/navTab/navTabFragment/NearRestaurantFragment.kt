package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.find_my_matzip.databinding.FragmentNearRestaurantBinding
import com.example.find_my_matzip.model.ResWithScoreDto
import com.example.find_my_matzip.navTab.adapter.NearRestaurantRecyclerAdapter
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class NearRestaurantFragment : Fragment() {
    lateinit var binding: FragmentNearRestaurantBinding
    lateinit var adapter: NearRestaurantRecyclerAdapter
    var restaurantsInsideCircle: List<ResWithScoreDto> = emptyList() // 이 부분을 추가하고 초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNearRestaurantBinding.inflate(layoutInflater)

        // restaurantsInsideCircle를 어댑터에 전달하여 초기화
        Log.d("restaurantsInsideCircle","$restaurantsInsideCircle")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearRestaurantBinding.inflate(layoutInflater, container, false)

        val nearRestaurantList = restaurantsInsideCircle

        if (nearRestaurantList.isNotEmpty()) {
            val firstRestaurant = nearRestaurantList[0]
            val layoutManager = LinearLayoutManager(requireContext())
            binding.nearResListRecyclerView.layoutManager = layoutManager
            adapter = NearRestaurantRecyclerAdapter(this, nearRestaurantList)
            binding.nearResListRecyclerView.adapter = adapter
        } else {
            // restaurantsInsideCircle가 비어있을 때의 처리
            // 예: 사용자에게 어떤 메시지를 보여주거나 다른 작업 수행
        }

        return binding.root
    }
}