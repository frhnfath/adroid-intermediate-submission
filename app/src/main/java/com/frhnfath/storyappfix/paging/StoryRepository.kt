package com.frhnfath.storyappfix.paging

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.frhnfath.storyappfix.data.ListStoryItem
import com.frhnfath.storyappfix.data.StoryDatabase
import com.frhnfath.storyappfix.data.StoryRemoteMediator
import com.frhnfath.storyappfix.network.ApiService

class StoryRepository(private val storyDatabase: StoryDatabase,private val apiService: ApiService) {
    fun getAllStoriesPaging(token: String): LiveData<PagingData<ListStoryItem>> {
        Log.d("DATA", "getAllStoriesPaging: $token")
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }).liveData
    }
}