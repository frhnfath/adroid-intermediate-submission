package com.frhnfath.storyappfix.session

import android.content.Context
import android.util.Log
import com.frhnfath.storyappfix.data.UserModel

internal class UserPreferences(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = "token"
        private const val LOGIN = "islogin"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: String) {
        val editor = preferences.edit()
        editor.putString(TOKEN, value)
        editor.putBoolean(LOGIN, true)
        editor.apply()
    }

    fun getUser(): UserModel {
        val model = UserModel()
        model.token = preferences.getString(TOKEN, "").toString()
        model.isLogin = preferences.getBoolean(LOGIN, false)
        return model
    }

    fun deleteUser() {
        preferences.edit().remove("token").apply()
        preferences.edit().putBoolean("islogin", false).apply()
    }
}