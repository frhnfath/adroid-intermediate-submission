package com.frhnfath.storyappfix.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frhnfath.storyappfix.data.ListStoryItem

class MapsViewModel(token: String): ViewModel() {
    private val mutableMap = MutableLiveData<List<ListStoryItem>>()
    private val locationMap: LiveData<List<ListStoryItem>> = mutableMap

}