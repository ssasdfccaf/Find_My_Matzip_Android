package com.example.find_my_matzip.navTab.navTabFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentBoardDtlBinding

class boardDtlFragment : Fragment() {
    lateinit var binding: FragmentBoardDtlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBoardDtlBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardDtlBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}