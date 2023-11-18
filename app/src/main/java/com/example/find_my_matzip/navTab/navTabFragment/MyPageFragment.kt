package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.find_my_matzip.ProfileUpdateFragment
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentMyPageBinding


class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMyPageBinding.inflate(layoutInflater)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(layoutInflater, container, false)
        binding.updateBtn.setOnClickListener {

           // profileUpdateFragment 회원수정창(타 프레그먼트로) 이동하는 코드
            val profileUpdateFragment = ProfileUpdateFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, profileUpdateFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return binding.root


    }
}
