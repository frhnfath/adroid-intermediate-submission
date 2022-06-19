package com.frhnfath.storyappfix.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.frhnfath.storyappfix.data.StoryModel
import com.frhnfath.storyappfix.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<StoryModel>(STORY)
        if (story != null) {
            binding.tvDetailUsername.text = story.name.toString()
            binding.tvDetailDescription.text = story.description.toString()
            Glide.with(this).load(story.image).into(binding.imageDetail)
        }
    }

    companion object {
        const val STORY = "story"
    }
}