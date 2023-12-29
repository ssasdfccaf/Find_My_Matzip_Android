package com.example.find_my_matzip.search

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.FragmentBoardSearchBinding
import com.example.find_my_matzip.databinding.FragmentProfileUpdateBinding

class BoardSearchFragment : Fragment() {
    lateinit var binding: FragmentBoardSearchBinding
    private val TAG: String = "BoardSearchFragment"

    companion object {
        fun newInstance(text: String) =
            BoardSearchFragment().apply {
                arguments = Bundle().apply {
                    putString("text", text)
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("SdoLifeCycle","BoardSearchFragment onCreateView")
        binding = FragmentBoardSearchBinding.inflate(layoutInflater, container, false)

        //전달 받은 검색어
        val newText = arguments?.getString("text")
        Log.d(TAG, "newText : $newText")
        binding.boardText.text = newText

        return binding.root
    }

}