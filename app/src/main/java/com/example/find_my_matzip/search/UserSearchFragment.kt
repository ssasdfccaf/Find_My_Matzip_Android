package com.example.find_my_matzip.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.find_my_matzip.databinding.FragmentUserSearchBinding

class UserSearchFragment : Fragment() {
    lateinit var binding:FragmentUserSearchBinding
    private val TAG: String = "UserSearchFragment"

    companion object {
        fun newInstance(text: String) =
            UserSearchFragment().apply {
                arguments = Bundle().apply {
                    putString("text", text)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("SdoLifeCycle","UserSearchFragment onCreateView")
        binding = FragmentUserSearchBinding.inflate(layoutInflater, container, false)
        
        //전달 받은 검색어
        val newText = arguments?.getString("text")
        Log.d(TAG, "newText : $newText")

        return binding.root
    }


}