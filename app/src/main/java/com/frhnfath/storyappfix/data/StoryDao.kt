package com.frhnfath.storyappfix.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<ListStoryItem>)

    @Query("SELECT * FROM list_stories")
    fun getAllStories(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM list_stories")
    suspend fun deleteAll()
}