package com.example.find_my_matzip.utiles

import android.content.Context
import android.content.SharedPreferences

//로그인 SharedPreferences관리
object SharedPreferencesManager{

    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    //SharedPreferences 싱글톤 객체 생성 (auto_login)
    fun init(context: Context){
        prefs = context.getSharedPreferences("auto_login", Context.MODE_PRIVATE)
        editor = prefs?.edit()
        //어플 시작시 초기화
        //setLoginInfo("","","")
    }


    //로그인 정보 저장
    //사용 예시 : SharedPreferencesManager.setLoginInfo("id" , "pw");
    fun setLoginInfo(id: String?, pw: String?,token:String?) {
        editor?.putString("id", id)
        editor?.putString("pw", pw)
        editor?.putString("token", token)
        editor?.apply()
    }


    //로그인 정보 불러오기
    // 사용 예시
    //  Map<String, String> loginInfo = SharedPreferencesManager.getLoginInfo();
    //  if (!loginInfo.isEmpty()){
    //      String email    = loginInfo.get("email");
    //      String password = loginInfo.get("password");
    //  } -> 후에 서버에 로그인 리퀘스트 하면 자동 로그인 가능
    fun getLoginInfo(): Map<String, String?>? {
        val loginInfo: MutableMap<String, String?> = HashMap()
        val id = prefs?.getString("id", "")
        val pw = prefs?.getString("pw", "")
        val token = prefs?.getString("token","")
        loginInfo["id"] = id
        loginInfo["pw"] = pw
        loginInfo["token"] = token
        return loginInfo
    }

    //로그인 정보 삭제 (로그아웃, 회원 탈퇴시)
    //사용 예시 : clearPreferences()
    fun clearPreferences() {
        editor?.clear()
        editor?.apply() //비동기 처리
    }


    //원하는 값 하나만 가져오기
    //사용 예시 : getString(this, id, "") => "유저아이디"
    fun getString(key:String,defValue:String):String{
        return prefs?.getString(key, defValue).toString()
    }


}

