package com.example.find_my_matzip.search.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.LayoutSearchItemBinding
import com.example.find_my_matzip.model.SearchDto
import com.example.find_my_matzip.search.BoardSearchFragment
import java.text.SimpleDateFormat
import java.util.Locale

private val TAG: String = "SearchHistoryRecyclerViewAdapter"

class SearchItemViewHolder(val binding:LayoutSearchItemBinding):RecyclerView.ViewHolder(binding.root)

class SearchHistoryRecyclerViewAdapter(val context: Context, var datas: List<SearchDto>?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchItemViewHolder(
            LayoutSearchItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    fun addData(newData: List<SearchDto>) {
        val oldSize = datas?.size ?: 0
        datas = datas.orEmpty() + newData
        notifyItemRangeInserted(oldSize, newData.size)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as SearchItemViewHolder).binding
        val Item = datas?.get(position)

        binding.searchText.text = Item?.text
        binding.searchDate.text = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(Item?.date)


        binding.constraintSearchItem.setOnClickListener{
            Log.d(TAG,"constraintSearchItem 클릭")

            val newText = binding.searchText.text.toString()

            // 데이터를 전달하기 위한 Bundle 생성
            val bundle = Bundle().apply {
                putString("text", newText)
            }

            //인스턴스 생성
            val boardSearchFragment = BoardSearchFragment()
            // 인자로 데이터 전달
            boardSearchFragment.arguments = bundle

            // FragmentManager를 통해 Fragment 트랜잭션 시작
            val fragmentManager = (binding.root.context as AppCompatActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()

            // Fragment를 표시하는 코드
            transaction.add(R.id.fragmentContainer, boardSearchFragment)
            transaction.addToBackStack(null)
            // 트랜잭션 완료
            transaction.commit()

        }

        binding.deleteSearchBtn.setOnClickListener{
            Log.d(TAG,"deleteBtn 클릭")
        }

    }

}


