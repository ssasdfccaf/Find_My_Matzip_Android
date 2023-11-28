package com.example.find_my_matzip.navTab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.find_my_matzip.databinding.ItemDialogBinding


//
//class FollowerViewHolder(val binding: ItemDialogBinding) : RecyclerView.ViewHolder(binding.root)

// FollowerAdapter 클래스는 RecyclerView의 어댑터로, 데이터를 받아와 화면에 표시하는 역할을 합니다.
class FollowingAdapter(val context: Context, var datas: List<String>?, private val listener: OnFollowingClickListener) :
    RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    // ViewHolder를 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val binding = ItemDialogBinding.inflate(LayoutInflater.from(context), parent, false)
        return FollowingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        // datas가 null이면 0을 반환, 그렇지 않으면 datas의 크기를 반환
        return datas?.size ?: 0
    }


    // ViewHolder에 데이터를 바인딩하는 함수
    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        // 현재 위치의 데이터를 가져와서 ViewHolder에 바인딩
        val item = datas?.get(position)
        holder.bind(item)
    }

    // 내부 클래스로 정의된 FollowerViewHolder는 각 아이템 뷰의 구성요소를 관리합니다.
    inner class FollowingViewHolder(val binding: ItemDialogBinding) : RecyclerView.ViewHolder(binding.root) {
        // 아이템 뷰에 데이터를 바인딩하는 함수
        fun bind(item: String?) {
            // 뷰 바인딩 객체를 통해 아이템의 텍스트 설정
            binding.dialogUserid.text = item
            // 아이템 뷰를 클릭했을 때의 동작 정의
            binding.root.setOnClickListener {
                // 클릭 시 리스너의 onFollowClick 메서드 호출
                listener.onFollowingClick(item ?: "")
            }
        }
    }

//    FollowerAdapter의 OnFollowerClickListener에서 팔로워를 클릭했을 때 호출되는 onFollowClick 메서드에서 해당 유저의 프로필로 이동하는 코드를 추가
    interface OnFollowingClickListener {
        fun onFollowingClick(item: String)
    }
}
