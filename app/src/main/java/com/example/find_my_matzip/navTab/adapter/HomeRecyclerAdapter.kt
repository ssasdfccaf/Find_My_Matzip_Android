package com.example.find_my_matzip.navTab.adapter
import android.content.ClipData.Item
import com.example.find_my_matzip.databinding.HomeFragmentItemBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.find_my_matzip.R
import com.example.find_my_matzip.databinding.ItemMainboardBinding
import com.example.find_my_matzip.model.MainBoardDto
import com.example.find_my_matzip.navTab.navTabFragment.HomeFragment
import com.example.find_my_matzip.navTab.navTabFragment.boardDtlFragment

class MainBoardViewHolder(val binding : ItemMainboardBinding) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.allItem.setOnClickListener {
            val fragment = boardDtlFragment()
            // Fragment를 추가하기 위해 context를 사용
            val fragmentManager = (binding.root.context as FragmentActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}

class HomeRecyclerAdapter
    (val context: HomeFragment,
     val  datas : List<MainBoardDto>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemMainboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainBoardViewHolder(itemBinding)
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

        // 아이템 클릭 시 boardDtlFragment로 이동(정보전달)
//        binding.root.setOnClickListener {
//            // 클릭한 아이템의 정보를 전달하고 boardDtlFragment로 이동하는 코드
//            val selectedItem = datas?.get(position)
//            val fragment =
//                boardDtlFragment.newInstance(selectedItem) // boardDtlFragment.newInstance에 선택한 아이템을 전달할 수 있도록 구현되어 있어야 합니다.
//
//            // FragmentManager를 사용하여 이동
//            val transaction =
//                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragmentContainer, fragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
        }
    }

