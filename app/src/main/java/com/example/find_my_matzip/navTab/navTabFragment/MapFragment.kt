package com.example.find_my_matzip.navTab.navTabFragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.find_my_matzip.MyApplication
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentMapBinding
import com.example.find_my_matzip.model.ResWithScoreDto
import com.example.find_my_matzip.model.RestaurantDto
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapFragment : Fragment() , OnMapReadyCallback {
    private val LOCATION_PERMISSION_REQUEST_CODE = 5000
    lateinit var binding: FragmentMapBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource


    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMapBinding.inflate(layoutInflater)
        if (!hasPermission()) {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initMapView()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }
    private fun initMapView() {
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        // fragment의 getMapAsync() 메서드로 OnMapReadyCallback 콜백을 등록하면 비동기로 NaverMap 객체를 얻을 수 있다.
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }
    // hasPermission()에서는 위치 권한이 있을 경우 true를, 없을 경우 false를 반환한다.
    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onMapReady(naverMap: NaverMap) {

        Log.d("sdo", "onMapReady")
        val cameraPosition = CameraPosition(
            LatLng(35.15690579523921, 129.05957113473747),  // 지도 시작 위치 지정
            14.0 // 줌 레벨
        )
        val restaurantService = (context?.applicationContext as MyApplication).restaurantService
        val restaurantList = restaurantService.getAllRestaurantsByAvgScore()

        restaurantList.enqueue(object : Callback<List<ResWithScoreDto>> {
            override fun onResponse(
                call: Call<List<ResWithScoreDto>>,
                response: Response<List<ResWithScoreDto>>
            ) {
                val restaurantList = response.body()
                if (restaurantList != null && restaurantList.isNotEmpty()) {
                    for(i in restaurantList.indices)
                    {
                        val currentRestaurant = restaurantList[i]
                        val latitude: Double = currentRestaurant.res_lat.toDouble()
                        val longitude: Double = currentRestaurant.res_lng.toDouble()

                        // 네이버 지도 API를 이용하여 마커 생성
                        val marker = Marker()
                        marker.position = LatLng(latitude, longitude)

                        // 마커를 지도에 추가
                        marker.map = naverMap

                        marker.setOnClickListener(Overlay.OnClickListener {
                            val intent = Intent(context, ResInfoActivity::class.java)
                            intent.putExtra("resInfoName", currentRestaurant.res_name)
                            intent.putExtra("resInfoAvgScore", currentRestaurant.avgScore)
                            intent.putExtra("resInfoMenu", currentRestaurant.res_menu)
                            intent.putExtra("resInfoOT", currentRestaurant.operate_time)
                            intent.putExtra("resInfoIntro", currentRestaurant.res_intro)
                            intent.putExtra("resInfoThumbnail", currentRestaurant.res_thumbnail)

//                            Log.d("infotest", "식당사진 ${currentRestaurant.res_thumbnail}")
//                            Log.d("infotest", "식당이름 ${currentRestaurant.res_name}")
//                            Log.d("infotest", "식당메뉴 ${currentRestaurant.res_menu}")
//                            Log.d("infotest", "영업시간 ${currentRestaurant.operate_time}")
//                            Log.d("infotest", "식당소개 ${currentRestaurant.res_intro}")
                            Log.d("infotest", "식당평점 ${currentRestaurant.avgScore}")


                            startActivity(intent)
                            false
                        })

                    //    Log.d("sdo", "식당 $i - 위도: ${currentRestaurant.res_lat}, 경도: ${currentRestaurant.res_lng}")

                    }
                //    Log.d("sdo", "Full Response: $restaurantList")
                } else {
                //    Log.e("sdo", "Response body is null or empty.")

                }
            }
            override fun onFailure(call: Call<List<ResWithScoreDto>>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                Log.e("sdo", " 통신 실패")
            }
        })


      //  marker.position = LatLng(35.15690579523921, 129.05957113473747)
      //  marker.map = naverMap


        naverMap.cameraPosition = cameraPosition

        this.naverMap = naverMap
        // 현재 위치
        naverMap.locationSource = locationSource
        // 현재 위치 버튼 기능
        naverMap.uiSettings.isLocationButtonEnabled = true
        // 위치를 추적하면서 카메라도 따라 움직인다.
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }




}