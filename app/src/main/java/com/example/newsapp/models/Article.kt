package com.example.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "articles")
data class Article (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
): Serializable

//Room работает только с примитивами
//для custom классов нужно делать конвертацию
// как для класса Source

//мы будем сохранять в базу только то
//то что нам нравиться
//а не все статьи, поэтому не все статьи
//будут иметь id