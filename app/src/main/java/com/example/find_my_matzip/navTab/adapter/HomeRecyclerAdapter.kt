package com.example.find_my_matzip.navTab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.find_my_matzip.databinding.HomeFragmentItemBinding

class HomeRecyclerAdapter : RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>() {
    lateinit var items: ArrayList<RecyclerItem>

    fun build(i: ArrayList<RecyclerItem>): HomeRecyclerAdapter {
        items = i
        return this
    }

    class ViewHolder(val binding: HomeFragmentItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecyclerItem) {
            with(binding)
            {
                tvPalette.text = item.name
                viewPager.adapter = HomeViewPagerAdapter().build(item.colors)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerAdapter.ViewHolder =
        ViewHolder(
            HomeFragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            parent.context
        )

    override fun onBindViewHolder(holder: HomeRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}