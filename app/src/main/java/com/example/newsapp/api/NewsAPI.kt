package com.example.newsapp.api

import com.example.newsapp.models.NewsResponse
import com.example.newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "ru",
        @Query("category")
        category: String = "technology",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
}

/**
 * https://newsapi.org/v2/everything?q=bitcoin&apiKey=da552a64d5a44ad19de7d48c95ea3e3a
{
    "status": "ok",
    "totalResults": 812,
    "articles": [
                    {
                        "source": {
                                    "id": "cnn",
                                    "name": "CNN"
                                  },
                        "author": "Chloe Melas and Cheri Mossburg, CNN",
                        "title": "Anne Heche is in stable condition after a fiery car crash at a Los Angeles home",
                        "description": "Anne Heche is in stable condition Sunday, two days after the car she was driving crashed into a home and became engulfed in flames, a representative for the actress said.",
                        "url": "https://www.cnn.com/2022/08/07/entertainment/anne-heche-car-crash-sunday/index.html",
                        "urlToImage": "https://cdn.cnn.com/cnnnext/dam/assets/220805193758-anne-heche-file-restricted-113021-super-tease.jpg",
                        "publishedAt": "2022-08-07T18:23:14Z",
                        "content": "(CNN)Anne Heche is in stable condition Sunday, two days after the car she was driving crashed into a home and became engulfed in flames, a representative for the actress said.\r\n\"Her family and friend… [+3028 chars]"
                    },

                    {
                        "source": {
                            "id": null,
                            ...
                            ...
                    },
            ]
 * */

/**
https://newsapi.org/v2/top-headlines?country=us&apiKey=da552a64d5a44ad19de7d48c95ea3e3a
 */