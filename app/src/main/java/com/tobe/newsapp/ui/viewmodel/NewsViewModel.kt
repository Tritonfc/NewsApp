package com.tobe.newsapp.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tobe.newsapp.NewsAppApplication
import com.tobe.newsapp.api.NewsResponse
import com.tobe.newsapp.data.models.Article
import com.tobe.newsapp.data.repository.NewsRepository
import com.tobe.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    app : Application,
    private val repository: NewsRepository
) : AndroidViewModel(app) {

    val breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNewsPage = 1

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNewsPage = 1

    init {
        getBreakingNews("us")
    }


    fun getBreakingNews(countryCode:String) = viewModelScope.launch {
      saveBreakingNewsCall(countryCode)
    }

   private suspend fun saveBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = repository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }else{
                breakingNews.postValue(Resource.Error("There is no internet connection"))
            }
        } catch (t : Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private suspend fun saveSearchNewsCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = repository.searchNews(searchQuery, breakingNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                searchNews.postValue(Resource.Error("There is no internet connection"))
            }
        } catch (t : Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    fun searchNews(searchQuery : String) = viewModelScope.launch {
      saveSearchNewsCall(searchQuery)
    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {respond ->
                return Resource.Success(respond)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun getSavedNews() = repository.getSavedNews()


    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }

    private fun hasInternetConnection(): Boolean{
        val cm = getApplication<NewsAppApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = cm.activeNetwork?: return false
            val cap =cm.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                cap.hasTransport(TRANSPORT_WIFI) -> true
                cap.hasTransport(TRANSPORT_CELLULAR)-> true
                cap.hasTransport(TRANSPORT_ETHERNET)-> true

                else -> false
            }

        }else{

        }

        return false

    }
}