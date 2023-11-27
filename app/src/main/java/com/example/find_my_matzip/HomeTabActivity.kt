package com.example.find_my_matzip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.find_my_matzip.navTab.navTabFragment.HomeFragment
import com.example.find_my_matzip.navTab.navTabFragment.MapFragment
import com.example.find_my_matzip.navTab.navTabFragment.MyPageFragment
import com.example.find_my_matzip.navTab.navTabFragment.RankingFragment
import com.example.find_my_matzip.navTab.navTabFragment.SearchReviewFragment
import com.example.find_my_matzip.databinding.ActivityHomeTabBinding
import com.example.find_my_matzip.navTab.navTabFragment.RestaurantFragment
import com.example.find_my_matzip.utiles.SharedPreferencesManager

class HomeTabActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeTabBinding
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 , 업버튼
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toggle = ActionBarDrawerToggle(
            this@HomeTabActivity, binding.drawer, R.string.open, R.string.close
        )

        // 드로워 열어주기
        toggle.syncState()

        // 탭 레이아웃
        val tabLayout = binding.bottomNavigationView

        tabLayout.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tab1 -> {replaceFragment(HomeFragment())}
                R.id.tab2 -> {replaceFragment(RestaurantFragment())}
                R.id.tab3 -> {replaceFragment(MapFragment())}
                R.id.tab4 -> {replaceFragment(RankingFragment())}
                R.id.tab5 -> {replaceFragment(MyPageFragment())}
                else -> false
            }
            return@setOnNavigationItemSelectedListener true
        }
        replaceFragment(HomeFragment())

        //Drawer 네비게이션
        binding.mainDrawerView.setNavigationItemSelectedListener {
            if (it.title == "종료"){
                val builder = AlertDialog.Builder(this@HomeTabActivity)
                builder.setTitle("Exit?")
                builder.setMessage("앱을 종료하시겠습니까?")
                builder.setNegativeButton("아니오") { dialog, which ->
                    // 아무 작업도 수행하지 않음
                }
                builder.setPositiveButton("예") { dialog, which ->
                    // stack 전부 지우고 앱 종료
                    finishAffinity()
                }
                builder.show()
            }
            else if (it.title == "로그아웃") {
                Toast.makeText(this@HomeTabActivity,"로그아웃 화면 이동", Toast.LENGTH_SHORT).show()

                //로그인 정보 지우기
                SharedPreferencesManager.clearPreferences()

                // BackStack의 fragment전부 삭제
                clearBackStack()

                //로그인 화면으로 이동
                val intent = Intent(this@HomeTabActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            else if (it.title == "회원 탈퇴") {
                val builder = AlertDialog.Builder(this@HomeTabActivity)
                builder.setTitle("회원 탈퇴")
                builder.setMessage("정말 탈퇴하시겠습니까???????")
                builder.setNegativeButton("아니오") { dialog, which ->
                    // 아무 작업도 수행하지 않음
                }
                builder.setPositiveButton("예") { dialog, which ->

                    // 회원 탈퇴 전 본인 확인
                    val passwordBuilder = AlertDialog.Builder(this@HomeTabActivity)
                    builder.setTitle("본인 확인")
                    builder.setMessage("비밀번호를 입력해주세요")

                    val inflater = layoutInflater
                    val passwordDialogView = inflater.inflate(R.layout.dialog_password, null)
                    passwordBuilder.setView(passwordDialogView)

                    val passwordEditText = passwordDialogView.findViewById<EditText>(R.id.editTextPassword)

                    passwordBuilder.setPositiveButton("확인") { dialog, which ->
                        
                        val enteredPassword = passwordEditText.text.toString()

                        //비밀번호 확인
                        if (isCorrectPassword(enteredPassword)) {
                            //회원정보 삭제 로직 추가
                            //1.DB에서 DATA 삭제
                            //2.firebase에서 이미지삭제


                            Toast.makeText(this@HomeTabActivity, "비밀번호 확인 성공", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@HomeTabActivity, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                        }
                    }

                    passwordBuilder.setNegativeButton("취소") { dialog, which ->
                        // Cancelled password input
                        Toast.makeText(this@HomeTabActivity, "비밀번호 입력이 취소되었습니다", Toast.LENGTH_SHORT).show()
                    }

                    builder.show()


                }
                builder.show()
            }
            true
        }

    }

    //비밀번호 확인
    private fun isCorrectPassword(enteredPassword: String): Boolean {
        val correctPw = SharedPreferencesManager.getString("pw","")
        return correctPw == enteredPassword
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //backstack의 fragment전부 삭제
    private fun clearBackStack() {
        val fragmentManager = supportFragmentManager
        val count = fragmentManager.backStackEntryCount
        for (i in 0 until count) {
            fragmentManager.popBackStack()
        }
    }
}