package com.frhnfath.storyappfix.activity

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frhnfath.storyappfix.R
import com.frhnfath.storyappfix.data.StoryModel

class ListStoriesAdapter(private val listStories: ArrayList<StoryModel>): RecyclerView.Adapter<ListStoriesAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.story_image)
        var username: TextView = itemView.findViewById(R.id.story_username)
        var description: TextView = itemView.findViewById(R.id.story_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(listStories[position].image).into(holder.imgPhoto)
        holder.username.text = listStories[position].name
        holder.description.text = listStories[position].description

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "You chose " + listStories[position].name + "'s Story", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.STORY, listStories[position])
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listStories.size
}