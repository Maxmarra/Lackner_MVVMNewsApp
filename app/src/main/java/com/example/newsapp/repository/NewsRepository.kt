package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import retrofit2.Response

// Здесь мы продублировали все методы из
// NewsApi + СДЕЛАЛИ запрос по сети к API (РЕАЛЬНЫЙ ВЫЗОВ ЭТОГО МЕТОДА ПРОИЗОЙДЕТ ВО VIEWMODEL!)
// и ArticleDao - здесь просто сделали из NewsRepository прокладку

class NewsRepository(val db: ArticleDatabase) {

        /////////////API//////////

    //поле apiKey передавать не надо
    // его значение уже указано в NewsApi!
    // все что там уже стоит по умолчанию
    // передавать не надо!!!
    suspend fun getBreakingNews(pageNumber: Int)
        : Response<NewsResponse> {
            return RetrofitInstance.api.getBreakingNews(
                pageNumber = pageNumber
            )
    }
    suspend fun searchNews(searchQuery: String, pageNumber: Int)
        = RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

        /////////DATABASE/////

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    //нет suspend так как данный метод возвращает LiveData!!
    //поменяли названия для конкретики
    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)



}