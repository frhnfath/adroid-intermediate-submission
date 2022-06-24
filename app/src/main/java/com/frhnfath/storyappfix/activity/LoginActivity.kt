package com.frhnfath.storyappfix.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.frhnfath.storyappfix.data.LoginResponse
import com.frhnfath.storyappfix.databinding.ActivityLoginBinding
import com.frhnfath.storyappfix.network.ApiConfig
import com.frhnfath.storyappfix.session.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mUserPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMyButtonEnabled()
        playAnimation()

        mUserPreferences = UserPreferences(this)
        Log.d("login state", "onCreate: ${mUserPreferences.getUser().isLogin}")
        Log.d("login token", "onCreate: ${mUserPreferences.getUser().token}")

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                setMyButtonEnabled()
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnabled()
            }

            override fun afterTextChanged(s: Editable) {
                // Do Nothing
            }
        })

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            Log.d("Login", "onCreate: working")
            finish()
        }
    }

    private fun userCredentials() : Array<String> {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        return arrayOf(email, password)
    }

    private fun postLogin(email: String, password: String){
        val userPreference = UserPreferences(this)
        showLoading(true)
        val client = ApiConfig.getApiService().postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    userPreference.setUser(responseBody.loginResult.token)
                    Toast.makeText(applicationContext,"Signed in successfully", Toast.LENGTH_SHORT).show()
                    Log.d("Token", "onResponse: ${responseBody.loginResult.token}")
                    moveActivity()
                } else {
                    Toast.makeText(applicationContext, "Couldn't sign in", Toast.LENGTH_SHORT).show()
                    Log.d("LoginActivity", "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Log.e("LoginActivity", "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun moveActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvSignin, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val tvRegister = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)
        val registerButton = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)

        val togetherEmail = AnimatorSet().apply {
            playTogether(tvEmail, edtEmail)
        }

        val togetherPassword = AnimatorSet().apply {
            playTogether(tvPassword, edtPassword)
        }

        AnimatorSet().apply {
            playSequentially(title, togetherEmail, togetherPassword, loginButton, tvRegister, registerButton)
            start()
        }
    }

    private fun setMyButtonEnabled() {
        val result = binding.edtPassword.text
        binding.loginButton.isEnabled = result != null && result.toString().length > 5
        if (result.toString().length < 6) {
            binding.loginButton.setOnClickListener { Toast.makeText(this@LoginActivity, "Password Must Have At Least 6 Characters", Toast.LENGTH_SHORT).show()}
        } else {
            binding.loginButton.setOnClickListener {
                val user = userCredentials()
                postLogin(user[0], user[1])
            }
        }
    }
}