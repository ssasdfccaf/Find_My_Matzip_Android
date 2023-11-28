package com.example.find_my_matzip.navTab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.find_my_matzip.databinding.NearRestaurantListBinding
import com.example.find_my_matzip.model.ResWithScoreDto
import com.example.find_my_matzip.navTab.navTabFragment.NearRestaurantFragment

class NearRestaurantViewHolder(val binding: NearRestaurantListBinding) : RecyclerView.ViewHolder(binding.root)

class NearRestaurantRecyclerAdapter(
        val fragment: NearRestaurantFragment,
        private var nearRestaurantList: List<ResWithScoreDto>
) : RecyclerView.Adapter<NearRestaurantViewHolder>() {
        init {
                // avgScore 내림차순으로 정렬
                nearRestaurantList = nearRestaurantList.sortedByDescending { it.avgScore }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearRestaurantViewHolder {
                return NearRestaurantViewHolder(
                        NearRestaurantListBinding.inflate(
                                LayoutInflater.from(parent.context),
                                parent,
                                false
                        )
                )
        }

        override fun getItemCount(): Int {
                return nearRestaurantList.size
        }

        override fun onBindViewHolder(holder: NearRestaurantViewHolder, position: Int) {
                val binding = holder.binding
                val item = nearRestaurantList[position]

                binding.resName.text = item.res_name
                binding.resMenu.text = item.res_menu
                binding.resPhone.text = item.res_phone
                binding.avgScore.text = item.avgScore.toString()

                // Glide에서 null 체크 추가
                if (item.res_thumbnail != null) {
                        Glide.with(binding.root.context)
                                .load(item.res_thumbnail)
                                .override(150, 150)
                                .into(binding.resThumbnail)
                }
        }
}