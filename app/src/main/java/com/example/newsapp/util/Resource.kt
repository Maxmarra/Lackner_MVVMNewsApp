package com.example.newsapp.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)

    // message не nullable потому что если будет ошибка
    // обязательно будет сообщение
    // data nullable потому что мы не всегда можем иметь
    // body Response при error
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    class Loading<T> : Resource<T>()
}