package com.tobe.newsapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tobe.newsapp.data.repository.NewsRepository

class NewsViewModelProviderFactory(val newsRepository: NewsRepository,val app : Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRepository) as T
    }
}