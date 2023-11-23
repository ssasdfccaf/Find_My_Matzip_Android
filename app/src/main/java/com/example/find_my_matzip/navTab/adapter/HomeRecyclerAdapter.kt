package com.example.find_my_matzip.navTab.adapter
import android.content.ClipData.Item
import com.example.find_my_matzip.databinding.HomeFragmentItemBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.find_my_matzip.databinding.ItemMainboardBinding
import com.example.find_my_matzip.model.MainBoardDto
import com.example.find_my_matzip.navTab.navTabFragment.HomeFragment

class MainBoardViewHolder(val binding : ItemMainboardBinding) : RecyclerView.ViewHolder(binding.root)

class HomeRecyclerAdapter(val context: HomeFragment, val  datas : List<MainBoardDto>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MainBoardViewHolder(
            ItemMainboardBinding.inflate(
                LayoutInflater.from(parent.context),parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MainBoardViewHolder).binding
        val Item = datas?.get(position)

        binding.boardId.text = Item?.id
        binding.boardTitle.text = Item?.boardTitle
        binding.Score.text = Item?.score.toString()

        Glide.with(context)
            .load(Item?.imgUrl)
            .override(900, 900)
            .into(binding.boardThumbnail)
    }

}