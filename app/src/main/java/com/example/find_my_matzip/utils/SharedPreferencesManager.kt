package com.example.find_my_matzip.utils

import android.content.Context
import android.content.SharedPreferences

//로그인 SharedPreferences관리
class SharedPreferencesManager {

    //companion object : java의 static과 같은 의미
    //singleton pattern으로 구성 (어디서든 접근 가능, 해당 인스턴스는 오직 하나)
    companion object {
        private const val PREFERENCES_NAME = "auto_login"

        //SharedPreferences 불러오기
        private fun getPreferences(mContext: Context): SharedPreferences {
            return mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }

        //로그인 정보 저장
        //사용 예시 : SharedPreferencesManager.setLoginInfo(this, "id" , "pw");
        fun setLoginInfo(context: Context, id: String?, pw: String?) {
            val prefs = getPreferences(context)
            val editor = prefs.edit()
            editor.putString("id", id)
            editor.putString("pw", pw)
            editor.apply()
        }

        //로그인 정보 불러오기
        // 사용 예시
        //  Map<String, String> loginInfo = SharedPreferencesManager.getLoginInfo(this);
        //  if (!loginInfo.isEmpty()){
        //      String email    = loginInfo.get("email");
        //      String password = loginInfo.get("password");
        //  } -> 후에 서버에 로그인 리퀘스트 하면 자동 로그인 가능
        fun getLoginInfo(context: Context): Map<String, String?>? {
            val prefs = getPreferences(context)
            val loginInfo: MutableMap<String, String?> = HashMap()
            val id = prefs.getString("id", "")
            val pw = prefs.getString("pw", "")
            loginInfo["id"] = id
            loginInfo["pw"] = pw
            return loginInfo
        }

        //로그인 정보 삭제 (로그아웃, 회원 탈퇴시)
        //사용 예시 : clearPreferences(this)
        fun clearPreferences(context: Context) {
            val prefs = getPreferences(context)
            val editor = prefs.edit()
            editor.clear()
            editor.apply() //비동기 처리
        }
    }
}