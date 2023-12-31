package com.example.find_my_matzip.utiles

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date

//로그인 SharedPreferences관리
object SharedPreferencesManager{

    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    //SharedPreferences 싱글톤 객체 생성 (auto_login)
    fun init(context: Context){
        prefs = context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
        //prefs = context.getSharedPreferences("auto_login", Context.MODE_PRIVATE)
        editor = prefs?.edit()
    }


    //로그인 정보 저장
    //사용 예시 : SharedPreferencesManager.setLoginInfo("id" , "pw");
    fun setLoginInfo(id: String?, pw: String?,token:String?,autoLogin:Boolean) {
        editor?.putString("id", id)
        editor?.putString("pw", pw)
        editor?.putString("token", token)
        editor?.putBoolean("autoLogin", autoLogin)
        editor?.apply()
    }

    //로그인 관련 모든 정보 조회
    fun getLoginInfo(): Map<String, Any?>? {
        val loginInfo: MutableMap<String, Any?> = HashMap()
        val id = prefs?.getString("id", "")
        val pw = prefs?.getString("pw", "")
        val token = prefs?.getString("token","")
        val autoLogin = prefs?.getBoolean("autoLogin",false)
        loginInfo["id"] = id
        loginInfo["pw"] = pw
        loginInfo["token"] = token
        loginInfo["autoLogin"] = autoLogin
        return loginInfo
    }

    //로그인 정보 삭제 (로그아웃) ->id, 검색기록 제외 모두 삭제
    //사용 예시 : clearPreferences()
    fun clearLoginPreferences() {
        editor?.remove("pw")
        editor?.remove("token")
        editor?.remove("autoLogin")
        editor?.apply()
    }


    // 검색 기록 저장
    fun saveSearchHistory(text: String){
        if(text.isNotEmpty()){
            val currentDate = SimpleDateFormat("yyyy.MM.dd").format(Date())
            val historyItem = "$text,$currentDate"

            //기존의 list조회
            val existingSet = prefs?.getStringSet("search_history", HashSet()) ?: HashSet()
            existingSet.add(historyItem)

            //새로운 text추가된 list저장
            editor?.putStringSet("search_history", existingSet)
            editor?.apply()
        }
    }

    //특정 검색어 삭제
    fun deleteSearchHistory(text: String){
        //기존 list
        val existingSet = prefs?.getStringSet("search_history", HashSet() ) ?: HashSet()

        // 삭제할 단어 찾기
        val itemsToDelete = existingSet.filter { it.startsWith("$text,") }

        // 기존 list에서 삭제할 단어 제거
        existingSet.removeAll(itemsToDelete)

        //변경된 list적용
        editor?.putStringSet("search_history", existingSet)
        editor?.apply()
    }


    // 검색 기록 조회
    fun getSearchHistory(): Set<String>? {
        return prefs?.getStringSet("search_history", HashSet())
    }

    // 검색 기록 삭제
    fun clearSearchPreferences(){
        editor?.remove("search_history")
        editor?.apply()
    }


    //모든 기록 삭제(회원 탈퇴)
    fun clearAllPreferences() {
        editor?.clear()
        editor?.apply()
    }


    //원하는 값 하나만 가져오기
    //사용 예시 : getString("id", "") => "유저아이디"
    fun getString(key:String,defValue:String):String{
        return prefs?.getString(key, defValue).toString()
    }

    //boolean형태의 값(autoLogin)가져올때 사용
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return prefs?.getBoolean(key, defValue) ?: defValue
    }


}





