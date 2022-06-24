package com.frhnfath.storyappfix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.frhnfath.storyappfix.data.ListStoryItem
import com.frhnfath.storyappfix.paging.StoryRepository
import com.frhnfath.storyappfix.session.UserPreferences

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private lateinit var mUserPreferences: UserPreferences
    fun story(token: String): LiveData<PagingData<ListStoryItem>> = storyRepository.getAllStoriesPaging(token).cachedIn(viewModelScope)
}