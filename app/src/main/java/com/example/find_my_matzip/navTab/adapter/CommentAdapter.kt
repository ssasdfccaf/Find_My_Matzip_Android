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
import com.example.find_my_matzip.model.CommentDto
import com.example.find_my_matzip.navTab.navTabFragment.CommentFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

// 뷰와 데이터 연결 한다.

class CommentViewHoleder2(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
    val recyclerViewChildren: RecyclerView = binding.recyclerViewChildren
}


class CommentAdapter2(val context: CommentFragment, var datas: List<CommentDto>) :
    RecyclerView.Adapter<CommentViewHoleder2>() {
    // "답글달기" 버튼 클릭을 처리하기 위한 콜백

    // "답글달기" 버튼 클릭을 처리하기 위한 콜백
    var onReplyClick: ((CommentDto, Int, Long) -> Unit)? = null

    // 데이터 바인딩을 사용한 ViewHolder 인스턴스 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHoleder2 {
        // XML 레이아웃을 데이터 바인딩을 통해 인플레이트
        val binding = ItemCommentBinding.inflate(

            LayoutInflater.from(parent.context), parent, false
        )
        return CommentViewHoleder2(binding)
    }


    // 데이터 세트의 총 아이템 수 반환
    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    // 데이터를 뷰에 바인딩하고 댓글 아이템 레이아웃을 설정
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommentViewHoleder2, position: Int) {
        val binding = holder.binding
        val item = datas?.get(position)

        // 아이템이 null이면 에러 로그 출력
        Log.e("CommentViewHoleder2", "Item is null at position $position")

        binding.commentWriter?.text = item?.commentWriter
        binding.commentContents.text = item?.commentContents
        binding.commentCreatedTime.text = item?.commentCreatedTime.toString()


        // "얼마 전" 형식의 텍스트 계산 및 설정
        val commentTimeAgo = getTimeAgoText(item?.commentCreatedTime)
        binding.commentCreatedTime.text = commentTimeAgo

        // 댓글이 부모 댓글인지 확인
        val isParentComment = item?.depth == 0

        // 댓글 깊이에 따라 들여쓰기 설정
        val indentSize = context.resources.getDimensionPixelSize(R.dimen.comment_indent)
        val layoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams

        layoutParams.marginStart = if (isParentComment) {
            indentSize * item?.depth!!
        } else {
            indentSize * (item?.depth!! + 1)
        }

        binding.root.layoutParams = layoutParams

        // 자식 댓글이 있으면 내부 RecyclerView 초기화
        if (item?.children?.isNotEmpty() == true) {
            val innerAdapter = CommentAdapter2(context, item.children)
            binding.recyclerViewChildren.layoutManager =
                LinearLayoutManager(context?.requireContext()?.applicationContext)
            binding.recyclerViewChildren.adapter = innerAdapter
        } else {
            // 자식 댓글이 없으면 내부 RecyclerView 숨김
            binding.recyclerViewChildren.visibility = View.GONE
            // "답글달기" 버튼에 대한 클릭 리스너 설정
        }

        holder.binding.saveReply.setOnClickListener {
            // 댓글 답글 버튼이 클릭되었을 때, 콜백 함수 호출
            val parentComment = datas[position] // 부모 댓글
            onReplyClick?.invoke(parentComment, parentComment.depth, parentComment.commentId)

            // 대댓글이 있을 경우 해당 대댓글에 대한 답글 콜백도 호출
            if (item?.children?.isNotEmpty() == true) {
                for (childComment in item.children) {
                    onReplyClick?.invoke(childComment, childComment.depth, parentComment.commentId)
                }
            }
        }

        // "답글달기" 버튼에 대한 클릭 리스너 설정
//        binding.saveReply.setOnClickListener {
//            // 댓글 답글 버튼이 클릭되었을 때, 콜백 함수 호출
//            val parentComment = datas[position] // 부모 댓글
//            onReplyClick?.invoke(parentComment, null, parentComment.commentId)
//
//            // 대댓글이 있을 경우 해당 대댓글에 대한 답글 콜백도 호출
//            if (item?.children?.isNotEmpty() == true) {
//                for (childComment in item.children) {
//                    onReplyClick?.invoke(item, childComment, parentComment.commentId)
//                }
//            }
        }}
    // 댓글 작성 시간을 기반으로 "얼마 전" 형식의 텍스트 계산
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
