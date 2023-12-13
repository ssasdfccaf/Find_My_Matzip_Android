package com.example.find_my_matzip.navTab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.find_my_matzip.databinding.ItemNewmainboardBinding
import com.example.find_my_matzip.model.MainBoardDto
import com.example.find_my_matzip.model.NewMainBoardDto

class NewMainBoardViewHolder(private val binding :ItemNewmainboardBinding,private val onItemClick: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: NewMainBoardDto) {
        //유저프로필이미지
        Glide.with(binding.root)
            .load(item.user.userImage)
            .override(40, 40)
            .into(binding.userProfileImg)
        //유저이름
        binding.userId.text = item.user.username
        //게시글아이디설정
        binding.boardId.text = item.id.toString()
        //게시글제목
        binding.boardTitle.text = item.boardTitle
        //게시글평점
        binding.boardScore.rating = item.score.toFloat()

        val viewPager = binding.ImgViewPager
        // 이미지 리사이클러뷰 어댑터 초기화
        val viewPagerAdapter = NewHomeViewPagerAdapter(binding.root.context, item.boardImgDtoList)
        viewPager.adapter = viewPagerAdapter

        binding.allItem.setOnClickListener {
            onItemClick(binding.boardId.text.toString())
        }
    }
}
class NewHomeRecyclerAdapter(context : Context) : RecyclerView.Adapter<NewMainBoardViewHolder>() {

    private val datas : MutableList<NewMainBoardDto> = mutableListOf()

    fun addData(newBoardList: List<NewMainBoardDto>) {
        datas.addAll(newBoardList)
        notifyDataSetChanged()
    }
    // 클릭 리스너 설정
    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewMainBoardViewHolder {
        val itemBinding = ItemNewmainboardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewMainBoardViewHolder(itemBinding){boardId ->
            onItemClickListener?.invoke(boardId)
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: NewMainBoardViewHolder, position: Int) {
        val item = datas[position]
        holder.bind(item)
    }



}