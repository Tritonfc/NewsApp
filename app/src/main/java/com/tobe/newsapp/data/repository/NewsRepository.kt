package com.tobe.newsapp.data.repository

import com.tobe.newsapp.api.NewsApi
import com.tobe.newsapp.api.NewsResponse
import com.tobe.newsapp.data.db.ArticleDao
import com.tobe.newsapp.data.models.Article
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApi: NewsApi,private val dao: ArticleDao) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        newsApi.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        newsApi.searchForNews(searchQuery, pageNumber)


    suspend fun  upsert(article: Article) = dao.upsert(article)
    suspend fun  delete(article: Article) = dao.delete(article)
    fun getSavedNews() = dao.getAllArticles()


}