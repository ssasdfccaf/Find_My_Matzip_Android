package com.example.find_my_matzip.navTab.adapter
import android.os.Build
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
interface CommentAdapterListener {
    fun onReplyClick(comment: CommentDto, boardId: Long)
}

class CommentViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
    val recyclerViewChildren: RecyclerView = binding.recyclerViewChildren
}

class CommentAdapter(
    val context: CommentFragment, // Pass CommentFragment instance
    val boardId: Long,
    var datas: List<CommentDto>,
    val listener: CommentAdapterListener? = null
) : RecyclerView.Adapter<CommentViewHolder>() {

    var onReplyClick: ((CommentDto, Long) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder{
        // XML 레이아웃을 데이터 바인딩을 통해 인플레이트
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return CommentViewHolder(binding)
    }


    // 데이터 세트의 총 아이템 수 반환
    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    // 데이터를 뷰에 바인딩하고 댓글 아이템 레이아웃을 설정
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val binding = holder.binding
        val item = datas?.get(position)

        binding.commentWriter?.text = item?.commentWriter
        binding.commentContents.text = item?.commentContents
        binding.commentCreatedTime.text = item?.commentCreatedTime.toString()

        val commentTimeAgo = getTimeAgoText(item?.commentCreatedTime)
        binding.commentCreatedTime.text = commentTimeAgo

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
            val innerAdapter = CommentAdapter(context, boardId, item.children, listener)
            binding.recyclerViewChildren.layoutManager =
                LinearLayoutManager(context?.requireContext()?.applicationContext)
            binding.recyclerViewChildren.adapter = innerAdapter
        } else {
            // 자식 댓글이 없으면 내부 RecyclerView 숨김
            binding.recyclerViewChildren.visibility = View.GONE
        }
        binding.saveReply.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val parentComment = datas[position]
                listener?.onReplyClick(parentComment, boardId)
                context.showReplyDialog(parentComment, boardId)
            }
        }
    }
}


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



