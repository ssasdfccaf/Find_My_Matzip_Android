package com.matzip.find_my_matzip.navTab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matzip.find_my_matzip.R

class FullScreenImageAdapter(private val context: Context, private val imageUrls: List<String>) :
    RecyclerView.Adapter<FullScreenImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_full_screen_image, parent, false)


        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        holder.loadImage(imageUrl)
    }

    override fun getItemCount(): Int {
        return imageUrls.count { it.isNotEmpty() } // 비어있지 않은 이미지 URL의 개수 반환
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.fullScreenImageView)

        fun loadImage(imageUrl: String) {
            Glide.with(context)
                .load(imageUrl)
                .into(imageView)
        }
    }
}
