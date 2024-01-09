package com.example.find_my_matzip.navTab.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.find_my_matzip.R
import com.example.find_my_matzip.model.BoardImgDto
import com.example.find_my_matzip.navTab.navTabFragment.boardDtlFullScreenImageFragment

class BoardDtlViewPagerAdapter(private val context: Context, private val boardImgDtoList: List<BoardImgDto>) :
    RecyclerView.Adapter<BoardDtlViewPagerAdapter.ImageViewHolder>() {

    private val imageUrls = boardImgDtoList.filter { it.imgUrl.isNotEmpty() }
    private var currentPosition = 0
    fun setCurrentPosition(position: Int) {
        currentPosition = position
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_home_view_pager, parent, false)

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageInfo = boardImgDtoList[position]

        if (imageInfo.imgUrl.isNotEmpty()) {
            if (position == 0 && imageInfo.repImgYn == "Y") {
                holder.loadImage(imageInfo.imgUrl) // 첫 번째 이미지, repImgYn이 "Y"인 경우
            } else if (position > 0) {
                val nonRepImages = boardImgDtoList.filter { it.repImgYn == "N" }
                val nonRepPosition = position - 1 // 첫 번째 이미지 이후의 위치를 계산
                if (nonRepPosition < nonRepImages.size) {
                    holder.loadImage(nonRepImages[nonRepPosition].imgUrl)
                }
            }
        }
    }
    override fun getItemCount(): Int {
//        return minOf(boardImgDtoList.size, 5)
        //이미지 url이 있을때만 뷰페이저아이템만들기
        return imageUrls.size

    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.viewPagerImageView)

        // 이미지 뷰가 클릭됐을 때의 동작 설정
        init {
            itemView.setOnClickListener {
                val imageUrlList = boardImgDtoList.mapNotNull { it.imgUrl } // BoardImgDto에서 이미지 URL만 추출하여 리스트로 만듭니다.
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val fragment = boardDtlFullScreenImageFragment()
                    val bundle = Bundle()
                    bundle.putStringArrayList("image_urls", ArrayList(imageUrlList))
                    bundle.putInt("selected_position", position)
                    fragment.arguments = bundle

                    val activity = itemView.context as AppCompatActivity
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        fun loadImage(imageUrl: String) {
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.readyimg)
                .error(R.drawable.noimg)
                .into(imageView)
        }
    }
}