package com.tobe.newsapp.data.db

import androidx.room.TypeConverter
import com.tobe.newsapp.data.models.Source

class Converters {


    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(string: String): Source {
        return Source(string, string)
    }
}