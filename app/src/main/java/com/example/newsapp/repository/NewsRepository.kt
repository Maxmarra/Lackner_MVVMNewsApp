package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase

class NewsRepository(val db: ArticleDatabase) {

    //поле apiKey передавать не надо
    // его значение уже указано в NewsApi!
    // все что там уже стоит по умолчанию
    // передавать не надо!!!
    suspend fun getBreakingNews(
        countryCode: String, pageNumber: Int) =

        RetrofitInstance.api.getBreakingNews(
            countryCode, pageNumber)
}