package com.frhnfath.storyappfix.di

import android.content.Context
import com.frhnfath.storyappfix.data.StoryDatabase
import com.frhnfath.storyappfix.network.ApiConfig
import com.frhnfath.storyappfix.paging.StoryRepository

object Injection {
    fun provideRepo(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}