package com.frhnfath.storyappfix.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frhnfath.storyappfix.activity.DetailActivity
import com.frhnfath.storyappfix.data.ListStoryItem
import com.frhnfath.storyappfix.data.StoryModel
import com.frhnfath.storyappfix.databinding.StoryItemBinding

class ListStoriesAdapter: PagingDataAdapter<ListStoryItem, ListStoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
    }

    class ListViewHolder(private val binding: StoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        // bind data to views of application
        fun bind(story: ListStoryItem) {
            with(binding) {
                Glide.with(storyImage).load(story.photoUrl).into(storyImage)
                storyUsername.text = story.name
                storyDescription.text = story.description
                // if clicked, go to details with
                itemView.setOnClickListener {
                    onClick(story)
                }
            }
        }
        private fun onClick(desc: ListStoryItem) {
            // assign data to StoryModel so they can be sent using putExtra
            val story = StoryModel(desc.name, desc.photoUrl, desc.description)
            Toast.makeText(itemView.context, "You chose " + story.name + "s Story", Toast.LENGTH_SHORT).show()
            val intent = Intent(itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.STORY, story)
            itemView.context.startActivity(intent)
        }
    }

    // add diff_callback
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}