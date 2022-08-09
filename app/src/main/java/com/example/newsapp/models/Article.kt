package com.example.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "articles"
)
data class Article(
    //мы будем сохранять в базу только то
    //то что нам нравиться
    //а не все статьи, поэтому не все статьи
    //будут иметь id
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
//Room работает только с примитивами
//для custom классов нужно делать конвертацию
// как для класса Source
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)