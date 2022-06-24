package com.frhnfath.storyappfix.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.frhnfath.storyappfix.data.ListStoryItem
import com.frhnfath.storyappfix.network.ApiService
import java.lang.Exception

class StoryPagingSource(private val apiService: ApiService, private val header: String) : PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            Log.d("Load", "load: working")
            val position = params.key ?: INITIAL_PAGE_INDEX
            Log.d("TAG result", "$position")
            val responseData = apiService.getAllStories(header, position, params.loadSize)
            val data = responseData.listStory
            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (data.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}