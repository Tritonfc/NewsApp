package com.tobe.newsapp.api

import com.tobe.newsapp.data.models.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)