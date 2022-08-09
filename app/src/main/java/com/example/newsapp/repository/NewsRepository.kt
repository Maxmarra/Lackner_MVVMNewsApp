package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.models.NewsResponse
import retrofit2.Response

class NewsRepository(val db: ArticleDatabase) {

    //поле apiKey передавать не надо
    // его значение уже указано в NewsApi!
    // все что там уже стоит по умолчанию
    // передавать не надо!!!
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int)
        : Response<NewsResponse> {
            return RetrofitInstance.api.getBreakingNews(
                countryCode, pageNumber
            )
    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int)
        = RetrofitInstance.api.searchForNews(searchQuery, pageNumber)
}