package com.example.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>)
            : Resource<NewsResponse> {

        if (response.isSuccessful) {

            response.body()?.let { resultResponse ->
                //мы сохраняем полученный ответ от сети в поле data класса Resource
                //этот класс создан спецом для этого и у нас либо приходят
                //данные в поле data, либо мы переходим к Resource.Error
                return Resource.Success(data = resultResponse)
            }
        }
        return Resource.Error(message = response.message())
    }
}