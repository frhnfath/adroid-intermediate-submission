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
import com.frhnfath.storyappfix.data.UserResponse
import com.frhnfath.storyappfix.databinding.ActivityRegisterBinding
import com.frhnfath.storyappfix.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMyButtonEnabled()
        playAnimation()

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                setMyButtonEnabled()
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, before: Int, count: Int) {
                setMyButtonEnabled()
            }

            override fun afterTextChanged(p0: Editable?) {
                // Do nothing
            }
        })

        binding.signInButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun userCredentials(): Array<String> {
        val username = binding.edtUsername.text.toString()
        if (username.isEmpty()) {
            binding.edtUsername.error = "Username is required"
            binding.edtUsername.requestFocus()
        }
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        return arrayOf(username, email, password)
    }

    private fun postRegister(username: String, email: String, password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().postRegister(username, email, password)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Toast.makeText(applicationContext, "Registered. Please sign in", Toast.LENGTH_SHORT).show()
                    moveActivity()
                } else {
                    Toast.makeText(applicationContext, "Couldn't register", Toast.LENGTH_SHORT).show()
                    Log.d("RegisterActivity", "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showLoading(false)
                Log.e("Fail", "onFailure: ${t.message}")
            }
        })
    }

    private fun moveActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)
        val tvUsername = ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 1f).setDuration(500)
        val edtUsername = ObjectAnimator.ofFloat(binding.edtUsername, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val registerButton = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.signInButton, View.ALPHA, 1f).setDuration(500)

        val togetherUsername = AnimatorSet().apply {
            playTogether(tvUsername, edtUsername)
        }

        val togetherEmail = AnimatorSet().apply {
            playTogether(tvEmail, edtEmail)
        }

        val togetherPassword = AnimatorSet().apply {
            playTogether(tvPassword, edtPassword)
        }

        AnimatorSet().apply {
            playSequentially(title, togetherUsername, togetherEmail, togetherPassword, registerButton, tvLogin, loginButton)
            start()
        }
    }

    private fun setMyButtonEnabled() {
        val result = binding.edtPassword.text
        binding.registerButton.isEnabled = result != null && result.toString().length > 5
        if (result.toString().length < 6) {
            binding.registerButton.setOnClickListener { Toast.makeText(this@RegisterActivity, "Password Must Have 6 Character Minimum", Toast.LENGTH_SHORT).show()}
        } else {
            binding.registerButton.setOnClickListener {
                val user = userCredentials()
                postRegister(user[0], user[1], user[2])
            }
        }
    }
}