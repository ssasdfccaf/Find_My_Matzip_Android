package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentHomeFollowBinding
import com.example.find_my_matzip.navTab.adapter.HomeFollowRecyclerAdapter
import com.example.find_my_matzip.navTab.adapter.RecyclerItem2


class HomeFollowFragment : Fragment() {
    lateinit var binding: FragmentHomeFollowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeFollowBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeFollowBinding.inflate(layoutInflater, container, false)

        binding.homeRecyclerView.apply {
            adapter = HomeFollowRecyclerAdapter().build(items)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding.toHome.setOnClickListener {
            // 클릭 시 HomeFollowFragment로 이동하는 코드
            val fragment = HomeFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    val items = arrayListOf<RecyclerItem2>(
//        RecyclerItem2("cozy", arrayListOf("pink", "purple", "sky blue", "white")),
//        RecyclerItem2(
//            "rainbow",
//            arrayListOf("red", "orange", "yellow", "green", "blue", "indigo", "purple")
//        ),
//        RecyclerItem2(
//            "Healthy Leaves",
//            arrayListOf("Olive Green", "Lime Green", "Yellow Green", "Green")
//        ),
//        RecyclerItem2("korea", arrayListOf("white", "black", "blue", "red")),
//        RecyclerItem2("usa", arrayListOf("blue", "red", "white")),
//        RecyclerItem2("italy", arrayListOf("green", "white", "red")),
//        RecyclerItem2("china", arrayListOf("red", "yellow")),
//        RecyclerItem2("google", arrayListOf("red", "yellow", "green", "blue")),
    )
}