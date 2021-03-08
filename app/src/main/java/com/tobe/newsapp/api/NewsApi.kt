package com.tobe.newsapp.api

import com.tobe.newsapp.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    companion object {
        const val BASE_URL = "https://newsapi.org/"
        const val CLIENT_ID = BuildConfig.NEWS_API_ACCESS_KEY
    }


    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,

        @Query("apiKey")
        apiKey: String = CLIENT_ID
    ):Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchNews: String,
        @Query("page")
        pageNumber: Int = 1,

        @Query("apiKey")
        apiKey: String = CLIENT_ID
    ):Response<NewsResponse>
}