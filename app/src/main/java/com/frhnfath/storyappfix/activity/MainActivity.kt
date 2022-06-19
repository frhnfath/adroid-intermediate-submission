package com.frhnfath.storyappfix.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.frhnfath.storyappfix.R
import com.frhnfath.storyappfix.data.AllStoriesResponse
import com.frhnfath.storyappfix.data.StoryModel
import com.frhnfath.storyappfix.databinding.ActivityMainBinding
import com.frhnfath.storyappfix.network.ApiConfig
import com.frhnfath.storyappfix.session.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreferences: UserPreferences
    private lateinit var adapter: ListStoriesAdapter
    private var listStories = ArrayList<StoryModel>()

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

        showRecyclerList()
        getStories()

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showRecyclerList() {
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        val listStoriesAdapter = ListStoriesAdapter(listStories)
        binding.rvStories.adapter = listStoriesAdapter
    }

    private fun getStories() {
        showLoading(true)
        mUserPreferences = UserPreferences(this)
        val client = ApiConfig.getApiService().getAllStories("Bearer " + mUserPreferences.getUser().token)
        client.enqueue(object : Callback<AllStoriesResponse> {
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    for (i in responseBody.listStory) {
                        val story = StoryModel(i.name, i.photoUrl, i.description, i.createdAt)
                        listStories.add(story)
                    }
                    adapter = ListStoriesAdapter(listStories)
                    binding.rvStories.adapter = adapter
                    Log.e("Load Stories Success", "onResponse: ")
                } else {
                    Log.e("Load Stories Failed", "onResponse: ${response.message()}")
                    Toast.makeText(applicationContext, "Failed to load stories", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                showLoading(false)
                Log.e("Load Stories Failed", "onResponse: ${t.message}" )
                Toast.makeText(applicationContext, "Failed to load stories", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showLoading(state: Boolean) {
        if (state) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}