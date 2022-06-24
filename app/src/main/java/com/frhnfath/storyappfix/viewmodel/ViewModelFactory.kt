package com.frhnfath.storyappfix.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frhnfath.storyappfix.di.Injection
import java.lang.IllegalArgumentException

class ViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepo(context)) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}