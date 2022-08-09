package com.example.newsapp.db

import androidx.room.TypeConverter
import com.example.newsapp.models.Source

//Room работает только с примитивами
//для custom классов нужно делать конвертацию
// как для класса Source
class Converters {

    //говорим Room откуда взять понятный
    //ему формат например String у name
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    //первый параметр это source.name
    //то есть КУДА ПИШЕМ, а второй - name -
    // ЧТО ПИШЕМ
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}