package com.example.find_my_matzip.navTab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.find_my_matzip.databinding.ItemRankingBinding
import com.example.find_my_matzip.model.RankingDto
import com.example.find_my_matzip.navTab.navTabFragment.RankingFragment


//item_ranking
class RankingViewHoder(val binding: ItemRankingBinding) : RecyclerView.ViewHolder(binding.root)

class RankingRecyclerAdapter(val context: RankingFragment, val datas: List<RankingDto>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RankingViewHoder(
            ItemRankingBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as RankingViewHoder).binding
        val Item = datas?.get(position)

        binding.resId.text = Item?.resId
        binding.resName.text = Item?.resName
        binding.avgScore.text = Item?.avgScore.toString()

        Glide.with(context)
            .load(Item?.resThumbnail)
            .override(900, 900)
            .into(binding.resThumbnail)

    }
}