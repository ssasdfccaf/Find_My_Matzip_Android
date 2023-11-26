package com.example.find_my_matzip.navTab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.find_my_matzip.databinding.ItemDialogBinding


//
//class FollowerViewHolder(val binding: ItemDialogBinding) : RecyclerView.ViewHolder(binding.root)

class FollowerAdapter(val context: Context, var datas: List<String>?, private val listener: OnFollowerClickListener) :
    RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val binding = ItemDialogBinding.inflate(LayoutInflater.from(context), parent, false)
        return FollowerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }
    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val followerId = datas?.get(position)
        holder.bind(followerId)
    }
    inner class FollowerViewHolder(val binding: ItemDialogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(followerId: String?) {
            binding.dialogUserid.text = followerId
            binding.root.setOnClickListener {
                listener.onFollowClick(followerId ?: "")
            }
        }
    }

//    FollowerAdapter의 OnFollowerClickListener에서 팔로워를 클릭했을 때 호출되는 onFollowClick 메서드에서 해당 유저의 프로필로 이동하는 코드를 추가
    interface OnFollowerClickListener {
        fun onFollowClick(followerId: String)
    }
}
