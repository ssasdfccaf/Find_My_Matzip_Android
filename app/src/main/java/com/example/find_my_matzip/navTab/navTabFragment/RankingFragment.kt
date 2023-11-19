package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.find_my_matzip.databinding.FragmentRankingBinding
import com.example.find_my_matzip.navTab.adapter.RankingRecyclerAdapter

class RankingFragment : Fragment() {
    lateinit var binding :FragmentRankingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=FragmentRankingBinding.inflate(layoutInflater)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankingBinding.inflate(layoutInflater, container, false)

        // 더미로 사용한 텍스트 ..!
        var datas = mutableListOf<String>()
        for (i in 1..10) {
            datas.add("  $i ")
        }

        var layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        // recyclerView 해당 .. 수직으로 넣기
        binding.rankingRecyclerView.layoutManager = layoutManager
        // 액티비티 - > 리사이클러 뷰 -> 실제 데이터를 연결하는 부분
        binding.rankingRecyclerView.adapter = RankingRecyclerAdapter(datas)
        binding.rankingRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )

        return binding.root
    }
}