package com.example.find_my_matzip.navTab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.find_my_matzip.databinding.ViewPagerHomeColorBinding

class HomeFollowViewPagerAdapter  : RecyclerView.Adapter<HomeFollowViewPagerAdapter.ViewHolder>() {

    lateinit var items: ArrayList<String>

    fun build(i: ArrayList<String>): HomeFollowViewPagerAdapter {
        items = i
        return this
    }

    class ViewHolder(val binding: ViewPagerHomeColorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.viewPagerColor.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFollowViewPagerAdapter.ViewHolder =
        ViewHolder(ViewPagerHomeColorBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: HomeFollowViewPagerAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}