package com.cahyono.tokoonline.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.cahyono.tokoonline.model.User
import com.google.gson.Gson

class SharedPref(activity: Activity) {

    val login = "login"

    val nama = "name"
    val phone = "phone"
    val email = "email"

    val user = "user"

    private lateinit var sharedPreferences: SharedPreferences
    val mypref = "MY_PREF"

    init {
        sharedPreferences = activity.getSharedPreferences(mypref, Context.MODE_PRIVATE)
    }

    fun setStatusLogin(status:Boolean) {
        sharedPreferences.edit().putBoolean(login, status).apply()
    }

    fun setUser(value: User){
        val data:String = Gson().toJson(value, User::class.java)
        sharedPreferences.edit().putString(user,data).apply()
    }

    fun getUser() : User?{
        val data:String = sharedPreferences.getString(user, null) ?: return null
        return  Gson().fromJson<User>(data, User::class.java)
    }

    fun getStatusLogin():Boolean {
        return sharedPreferences.getBoolean(login, false)
    }

    fun setString(key: String, value: String){

        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }
}