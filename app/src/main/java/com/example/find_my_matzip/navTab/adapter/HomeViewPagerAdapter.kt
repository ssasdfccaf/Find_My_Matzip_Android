package com.example.find_my_matzip.navTab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.find_my_matzip.databinding.ViewPagerHomeColorBinding

class HomeViewPagerAdapter  : RecyclerView.Adapter<HomeViewPagerAdapter.ViewHolder>() {

    lateinit var items: ArrayList<String>

    fun build(i: ArrayList<String>): HomeViewPagerAdapter {
        items = i
        return this
    }

    class ViewHolder(val binding: ViewPagerHomeColorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.viewPagerColor.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewPagerAdapter.ViewHolder =
        ViewHolder(ViewPagerHomeColorBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: HomeViewPagerAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}