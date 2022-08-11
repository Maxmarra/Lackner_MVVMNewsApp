package com.example.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

    val breakingNews = MutableLiveData<Resource<NewsResponse>>()
    var breakingNewsPage = 1
    //переменная для пагинации
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    //переменная для пагинации
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("ru")
    }

    //на второй параметр pageNumber мы завели переменную var breakingNewsPage = 1
    //поэтому не передаем ее в этот метод
    fun getBreakingNews(countryCode: String)

        = viewModelScope.launch {

        //в начале запроса передали в Resource - состояние Resource.Loading()
        //что будет отображаться на UI мы опишем уже в самом фрагменте
        // в данном случае через when(response) {
        // is Resource.Loading -> мы покажем спиннер showProgressBar()
        // то есть Resource.Loading,Success,Error это такие флажки
        //устанавливаемые на каждом этапе запроса данных
        //но их все равно нужно будет описать
            breakingNews.postValue(Resource.Loading())

            //сделали сетевой запрос
            val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)

            //в методе handleBreakingNewsResponse() обработали полученный
            //результ, можно было сделать прям здесь в коде
            breakingNews.postValue(handleBreakingNewsResponse(response))
    }



    fun searchNews(searchQuery: String) = viewModelScope.launch {

        searchNews.postValue(Resource.Loading())

        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }


    private fun handleBreakingNewsResponse(
        response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if(breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if(searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    //опять так как это LiveData мы не используем корутину для вызова этой функции
    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

}