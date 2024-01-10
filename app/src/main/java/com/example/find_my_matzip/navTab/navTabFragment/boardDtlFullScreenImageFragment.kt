package com.example.find_my_matzip.navTab.navTabFragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.find_my_matzip.R
import com.example.find_my_matzip.navTab.adapter.FullScreenImageAdapter
import com.example.find_my_matzip.utils.BottomBarVisibilityListener


class boardDtlFullScreenImageFragment : Fragment() {

    private var imageUrls: ArrayList<String>? = null
    private var selectedPosition: Int = 0
    private lateinit var darkView: View

    private var bottomBarVisibilityListener: BottomBarVisibilityListener? = null

    fun setBottomBarVisibilityListener(listener: BottomBarVisibilityListener) {
        Log.d("setBottomBarVisibilityListener", "setBottomBarVisibilityListener 호출됨")
        bottomBarVisibilityListener = listener
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomBarVisibilityListener) {
            setBottomBarVisibilityListener(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrls = it.getStringArrayList("image_urls")
            selectedPosition = it.getInt("selected_position", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_board_dtl_full_screen_image, container, false)
        darkView = createDarkView() // 어두운 배경을 생성하고 저장
        view.post {
            val parentLayout = view.parent as? View
            val parentLayoutId = parentLayout?.id ?: View.NO_ID
            Log.d("ParentLayoutID", "Parent layout ID: $parentLayoutId")
        }




        val viewPager: ViewPager2 = view.findViewById(R.id.DtlFullScreen)
        val adapter = FullScreenImageAdapter(requireContext(), imageUrls ?: arrayListOf())
        viewPager.adapter = adapter


        Log.d("KKT","imageUrls : $imageUrls")

        viewPager.post {
            viewPager.currentItem = selectedPosition
        }
        return view
    }

    private fun createDarkView(): View {
        val darkView = View(requireContext())
        darkView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        darkView.setBackgroundColor(Color.parseColor("#FF000000")) // 어두운 배경색 지정
        return darkView
    }

    private var originalBackground: Drawable? = null

    override fun onStart() {
        super.onStart()
        Log.d("KKT","onStart")
        addDarkBackgroundToRootView() // Fragment가 화면에 보일 때 루트 뷰만 어둡게 처리
        // 어딘가에서 하단 바를 숨길 때
        bottomBarVisibilityListener?.hideBottomBar()
    }

    override fun onStop() {
        super.onStop()
        Log.d("KKT","onStop")
        removeDarkBackgroundFromRootView() // Fragment가 화면에서 사라질 때 루트 뷰에서 어둡게 처리된 배경을 제거
        // 어딘가에서 하단 바를 보일 때
        bottomBarVisibilityListener?.showBottomBar()
    }

    private fun addDarkBackgroundToRootView() {
        val rootView = view?.rootView
        originalBackground = rootView?.background
        rootView?.background = darkView.background
    }

    private fun removeDarkBackgroundFromRootView() {
        val rootView = view?.rootView
        rootView?.background = originalBackground
    }

}
