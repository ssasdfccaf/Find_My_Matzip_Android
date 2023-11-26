package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentProfileUpdateBinding

class ProfileUpdateFragment : Fragment() {
    lateinit var binding: FragmentProfileUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileUpdateBinding.inflate(layoutInflater)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileUpdateBinding.inflate(layoutInflater, container, false)

        binding.cancelBtn.setOnClickListener {
            //현재 fragment 아예 지우고, 이전 fragment 띄우기
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root


    }
}
