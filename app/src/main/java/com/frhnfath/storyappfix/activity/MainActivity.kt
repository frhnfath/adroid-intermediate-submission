package com.frhnfath.storyappfix.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.frhnfath.storyappfix.viewmodel.MainViewModel
import com.frhnfath.storyappfix.R
import com.frhnfath.storyappfix.viewmodel.ViewModelFactory
import com.frhnfath.storyappfix.adapter.ListStoriesAdapter
import com.frhnfath.storyappfix.adapter.LoadingStateAdapter
import com.frhnfath.storyappfix.databinding.ActivityMainBinding
import com.frhnfath.storyappfix.session.UserPreferences

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreferences: UserPreferences
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                mUserPreferences.deleteUser()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mUserPreferences = UserPreferences(this)

        Log.d("login state", "onCreate: ${mUserPreferences.getUser().isLogin}")
        Log.d("login token", "onCreate: ${mUserPreferences.getUser().token}")

        binding.rvStories.layoutManager = LinearLayoutManager(this)

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        getStories()
    }

    private fun getStories() {
        val adapter = ListStoriesAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(footer = LoadingStateAdapter {
            adapter.retry()
        })
        Log.d("Token Story", "getStories: ${mUserPreferences.getUser().token}")
        mainViewModel.story("Bearer " + mUserPreferences.getUser().token).observe(this) {
            Log.d("lifecycle", "getStories: $it")
            adapter.submitData(lifecycle, it)
            Log.d(TAG, "getStories: working")
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}