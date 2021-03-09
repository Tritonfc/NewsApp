package com.tobe.newsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.tobe.newsapp.data.repository.NewsRepository

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {
}