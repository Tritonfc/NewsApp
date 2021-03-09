package com.tobe.newsapp.data.repository

import com.tobe.newsapp.api.NewsApi
import com.tobe.newsapp.api.NewsResponse
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApi: NewsApi) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        newsApi.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        newsApi.searchForNews(searchQuery, pageNumber)

}