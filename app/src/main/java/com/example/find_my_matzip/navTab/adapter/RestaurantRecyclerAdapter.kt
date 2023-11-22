package com.example.find_my_matzip.navTab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.find_my_matzip.databinding.RestaurantListBinding
import com.example.find_my_matzip.model.RestaurantDto
import com.example.find_my_matzip.navTab.navTabFragment.RestaurantFragment

class RestaurantViewHolder(val binding: RestaurantListBinding) : RecyclerView.ViewHolder(binding.root)

class RestaurantRecyclerAdapter(val context: RestaurantFragment, val datas: List<RestaurantDto>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RestaurantViewHolder(
            RestaurantListBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as RestaurantViewHolder).binding
        val Item = datas?.get(position)

        binding.resName.text = Item?.res_name
        binding.resMenu.text = Item?.res_menu
        binding.resIntro.text = Item?.res_intro.toString()

        Glide.with(context)
            .load(Item?.res_thumbnail)
            .override(900, 900)
            .into(binding.resThumbnail)

    }
}