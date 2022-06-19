package com.frhnfath.storyappfix.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.frhnfath.storyappfix.data.UserModel
import com.frhnfath.storyappfix.databinding.ActivitySplashscreenBinding
import com.frhnfath.storyappfix.session.UserPreferences

class SplashscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashscreenBinding
    private lateinit var mUserPreferences: UserPreferences
    private var isLogin = false
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        mUserPreferences = UserPreferences(this)
        getExistingPreference()

        Handler(mainLooper).postDelayed({
                if (isLogin) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }, 3000)
    }

    private fun getExistingPreference() {
        userModel = mUserPreferences.getUser()
        if (userModel.isLogin) isLogin = true
        Log.d("state", "${userModel.isLogin}")
        Log.d("SplashScreenActivity", "getExistingPreference: ${userModel.token}")
    }
}