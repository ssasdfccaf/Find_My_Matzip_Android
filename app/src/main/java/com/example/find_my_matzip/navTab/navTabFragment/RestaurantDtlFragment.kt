package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentRestaurantDtlBinding
import com.example.find_my_matzip.model.RestaurantDto
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantDtlFragment : Fragment() {
    private var resId: String? = null
    lateinit var binding : FragmentRestaurantDtlBinding

    //resId 값으로 식당상세페이지 이동
    companion object {
        fun newInstance(resId: String): RestaurantDtlFragment {
            val fragment = RestaurantDtlFragment()
            val args = Bundle()
            args.putString("resId", resId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentRestaurantDtlBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resId = arguments?.getString("resId")
        binding = FragmentRestaurantDtlBinding.inflate(layoutInflater,container,false)

        val restaurantService = (context?.applicationContext as MyApplication).restaurantService
        val restaurantDtl = arguments?.getString("resId")?.let { restaurantService.getRestaurantDtl(it) }

        Log.d("restaurantDtlFlagment","restaurantDtl.enqueue 호출 전 : $restaurantDtl")

        restaurantDtl?.enqueue(object : Callback<RestaurantDto>{
            override fun onResponse(call: Call<RestaurantDto>, response: Response<RestaurantDto>) {

                Log.d("kkt","레스토랑 도착 확인.")
                val restaurantDto = response.body()
                Log.d("kkt","레스토랑 도착 확인1. : restaurantDto $restaurantDto")
                Log.d("kkt","레스토랑 도착 확인2. : restaurantDto.res_id ${restaurantDto?.res_id}")
                Log.d("kkt","레스토랑 도착 확인3. : restaurantDto.operate_time ${restaurantDto?.operate_time}")
                Log.d("kkt","레스토랑 도착 확인4. : restaurantDto.res_address ${restaurantDto?.res_address}")
                Log.d("kkt","레스토랑 도착 확인5. : restaurantDto.res_district ${restaurantDto?.res_district}")
                Log.d("kkt","레스토랑 도착 확인6. : restaurantDto.res_intro ${restaurantDto?.res_intro}")
                Log.d("kkt","레스토랑 도착 확인7. : restaurantDto.avgScore ${restaurantDto?.avgScore}")

                binding.resName.text = restaurantDto?.res_name.toString()
                binding.resAddress.text = restaurantDto?.res_address.toString()
                binding.operationTime.text = restaurantDto?.operate_time.toString()
                binding.resMenu.text = restaurantDto?.res_menu.toString()
                binding.resPhone.text = restaurantDto?.res_phone.toString()
                binding.resIntro.text = restaurantDto?.res_intro.toString()

                val formattedScore = String.format("%.1f", restaurantDto?.avgScore ?: 0.0)
                binding.resScore.text = formattedScore

                Log.d("kkt","레스토랑dtl 바인딩완료")

                Glide.with(requireContext())
                    .load(restaurantDto?.res_thumbnail)
                    .override(900, 900)
                    .into(binding.resThumbnail)

                Log.d("MyPageFragment", "도착 확인2: res_thumbnail ${restaurantDto?.res_thumbnail}")

            }

            override fun onFailure(call: Call<RestaurantDto>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })



        return binding.root

    }
}