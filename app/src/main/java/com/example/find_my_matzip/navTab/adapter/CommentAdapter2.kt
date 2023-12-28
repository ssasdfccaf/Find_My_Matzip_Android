package com.example.find_my_matzip.navTab.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.ItemCommentBinding
import com.example.find_my_matzip.model.Content
import com.example.find_my_matzip.navTab.navTabFragment.CommentFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

// 뷰와 데이터 연결 한다.

class CommentViewHoleder2(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)

class CommentAdapter2(val context: CommentFragment, var datas: List<Content>?) :
    RecyclerView.Adapter<CommentViewHoleder2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHoleder2 {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CommentViewHoleder2(binding)
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommentViewHoleder2, position: Int) {
        val binding = holder.binding


        val item = datas?.get(position)

        Log.e("BoardRecyclerAdapter", "Item is null at position $position")

        binding.commentWriter?.text = item?.commentWriter
        binding.commentContents.text = item?.commentContents
        binding.commentCreatedTime.text = item?.commentCreatedTime.toString()

        val commentTimeAgo = getTimeAgoText(item?.commentCreatedTime)
        binding.commentCreatedTime.text = commentTimeAgo
        val isParentComment = item?.depth == 0
        val indentSize = context.resources.getDimensionPixelSize(R.dimen.comment_indent)
        val layoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams

        layoutParams.marginStart = if (isParentComment) {
            indentSize * item?.depth!!
        } else {
            indentSize * (item?.depth!! + 1)
        }

        binding.root.layoutParams = layoutParams

        // 대댓글이 있을 때만 내부 RecyclerView를 초기화
        if (item?.children?.isNotEmpty() == true) {
            val innerAdapter = CommentAdapter2(context, item.children)
            binding.recyclerViewChildren.layoutManager = LinearLayoutManager(context?.requireContext()?.applicationContext)
            binding.recyclerViewChildren.adapter = innerAdapter
        } else {
            // 대댓글이 없으면 내부 RecyclerView를 숨김
            binding.recyclerViewChildren.visibility = View.GONE
        }}

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimeAgoText(commentTime: String?): String {
        val commentDateTime = LocalDateTime.parse(commentTime, DateTimeFormatter.ISO_DATE_TIME)
        val now = LocalDateTime.now()
        val duration = Duration.between(commentDateTime, now)

        return when {
            duration.toMinutes() < 1 -> "Just now"
            duration.toHours() < 1 -> "${duration.toMinutes()} minutes ago"
            duration.toDays() < 1 -> "${duration.toHours()} hours ago"
            else -> "${duration.toDays()} days ago"
        }
    }
}
